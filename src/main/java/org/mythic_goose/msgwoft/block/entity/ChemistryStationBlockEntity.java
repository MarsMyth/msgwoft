package org.mythic_goose.msgwoft.block.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.mythic_goose.msgwoft.init.ModBlockEntities;
import org.mythic_goose.msgwoft.init.ModItems;
import org.mythic_goose.msgwoft.recipe.ChemistryStationRecipe;
import org.mythic_goose.msgwoft.recipe.ChemistryStationRecipeManager;
import org.mythic_goose.msgwoft.screen.block.ChemistryStationMenu;

import java.util.List;
import java.util.Optional;

public class ChemistryStationBlockEntity extends BaseContainerBlockEntity
        implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedContainer {

    public static final int SLOT_PRIMARY     = 0;
    public static final int SLOT_RED         = 1;
    public static final int SLOT_GREEN       = 2;
    public static final int SLOT_BLUE        = 3;
    public static final int SLOT_PURPLE      = 4;
    public static final int SLOT_PROBABILITY = 5;
    public static final int CONTAINER_SIZE   = 6;

    public static final int DATA_CONCENTRATION   = 0;
    public static final int DATA_CRYSTALLIZATION = 1;
    public static final int DATA_QUALITY         = 2;
    public static final int DATA_PROBABILITY     = 3;
    public static final int DATA_COUNT           = 4;

    private static final int SCALE = 10000;

    private NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);

    private int syncConc = 0;
    private int syncCrys = 0;
    private int syncQual = 0;
    private int syncProb = 0;

    public final ContainerData data = new ContainerData() {
        @Override public int get(int i) {
            return switch (i) {
                case DATA_CONCENTRATION   -> syncConc;
                case DATA_CRYSTALLIZATION -> syncCrys;
                case DATA_QUALITY         -> syncQual;
                case DATA_PROBABILITY     -> syncProb;
                default -> 0;
            };
        }
        @Override public void set(int i, int v) {
            switch (i) {
                case DATA_CONCENTRATION   -> syncConc = v;
                case DATA_CRYSTALLIZATION -> syncCrys = v;
                case DATA_QUALITY         -> syncQual = v;
                case DATA_PROBABILITY     -> syncProb = v;
            }
        }
        @Override public int getCount() { return DATA_COUNT; }
    };

    public ChemistryStationBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CHEMISTRY_STATION_ENTITY, pos, state);
    }

    // ── Container ─────────────────────────────────────────────────────────────

    @Override protected @NotNull Component getDefaultName() {
        return Component.translatable("container.msgwoft.chemistry_station");
    }
    @Override public @NotNull NonNullList<ItemStack> getItems()              { return items; }
    @Override protected void setItems(NonNullList<ItemStack> l)     { this.items = l; }
    @Override public int getContainerSize()                         { return CONTAINER_SIZE; }
    @Override public int getMaxStackSize()                          { return 64; }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) stack.setCount(getMaxStackSize());
        setChanged();
        if (level != null && !level.isClientSide()) {
            if (slot == SLOT_PRIMARY) {
                readStatsFromItem();   // also calls recalcProbability
            } else if (slot == SLOT_PROBABILITY) {
                recalcProbability();
            }
        }
    }

    // ── Screen factory ────────────────────────────────────────────────────────

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory inv) {
        return new ChemistryStationMenu(syncId, inv, this, getBlockPos());
    }
    @Override public BlockPos getScreenOpeningData(ServerPlayer p)  { return getBlockPos(); }
    @Override public Component getDisplayName()                     { return getDefaultName(); }

    // ── Stats ─────────────────────────────────────────────────────────────────

    public void readStatsFromItem() {
        ItemStack primary = items.get(SLOT_PRIMARY);

        if (primary.isEmpty() || !primary.is(ModItems.IDENTIFIED_DATA_SPECIMEN)) {
            syncConc = 0;
            syncCrys = 0;
            syncQual = 0;
            recalcProbability();
            return;
        }

        CompoundTag tag = primary.getOrDefault(DataComponents.CUSTOM_DATA,
                CustomData.EMPTY).copyTag();

        // Values stored as 0.0–1.0 floats in the item's NBT
        syncConc = Math.round(tag.getFloat("Concentration")   * SCALE);
        syncCrys = Math.round(tag.getFloat("Crystallization") * SCALE);
        syncQual = Math.round(tag.getFloat("Quality")         * SCALE);

        recalcProbability();
    }

    public void recalcProbability() {
        float base    = (syncConc + syncCrys + syncQual) / (3f * SCALE);
        int   emeralds = items.get(SLOT_PROBABILITY).getCount();
        float total   = Math.min(1f, base + emeralds * 0.05f);
        syncProb = Math.round(total * SCALE);
        setChanged();
    }

    // ── Client accessors (read synced ContainerData) ──────────────────────────

    public float getConcentration()    { return syncConc / (float) SCALE; }
    public float getCrystallization()  { return syncCrys / (float) SCALE; }
    public float getQuality()          { return syncQual / (float) SCALE; }
    public float getTotalProbability() { return syncProb / (float) SCALE; }

    // ── Crafting ──────────────────────────────────────────────────────────────

    /**
     * Called server-side from ChemistryCraftPacket.
     * @param sequence ordered list of Colors the player queued in the bar
     */
    public void tryCraft(List<ChemistryStationRecipe.Color> sequence) {
        if (level == null || level.isClientSide()) return;

        ItemStack primary = items.get(SLOT_PRIMARY);
        if (primary.isEmpty() || !primary.is(ModItems.IDENTIFIED_DATA_SPECIMEN)) return;

        // 1. Strict sequence match against loaded recipes
        Optional<ChemistryStationRecipe> matchOpt =
                ChemistryStationRecipeManager.findMatchBySequence(sequence);

        if (matchOpt.isEmpty()) {
            // No recipe — consume specimen anyway, output dirt
            items.set(SLOT_PRIMARY, new ItemStack(Items.DIRT));
            setChanged();
            return;
        }

        ChemistryStationRecipe recipe = matchOpt.get();

        // 2. Confirm slots have the correct items and counts
        if (!ChemistryStationRecipeManager.slotsHaveIngredients(recipe, items)) {
            for (ChemistryStationRecipe.Color color : ChemistryStationRecipe.Color.values()) {
                int needed = recipe.countOf(color);
                if (needed == 0) continue;
                int slotIdx = ChemistryStationRecipeManager.colorToSlotIndex(color);
                ItemStack slot = items.get(slotIdx);
                String required = recipe.itemIdFor(color);
                String actual = slot.isEmpty() ? "EMPTY"
                        : BuiltInRegistries.ITEM.getKey(slot.getItem()).toString();
                System.out.println("[ChemistryStation] " + color
                        + " needs " + needed + "x " + required
                        + " | slot has " + slot.getCount() + "x " + actual);
            }
            return;
        }

        // 3. Consume chemicals (required count per color, regardless of success/fail)
        for (ChemistryStationRecipe.Color color : ChemistryStationRecipe.Color.values()) {
            int needed = recipe.countOf(color);
            if (needed > 0) consumeChemical(
                    ChemistryStationRecipeManager.colorToSlotIndex(color), needed);
        }

        // 4. Consume the DataSpecimen
        items.set(SLOT_PRIMARY, ItemStack.EMPTY);

        // 5. Probability roll
        boolean success = level.getRandom().nextFloat() <= getTotalProbability();
        items.set(SLOT_PRIMARY, success
                ? ChemistryStationRecipeManager.buildResult(recipe)
                : new ItemStack(Items.DIRT));

        setChanged();
        readStatsFromItem(); // refresh display for whatever is now in the slot
    }

    private void consumeChemical(int slot, int amount) {
        ItemStack stack = items.get(slot);
        if (!stack.isEmpty() && amount > 0) {
            stack.shrink(amount);
            if (stack.isEmpty()) items.set(slot, ItemStack.EMPTY);
        }
    }

    // ── NBT ───────────────────────────────────────────────────────────────────

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider reg) {
        super.saveAdditional(tag, reg);
        ContainerHelper.saveAllItems(tag, items, reg);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider reg) {
        super.loadAdditional(tag, reg);
        items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, reg);
        readStatsFromItem();
    }
}