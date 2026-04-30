package org.mythic_goose.msgwoft.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.mythic_goose.msgwoft.client.model.RavenModel;
import org.mythic_goose.msgwoft.entity.RavenEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RavenRenderer extends GeoEntityRenderer<RavenEntity> {

    public RavenRenderer(EntityRendererProvider.Context context) {
        super(context, new RavenModel());
    }
}