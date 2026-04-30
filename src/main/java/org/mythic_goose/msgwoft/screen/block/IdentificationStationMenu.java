package org.mythic_goose.msgwoft.screen.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.mythic_goose.msgwoft.block.entity.IdentificationStationBlockEntity;
import org.mythic_goose.msgwoft.init.ModItems;
import org.mythic_goose.msgwoft.init.ModMenuTypes;

public class IdentificationStationMenu extends AbstractContainerMenu {

    private final IdentificationStationBlockEntity blockEntity;
    private final ContainerData propertyDelegate;

    public int getProgress()    { return propertyDelegate.get(0); }
    public int getMaxProgress() { return propertyDelegate.get(1); }

    // Client-side
    public IdentificationStationMenu(int syncId, Inventory inventory, BlockPos pos) {
        this(syncId, inventory, getBlockEntity(inventory, pos), new SimpleContainerData(2));
    }

    // Server-side (from block entity createMenu)
    public IdentificationStationMenu(int syncId, Inventory inventory,
                                     IdentificationStationBlockEntity blockEntity) {
        this(syncId, inventory, blockEntity, new SimpleContainerData(2) {
            @Override public int get(int i) {
                return i == 0 ? blockEntity.getProgress() : blockEntity.getMaxProgress();
            }
        });
    }

    // Full constructor
    public IdentificationStationMenu(int syncId, Inventory playerInventory,
                                     IdentificationStationBlockEntity blockEntity,
                                     ContainerData propertyDelegate) {
        super(ModMenuTypes.IDENTIFICATION_STATION, syncId);
        this.blockEntity      = blockEntity;
        this.propertyDelegate = propertyDelegate;

        checkContainerSize(blockEntity, IdentificationStationBlockEntity.SLOT_COUNT);

        // Input slot — DataSpecimen only
        addSlot(new Slot(blockEntity, IdentificationStationBlockEntity.INPUT_SLOT, 79, 40) {
            @Override public boolean mayPlace(ItemStack stack) {
                return stack.is(ModItems.DATA_SPECIMEN);
            }
        });

        // Luck slot — LUCK_MODULE only, max 10
        addSlot(new Slot(blockEntity, IdentificationStationBlockEntity.LUCK_SLOT, 16, 40) {
            @Override public boolean mayPlace(ItemStack stack) {
                return stack.is(ModItems.LUCK_MODULE);
            }
            @Override public int getMaxStackSize() { return 10; }
        });

        // Efficiency slot — EFFICIENCY_MODULE only, max 10
        addSlot(new Slot(blockEntity, IdentificationStationBlockEntity.EFFICIENCY_SLOT, 144, 40) {
            @Override public boolean mayPlace(ItemStack stack) {
                return stack.is(ModItems.EFFICIENCY_MODULE);
            }
            @Override public int getMaxStackSize() { return 10; }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addDataSlots(propertyDelegate);
    }

    private static IdentificationStationBlockEntity getBlockEntity(Inventory inventory, BlockPos pos) {
        BlockEntity be = inventory.player.level().getBlockEntity(pos);
        if (be instanceof IdentificationStationBlockEntity station) return station;
        throw new IllegalStateException("No IdentificationStationBlockEntity at " + pos);
    }

    public boolean isCrafting() { return propertyDelegate.get(0) > 0; }

    public int getScaledBulbProgress() {
        int progress    = propertyDelegate.get(0);
        int maxProgress = propertyDelegate.get(1);
        int bulbPixelSize = 14;
        return maxProgress != 0 && progress != 0 ? progress * bulbPixelSize / maxProgress : 0;
    }

    @Override
    public boolean stillValid(Player player) { return blockEntity.stillValid(player); }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();

            if (index < IdentificationStationBlockEntity.SLOT_COUNT) {
                // Machine slot → player inventory
                if (!moveItemStackTo(originalStack, IdentificationStationBlockEntity.SLOT_COUNT, slots.size(), true))
                    return ItemStack.EMPTY;
            } else {
                // Player inventory → correct machine slot based on item type
                if (originalStack.is(ModItems.DATA_SPECIMEN)) {
                    if (!moveItemStackTo(originalStack, IdentificationStationBlockEntity.INPUT_SLOT,
                            IdentificationStationBlockEntity.INPUT_SLOT + 1, false))
                        return ItemStack.EMPTY;
                } else if (originalStack.is(Items.EMERALD)) {
                    if (!moveItemStackTo(originalStack, IdentificationStationBlockEntity.LUCK_SLOT,
                            IdentificationStationBlockEntity.LUCK_SLOT + 1, false))
                        return ItemStack.EMPTY;
                } else if (originalStack.is(Items.DIAMOND)) {
                    if (!moveItemStackTo(originalStack, IdentificationStationBlockEntity.EFFICIENCY_SLOT,
                            IdentificationStationBlockEntity.EFFICIENCY_SLOT + 1, false))
                        return ItemStack.EMPTY;
                } else {
                    return ItemStack.EMPTY;
                }
            }

            if (originalStack.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
        }

        return newStack;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; i++)
            for (int l = 0; l < 9; l++)
                addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; i++)
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
    }
}