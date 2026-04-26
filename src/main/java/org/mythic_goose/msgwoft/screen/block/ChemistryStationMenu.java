package org.mythic_goose.msgwoft.screen.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.mythic_goose.msgwoft.block.entity.ChemistryStationBlockEntity;
import org.mythic_goose.msgwoft.init.ModItems;
import org.mythic_goose.msgwoft.init.ModMenuTypes;
import org.mythic_goose.msgwoft.network.ChemistryCraftPacket;
import org.mythic_goose.msgwoft.recipe.ChemistryStationRecipe;

import java.util.ArrayList;
import java.util.List;

public class ChemistryStationMenu extends AbstractContainerMenu {

    private final ChemistryStationBlockEntity blockEntity;
    private final BlockPos pos;

    /** Client-side only: queued bar segments (colors in order of clicking) */
    private final List<ChemistryStationRecipe.Color> barQueue = new ArrayList<>();

    /** Client-side constructor — called by ExtendedScreenHandlerType */
    public ChemistryStationMenu(int syncId, Inventory playerInventory, BlockPos pos) {
        this(syncId, playerInventory,
                (ChemistryStationBlockEntity) playerInventory.player.level().getBlockEntity(pos),
                pos);
    }

    /** Server-side constructor — called from createMenu() */
    public ChemistryStationMenu(int syncId, Inventory playerInventory,
                                ChemistryStationBlockEntity blockEntity, BlockPos pos) {
        super(ModMenuTypes.CHEMISTRY_STATION, syncId);
        this.blockEntity = blockEntity;
        this.pos = pos;

        checkContainerSize(blockEntity, ChemistryStationBlockEntity.CONTAINER_SIZE);
        addDataSlots(blockEntity.data);

        if (blockEntity.getLevel() != null && !blockEntity.getLevel().isClientSide()) {
            blockEntity.readStatsFromItem();
        }

        // Slot 0: IDENTIFIED_DATA_SPECIMEN only, max 1
        addSlot(new Slot(blockEntity, ChemistryStationBlockEntity.SLOT_PRIMARY, 120, 121) {
            @Override public int getMaxStackSize() { return 1; }
            @Override public boolean mayPlace(ItemStack stack) {
                return stack.is(ModItems.IDENTIFIED_DATA_SPECIMEN);
            }
        });

        // Slots 1-4: Chemical slots (max 12 each)
        addSlot(new Slot(blockEntity, ChemistryStationBlockEntity.SLOT_RED,    227, 16)  { @Override public int getMaxStackSize() { return 12; } });
        addSlot(new Slot(blockEntity, ChemistryStationBlockEntity.SLOT_GREEN,  227, 54)  { @Override public int getMaxStackSize() { return 12; } });
        addSlot(new Slot(blockEntity, ChemistryStationBlockEntity.SLOT_BLUE,   227, 92)  { @Override public int getMaxStackSize() { return 12; } });
        addSlot(new Slot(blockEntity, ChemistryStationBlockEntity.SLOT_PURPLE, 227, 130) { @Override public int getMaxStackSize() { return 12; } });

        // Slot 5: Emerald booster — max 10, not consumed
        addSlot(new Slot(blockEntity, ChemistryStationBlockEntity.SLOT_PROBABILITY, 8, 131) {
            @Override public boolean mayPlace(ItemStack stack) { return stack.is(ModItems.LUCK_MODULE); }
            @Override public int getMaxStackSize() { return 10; }
        });

        // Player inventory
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 48 + col * 18, 174 + row * 18));
        for (int col = 0; col < 9; col++)
            addSlot(new Slot(playerInventory, col, 48 + col * 18, 232));
    }

    // ── Bar queue (client-side) ───────────────────────────────────────────────

    /**
     * Called when a colored button is clicked.
     * Checks that the corresponding slot has at least 1 item.
     * @return true if the segment was added
     */
    public boolean tryAddSegment(ChemistryStationRecipe.Color color) {
        if (barQueue.size() >= 12) return false;

        int slot = colorToSlot(color);
        // Count how many of this color are already queued vs. available
        long alreadyQueued = barQueue.stream().filter(c -> c == color).count();
        int available = blockEntity.getItem(slot).getCount();
        if (alreadyQueued >= available) return false;

        barQueue.add(color);
        return true;
    }

    /** Clears the bar queue (called on primary slot empty, GUI close, post-craft). */
    public void clearBarQueue() {
        barQueue.clear();
    }

    public List<ChemistryStationRecipe.Color> getBarQueue() {
        return java.util.Collections.unmodifiableList(barQueue);
    }

    private int colorToSlot(ChemistryStationRecipe.Color color) {
        return switch (color) {
            case RED    -> ChemistryStationBlockEntity.SLOT_RED;
            case GREEN  -> ChemistryStationBlockEntity.SLOT_GREEN;
            case BLUE   -> ChemistryStationBlockEntity.SLOT_BLUE;
            case PURPLE -> ChemistryStationBlockEntity.SLOT_PURPLE;
        };
    }

    // ── Craft ─────────────────────────────────────────────────────────────────

    public void clickCraftButton() {
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(
                new ChemistryCraftPacket(pos, new ArrayList<>(barQueue)));
        clearBarQueue();
    }

    // ── Slot changed ─────────────────────────────────────────────────────────

    @Override
    public void slotsChanged(net.minecraft.world.Container container) {
        super.slotsChanged(container);
        if (container == blockEntity
                && blockEntity.getLevel() != null
                && !blockEntity.getLevel().isClientSide()) {
            blockEntity.readStatsFromItem();
        }
    }

    // ── Quick move ───────────────────────────────────────────────────────────

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (slotIndex < ChemistryStationBlockEntity.CONTAINER_SIZE) {
                if (!moveItemStackTo(stack, ChemistryStationBlockEntity.CONTAINER_SIZE, slots.size(), true))
                    return ItemStack.EMPTY;
            } else {
                if (!moveItemStackTo(stack, 0, ChemistryStationBlockEntity.CONTAINER_SIZE, false))
                    return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
        }
        return result;
    }

    @Override public boolean stillValid(Player player) { return blockEntity.stillValid(player); }
    public ChemistryStationBlockEntity getBlockEntity() { return blockEntity; }
    public BlockPos getPos() { return pos; }
}