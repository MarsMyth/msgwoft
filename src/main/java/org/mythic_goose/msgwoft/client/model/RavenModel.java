package org.mythic_goose.msgwoft.client.model;

import net.minecraft.resources.ResourceLocation;
import org.mythic_goose.msgwoft.MSGWOFT;
import org.mythic_goose.msgwoft.entity.RavenEntity;
import software.bernie.geckolib.model.GeoModel;

public class RavenModel extends GeoModel<RavenEntity> {

    @Override
    public ResourceLocation getModelResource(RavenEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "geo/entity/raven.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RavenEntity entity) {
        return switch (entity.getRavenType()) {
            case ALBINO  -> ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "textures/entity/raven/raven_albino.png");
            case SEA_GREEN -> ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "textures/entity/raven/raven_sea_green.png");
            case THREE_EYED -> ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "textures/entity/raven/raven_three_eyed.png");
            default      -> ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "textures/entity/raven/raven_dark.png");
        };
    }

    @Override
    public ResourceLocation getAnimationResource(RavenEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "animations/entity/raven.animation.json");
    }
}