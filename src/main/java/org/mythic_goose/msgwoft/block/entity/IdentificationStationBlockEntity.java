package org.mythic_goose.msgwoft.block.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.mythic_goose.msgwoft.init.ModBlockEntities;
import org.mythic_goose.msgwoft.init.ModItems;
import org.mythic_goose.msgwoft.screen.block.IdentificationStationMenu;

import java.util.Random;

import static org.mythic_goose.msgwoft.item.DataSpecimenItem.randomColor;

public class IdentificationStationBlockEntity extends BaseContainerBlockEntity
        implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedContainer {

    public static final int INPUT_SLOT      = 0;
    public static final int LUCK_SLOT       = 1;
    public static final int EFFICIENCY_SLOT = 2;
    public static final int SLOT_COUNT      = 3;

    public static final int BASE_MAX_PROGRESS  = 6000;
    public static final int MIN_MAX_PROGRESS   = 60;  // fastest possible (10 diamonds)
    public static final int MAX_LUCK_STACKS    = 10;
    public static final int MAX_EFFICIENCY_STACKS = 10;

    private static final Random RANDOM = new Random();

    private int progress = 0;
    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    public IdentificationStationBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.IDENTIFICATION_STATION_ENTITY, pos, state);
    }

    @Override
    public @NotNull NonNullList<ItemStack> getItems() { return items; }

    @Override
    public int getContainerSize() { return SLOT_COUNT; }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        for (int i = 0; i < this.items.size(); i++)
            this.items.set(i, i < items.size() ? items.get(i) : ItemStack.EMPTY);
    }

    public void tick() {
        ItemStack input = items.get(INPUT_SLOT);
        boolean hasInput = !input.isEmpty() && input.is(ModItems.DATA_SPECIMEN);

        if (hasInput) {
            progress++;
            if (progress >= getMaxProgress()) {
                progress = 0;
                identify();
            }
        } else {
            progress = 0;
        }
        setChanged();
    }

    private void identify() {
        ItemStack input = items.get(INPUT_SLOT);
        ItemStack identified = new ItemStack(ModItems.IDENTIFIED_DATA_SPECIMEN);

        // Copy base NBT from the unidentified specimen
        identified.set(DataComponents.CUSTOM_DATA, input.get(DataComponents.CUSTOM_DATA));

        CompoundTag tag = identified.get(DataComponents.CUSTOM_DATA) != null
                ? identified.get(DataComponents.CUSTOM_DATA).copyTag()
                : new CompoundTag();

        // Apply luck boost
        int luckCount = getLuckCount();
        if (luckCount > 0) {
            tag.putFloat("Concentration",   applyLuck(tag.getFloat("Concentration"),   luckCount));
            tag.putFloat("Crystallization", applyLuck(tag.getFloat("Crystallization"), luckCount));
            tag.putFloat("Quality",         applyLuck(tag.getFloat("Quality"),         luckCount));
        }

        // Ensure Design and Color are always present (carried over from DataSpecimenItem)
        if (!tag.contains("Design")) tag.putInt("Design", RANDOM.nextInt(3));
        if (!tag.contains("Color"))  tag.putInt("Color",  randomColor());

        identified.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        items.set(INPUT_SLOT, identified);
    }

    /**
     * Each luck level gives a (luckCount * 5)% chance to boost the stat by up to 0.1.
     * At 10 emeralds: 50% chance of a boost per stat.
     */
    private float applyLuck(float base, int luckCount) {
        float chance = luckCount * 0.05f;
        if (RANDOM.nextFloat() < chance) {
            float boost = RANDOM.nextFloat() * 0.1f;
            return Math.min(1.0f, base + boost);
        }
        return base;
    }

    /**
     * Max progress scales down with diamonds in the efficiency slot.
     * 0 diamonds = 100 ticks, 10 diamonds = 60 ticks.
     */
    public int getMaxProgress() {
        int effCount = getEfficiencyCount();
        // Scale from 6000 ticks (0 diamonds) down to 60 ticks (10 diamonds)
        // Each diamond reduces by (6000 - 60) / 10 = 594 ticks
        return BASE_MAX_PROGRESS - (effCount * ((BASE_MAX_PROGRESS - MIN_MAX_PROGRESS) / MAX_EFFICIENCY_STACKS));
    }

    public int getProgress() { return progress; }

    private int getLuckCount() {
        ItemStack stack = items.get(LUCK_SLOT);
        if (stack.is(ModItems.LUCK_MODULE)) return Math.min(stack.getCount(), MAX_LUCK_STACKS);
        return 0;
    }

    private int getEfficiencyCount() {
        ItemStack stack = items.get(EFFICIENCY_SLOT);
        if (stack.is(ModItems.EFFICIENCY_MODULE)) return Math.min(stack.getCount(), MAX_EFFICIENCY_STACKS);
        return 0;
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.msgwoft.identification_station");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int syncId, Inventory inventory) {
        return new IdentificationStationMenu(syncId, inventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Progress", progress);
        ContainerHelper.saveAllItems(tag, items, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        progress = tag.getInt("Progress");
        ContainerHelper.loadAllItems(tag, items, registries);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayer player) {
        return this.worldPosition;
    }
}