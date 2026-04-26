package org.mythic_goose.msgwoft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mythic_goose.msgwoft.block.entity.IdentificationStationBlockEntity;
import org.mythic_goose.msgwoft.init.ModBlockEntities;

public class IdentificationStationBlock extends BaseEntityBlock {
    public static final MapCodec<IdentificationStationBlock> CODEC = simpleCodec(IdentificationStationBlock::new);

    public IdentificationStationBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new IdentificationStationBlockEntity(pos, state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level,
                                                        BlockPos pos, Player player,
                                                        BlockHitResult hit) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof IdentificationStationBlockEntity be) {
            player.openMenu(be);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return createTickerHelper(type, ModBlockEntities.IDENTIFICATION_STATION_ENTITY,
                (lvl, pos, st, be) -> be.tick());
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof IdentificationStationBlockEntity be) {
                Containers.dropContents(level, pos, be);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}