package org.mythic_goose.msgwoft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.mythic_goose.msgwoft.init.ModBlockEntities;

public class DimensionalWarpgateBlockEntity extends BlockEntity {

    private boolean stabilized = false;
    private int tickCount = 0;

    // Change these to your desired destination
    private static final BlockPos DESTINATION = new BlockPos(0, 64, 0);
    private static final net.minecraft.resources.ResourceKey<Level> DESTINATION_DIMENSION = Level.END;

    public DimensionalWarpgateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DIMENSIONAL_WARPGATE_ENTITY, pos, state);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos,
                                                    BlockState state, T blockEntity) {
        if (blockEntity instanceof DimensionalWarpgateBlockEntity be) {
            be.tickCount++;
        }
    }

    public int getTickCount() { return tickCount; }
    public boolean isStabilized() { return stabilized; }
    public void setStabilized(boolean stabilized) { this.stabilized = stabilized; }

    public void teleportEntity(Entity entity, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        // Cooldown: only trigger once per second
        if (entity.tickCount % 20 != 0) return;

        ServerLevel destination = serverLevel.getServer().getLevel(DESTINATION_DIMENSION);
        if (destination == null) return;

        level.playSound(null, worldPosition, SoundEvents.END_PORTAL_SPAWN,
                SoundSource.BLOCKS, 1.0f, 1.0f);

        // Re add later

//        Vec3 dest = Vec3.atCenterOf(DESTINATION);
//        entity.teleportTo(destination, dest.x, dest.y, dest.z,
//                java.util.Set.of(), entity.getYRot(), entity.getXRot());
    }

    // --- NBT --- (1.21.1 requires HolderLookup.Provider)

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("Stabilized", stabilized);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        stabilized = tag.getBoolean("Stabilized");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}