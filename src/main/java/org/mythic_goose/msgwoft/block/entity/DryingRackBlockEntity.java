package org.mythic_goose.msgwoft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.mythic_goose.msgwoft.init.ModBlockEntities;
import org.mythic_goose.msgwoft.recipe.DryingRackRecipes;

public class DryingRackBlockEntity extends BlockEntity {
    public static final int DRYING_TIME = 1200;

    private ItemStack storedItem = ItemStack.EMPTY;
    // Cached output for the renderer to cross-fade toward — never null, may be EMPTY
    private ItemStack outputItem = ItemStack.EMPTY;
    private int dryingTimer = 0;

    public DryingRackBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DRYING_RACK, pos, state);
    }

    // ── Tick ──────────────────────────────────────────────────────────────────

    public static void tick(Level level, BlockPos pos, BlockState state,
                            DryingRackBlockEntity be) {
        if (level.isClientSide || be.storedItem.isEmpty()) return;

        be.dryingTimer++;

        // Resolve output item once
        if (be.outputItem.isEmpty() && !be.storedItem.isEmpty()) {
            ItemStack result = DryingRackRecipes.getResult(be.storedItem);
            if (result != null) {
                be.outputItem = result.copy();
            }
        }

        // Sync timer to client every tick so renderer can interpolate progress
        level.sendBlockUpdated(pos, state, state, 3);

        if (be.dryingTimer >= DRYING_TIME) {
            if (!be.outputItem.isEmpty()) {
                be.storedItem = be.outputItem.copy();
            }
            be.outputItem = ItemStack.EMPTY;
            be.dryingTimer = 0;
            be.setChanged();
        }
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    public ItemStack getStoredItem() { return storedItem; }

    public ItemStack getOutputItem() { return outputItem; }

    public void setStoredItem(ItemStack stack) {
        this.storedItem = stack;
        this.outputItem = ItemStack.EMPTY; // reset; tick will re-resolve
        this.dryingTimer = 0;
        if (level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    /**
     * Smooth progress using partial tick — call from renderer only.
     * Returns 0.0–1.0.
     */
    public float getDryingProgress(float partialTick) {
        if (storedItem.isEmpty()) return 0f;
        return Math.clamp((dryingTimer + partialTick) / DRYING_TIME, 0f, 1f);
    }

    // ── NBT ───────────────────────────────────────────────────────────────────

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (!storedItem.isEmpty())
            tag.put("StoredItem", storedItem.save(registries));
        if (!outputItem.isEmpty())
            tag.put("OutputItem", outputItem.save(registries));
        tag.putInt("DryingTimer", dryingTimer);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        storedItem = ItemStack.parseOptional(registries, tag.getCompound("StoredItem"));
        outputItem = ItemStack.parseOptional(registries, tag.getCompound("OutputItem"));
        dryingTimer = tag.getInt("DryingTimer");
    }

    // ── Sync to client ────────────────────────────────────────────────────────

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}