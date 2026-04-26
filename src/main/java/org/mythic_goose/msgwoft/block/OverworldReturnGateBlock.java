package org.mythic_goose.msgwoft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mythic_goose.msgwoft.block.entity.OverworldReturnGateBlockEntity;
import org.mythic_goose.msgwoft.init.ModBlockEntities;

public class OverworldReturnGateBlock extends BaseEntityBlock {
    public static final MapCodec<OverworldReturnGateBlock> CODEC = simpleCodec(OverworldReturnGateBlock::new);

    public OverworldReturnGateBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OverworldReturnGateBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.DIMENSIONAL_WARPGATE_ENTITY,
                OverworldReturnGateBlockEntity::tick);
    }




    // Prevent survival players from breaking it
    @Override
    protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        if (player.isCreative()) return super.getDestroyProgress(state, player, level, pos);
        return 0.0f;
    }
    

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide) return;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof OverworldReturnGateBlockEntity warpgate)) return;

        warpgate.teleportEntity(entity, level);
    }
}