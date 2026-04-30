package org.mythic_goose.msgwoft.entity.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.mythic_goose.msgwoft.entity.RavenEntity;

import java.util.EnumSet;

public class RavenDeliverBundleGoal<T extends TamableAnimal> extends Goal {
    private final T tameable;
    private LivingEntity owner;
    private final Level world;
    private final double speed;
    private final PathNavigation navigation;
    private int updateCountdownTicks;
    private final float maxDistance;
    private final float minDistance;
    private float oldWaterPathfindingPenalty;
    private final boolean leavesAllowed;
    private LivingEntity receiver;

    public RavenDeliverBundleGoal(
            T tameable,
            double speed,
            float minDistance,
            float maxDistance,
            boolean leavesAllowed
    ) {
        this.tameable = tameable;
        this.world = tameable.level();
        this.speed = speed;
        this.navigation = tameable.getNavigation();
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.leavesAllowed = leavesAllowed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        if (!(tameable.getNavigation() instanceof GroundPathNavigation)
                && !(tameable.getNavigation() instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported navigation type for RavenDeliverBundleGoal");
        }
        if (!(tameable instanceof RavenEntity)) {
            throw new IllegalArgumentException("Unsupported mob type for RavenDeliverBundleGoal");
        }
    }

    private RavenEntity asRaven() {
        return (RavenEntity) this.tameable;
    }

    private boolean isDeliveringBundle() {
        return this.tameable.getEntityData().get(RavenEntity.GOING_TO_RECEIVER);
    }

    @Override
    public boolean canUse() {
        if (asRaven().getReceiverUuid() == null) return false;

        this.receiver = this.tameable.level().getPlayerByUUID(asRaven().getReceiverUuid());
        if (this.receiver == null) return false;

        LivingEntity livingEntity = this.tameable.getOwner();
        if (livingEntity == null || livingEntity.isSpectator()) return false;
        if (this.tameable.distanceToSqr(this.receiver) < (this.minDistance * this.minDistance)) return false;

        this.owner = livingEntity;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.navigation.isDone()) return false;
        return this.tameable.distanceToSqr(this.receiver) > (this.maxDistance * this.maxDistance);
    }

    @Override
    public void start() {
        this.updateCountdownTicks = 0;
        this.oldWaterPathfindingPenalty = this.tameable.getPathfindingMalus(PathType.WATER);
        this.tameable.setPathfindingMalus(PathType.WATER, 0.0F);
        this.tameable.getEntityData().set(RavenEntity.GOING_TO_RECEIVER, true);
    }

    @Override
    public void stop() {
        this.receiver = null;
        this.navigation.stop();
        this.tameable.setPathfindingMalus(PathType.WATER, this.oldWaterPathfindingPenalty);
    }

    @Override
    public void tick() {
        this.tameable.getLookControl().setLookAt(
                this.receiver,
                10.0F,
                (float) this.tameable.getMaxHeadXRot()
        );

        if (--this.updateCountdownTicks <= 0) {
            this.updateCountdownTicks = 10;

            if (this.tameable.distanceToSqr(this.receiver) >= 10_000.0) {
                tryTeleport();
            } else {
                this.navigation.moveTo(this.receiver, this.speed);
            }
        }
    }

    private void tryTeleport() {
        BlockPos receiverPos = this.receiver.blockPosition();
        asRaven().spawnFeatherParticles(10);

        for (int i = 0; i < 10; i++) {
            int dx = getRandomInt(-3, 3);
            int dy = getRandomInt(-1, 1);
            int dz = getRandomInt(-3, 3);
            if (tryTeleportTo(receiverPos.getX() + dx, receiverPos.getY() + dy, receiverPos.getZ() + dz)) {
                return;
            }
        }
    }

    private boolean tryTeleportTo(int x, int y, int z) {
        if (Math.abs(x - this.receiver.getX()) < 2.0
                && Math.abs(z - this.receiver.getZ()) < 2.0) {
            return false;
        }
        if (!canTeleportTo(new BlockPos(x, y, z))) return false;

        this.tameable.moveTo(
                x + 0.5, y, z + 0.5,
                this.tameable.yHeadRot,
                this.tameable.getXRot()
        );
        this.navigation.stop();
        return true;
    }

    private boolean canTeleportTo(BlockPos pos) {
        // In 1.21.1, getPathTypeStatic requires (PathfindingContext, MutableBlockPos)
        PathfindingContext context = new PathfindingContext(this.world, this.tameable);
        PathType nodeType = WalkNodeEvaluator.getPathTypeStatic(context, pos.mutable());
        if (nodeType != PathType.WALKABLE) return false;

        BlockState below = this.world.getBlockState(pos.below());
        if (!this.leavesAllowed && below.getBlock() instanceof LeavesBlock) return false;

        BlockPos offset = pos.subtract(this.tameable.blockPosition());
        return this.world.noCollision(this.tameable, this.tameable.getBoundingBox().move(offset));
    }

    private int getRandomInt(int min, int max) {
        return this.tameable.getRandom().nextInt(max - min + 1) + min;
    }
}