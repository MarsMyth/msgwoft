package org.mythic_goose.msgwoft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mythic_goose.msgwoft.block.entity.DimensionalWarpgateBlockEntity;
import org.mythic_goose.msgwoft.init.ModBlockEntities;
import org.mythic_goose.msgwoft.item.beta.StabilizationDeviceItem;

public class DimensionalWarpgateBlock extends BaseEntityBlock {
    public static final MapCodec<DimensionalWarpgateBlock> CODEC = simpleCodec(DimensionalWarpgateBlock::new);

    // Slightly smaller box shown when holding the chip
    private static final VoxelShape CHIP_SHAPE = Shapes.box(0.1, 0.1, 0.1, 0.9, 0.9, 0.9);

    public DimensionalWarpgateBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DimensionalWarpgateBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.DIMENSIONAL_WARPGATE_ENTITY,
                DimensionalWarpgateBlockEntity::tick);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    // Always full hitbox so right-click works for everyone
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    // Controls the visible outline
    @Override
    protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext entityCtx
                && entityCtx.getEntity() instanceof Player player) {
            if (player.isCreative()) return Shapes.block();
            // Show smaller outline when holding the chip
            for (InteractionHand hand : InteractionHand.values()) {
                if (player.getItemInHand(hand).getItem() instanceof StabilizationDeviceItem) {
                    return CHIP_SHAPE;
                }
            }
        }
        return Shapes.empty();
    }

    // Prevent survival players from breaking it
    @Override
    protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        if (player.isCreative()) return super.getDestroyProgress(state, player, level, pos);
        return 0.0f;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level,
                                               BlockPos pos, Player player,
                                               BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof DimensionalWarpgateBlockEntity warpgate)) return InteractionResult.PASS;

        if (warpgate.isStabilized()) {
            player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("§aDimensional Tear is already stabilized."), true);
            return InteractionResult.SUCCESS;
        }

        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack held = player.getItemInHand(hand);
            if (held.getItem() instanceof StabilizationDeviceItem) {
                warpgate.setStabilized(true);
                warpgate.setChanged();
                level.sendBlockUpdated(pos, state, state, 3);

                EquipmentSlot slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;

                if (!player.isCreative()) {
                    held.hurtAndBreak(1, player, slot);
                }

                level.playSound(null, pos, SoundEvents.END_PORTAL_SPAWN,
                        SoundSource.BLOCKS, 1.0f, 1.2f);
                player.displayClientMessage(
                        net.minecraft.network.chat.Component.literal("§6Dimensional Tear stabilized!"), true);
                player.displayClientMessage(
                        net.minecraft.network.chat.Component.literal("§5You stabilize it and you start feeling desynchronized from the plane of reality. A Synchronized Orb is required to traverse withn"), false);
                return InteractionResult.SUCCESS;
            }
        }

        player.displayClientMessage(
                net.minecraft.network.chat.Component.literal("§cDimensional Tear Unstable - Stabilize it"), true);
        return InteractionResult.PASS;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide) return;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof DimensionalWarpgateBlockEntity warpgate)) return;
        if (!warpgate.isStabilized()) return;

        warpgate.teleportEntity(entity, level);
    }
}