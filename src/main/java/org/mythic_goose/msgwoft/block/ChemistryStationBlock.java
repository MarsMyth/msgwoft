package org.mythic_goose.msgwoft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.mythic_goose.msgwoft.block.entity.ChemistryStationBlockEntity;
import org.mythic_goose.msgwoft.init.ModBlockEntities;

public class ChemistryStationBlock extends BaseEntityBlock {
    public static VoxelShape makeShape() {
        return Shapes.or(
                Shapes.create(0.125, 0, 0.125, 0.875, 0.0625, 0.875),
                Shapes.create(0.75, 0.0625, 0.3125, 0.875, 0.3125, 0.4375),
                Shapes.create(0.1875, 0.0625, 0.1875, 0.3125, 0.1875, 0.3125),
                Shapes.create(0.6875, 0.0625, 0.6875, 0.8125, 0.375, 0.8125),
                Shapes.create(0.1875, 0.0625, 0.5625, 0.3125, 0.25, 0.6875),
                Shapes.create(0.375, 0.075, 0.3125, 0.625, 0.075, 0.5625),
                Shapes.create(0.375, 0.075, 0.3125, 0.625, 0.1375, 0.3125),
                Shapes.create(0.375, 0.075, 0.5625, 0.625, 0.1375, 0.5625),
                Shapes.create(0.625, 0.075, 0.3125, 0.625, 0.1375, 0.5625),
                Shapes.create(0.375, 0.075, 0.3125, 0.375, 0.1375, 0.5625)
        );
    }

    public static final MapCodec<ChemistryStationBlock> CODEC = simpleCodec(ChemistryStationBlock::new);

    public ChemistryStationBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return makeShape();
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return makeShape();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChemistryStationBlockEntity(pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos,
                         BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof ChemistryStationBlockEntity be) {
                Containers.dropContents(level, pos, be);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ChemistryStationBlockEntity chemBE) {
                ((ServerPlayer) player).openMenu(chemBE);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return null; // No ticking needed — crafting is button-triggered
    }
}