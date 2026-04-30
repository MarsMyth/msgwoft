package org.mythic_goose.msgwoft.entity.goal;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import org.mythic_goose.msgwoft.entity.RavenEntity;

public class RavenFollowOwnerGoal extends FollowOwnerGoal {
    private final TamableAnimal tameable;

    public RavenFollowOwnerGoal(
            TamableAnimal tameable,
            double speed,
            float minDistance,
            float maxDistance
            // leavesAllowed removed: FollowOwnerGoal in 1.21.1 no longer accepts this param
    ) {
        super(tameable, speed, minDistance, maxDistance);
        this.tameable = tameable;
    }

    private boolean isDeliveringBundle() {
        return this.tameable.getEntityData().get(RavenEntity.GOING_TO_RECEIVER);
    }

    @Override
    public boolean canUse() {
        return !isDeliveringBundle() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return !isDeliveringBundle() && super.canContinueToUse();
    }
}