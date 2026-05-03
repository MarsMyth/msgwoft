package org.mythic_goose.msgwoft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mythic_goose.msgwoft.block.entity.DryingRackBlockEntity;
import org.mythic_goose.msgwoft.init.ModBlockEntities;

public class DryingRackBlock extends BaseEntityBlock {
    public static final MapCodec<DryingRackBlock> CODEC = simpleCodec(DryingRackBlock::new);
    public static final BooleanProperty HAS_ITEM = BooleanProperty.create("has_item");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static VoxelShape makeSouthShape() {
        return Shapes.or(
                Shapes.create(0, 0.875, 0.875, 1, 1, 1)
        );
    }
    public static VoxelShape makeWestShape() {
        return Shapes.or(
                Shapes.create(0.9375, 0.875, 0, 1, 1, 1)
        );
    }
    public static VoxelShape makeEastShape() {
        return Shapes.or(
                Shapes.create(0, 0.875, 0, 0.125, 1, 1)
        );
    }
    public static VoxelShape makeNorthShape() {
        return Shapes.or(
                Shapes.create(0, 0.875, 0, 1, 1, 0.125)
        );
    }

    public static VoxelShape getShape(Direction facing) {
        return switch (facing) {
            case NORTH -> makeSouthShape();
            case EAST  -> makeEastShape();
            case WEST  -> makeWestShape();
            default    -> makeNorthShape();
        };
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter,
                                                    BlockPos blockPos, CollisionContext collisionContext) {
        return getShape(blockState.getValue(FACING));
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter,
                                           BlockPos blockPos, CollisionContext collisionContext) {
        return getShape(blockState.getValue(FACING));
    }

    public DryingRackBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(HAS_ITEM, false)
                .setValue(FACING, Direction.SOUTH));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HAS_ITEM, FACING);
    }

    // Place facing toward the player
    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection().getOpposite())
                .setValue(HAS_ITEM, false);
    }

    // ── Block entity ──────────────────────────────────────────────────────────

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DryingRackBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.DRYING_RACK,
                DryingRackBlockEntity::tick);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    // ── Interaction ───────────────────────────────────────────────────────────

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state,
                                                        @NotNull Level level, @NotNull BlockPos pos,
                                                        @NotNull Player player, @NotNull BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof DryingRackBlockEntity be))
            return InteractionResult.PASS;

        if (!level.isClientSide) {
            ItemStack stored = be.getStoredItem();
            ItemStack held = player.getMainHandItem();

            if (!stored.isEmpty()) {
                // Take item out
                player.getInventory().placeItemBackInInventory(stored.copy());
                be.setStoredItem(ItemStack.EMPTY);
                level.setBlockAndUpdate(pos, state.setValue(HAS_ITEM, false));
            } else if (!held.isEmpty()) {
                // Place item in
                be.setStoredItem(held.copyWithCount(1));
                held.shrink(1);
                level.setBlockAndUpdate(pos, state.setValue(HAS_ITEM, true));
            }
            be.setChanged();
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    // ── Drop stored item on break ─────────────────────────────────────────────

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                         BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof DryingRackBlockEntity be) {
                if (!be.getStoredItem().isEmpty()) {
                    net.minecraft.world.Containers.dropItemStack(
                            level, pos.getX(), pos.getY(), pos.getZ(),
                            be.getStoredItem());
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}