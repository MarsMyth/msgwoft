package org.mythic_goose.msgwoft.client.render;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Triple;
import org.mythic_goose.msgwoft.MSGWOFT;

public class ModRenderTypes {

    private static final ResourceLocation END_SKY =
            ResourceLocation.withDefaultNamespace("textures/environment/end_sky.png");
    private static final ResourceLocation END_PORTAL =
            ResourceLocation.withDefaultNamespace("textures/entity/end_portal.png");

    public static final RenderType WARPGATE_UNSTABLE = RenderType.create(
            "msgwoft:warpgate_unstable",
            DefaultVertexFormat.POSITION,
            VertexFormat.Mode.QUADS,
            1536,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(
                            () -> ModShaders.warpgateUnstableShader
                    ))
                    .setTextureState(new RenderStateShard.MultiTextureStateShard(
                            ImmutableList.of(
                                    Triple.of(END_SKY, false, false),
                                    Triple.of(END_PORTAL, false, false)
                            )
                    ))
                    .setTransparencyState(RenderStateShard.NO_TRANSPARENCY)
                    .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                    .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
                    .setCullState(RenderStateShard.NO_CULL)
                    .createCompositeState(false)
    );

    public static final RenderType WARPGATE_RETURN = RenderType.create(
            "msgwoft:warpgate_return",
            DefaultVertexFormat.POSITION,
            VertexFormat.Mode.QUADS,
            1536,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(
                            () -> ModShaders.warpgateReturnShader
                    ))
                    .setTextureState(new RenderStateShard.MultiTextureStateShard(
                            ImmutableList.of(
                                    Triple.of(END_SKY, false, false),
                                    Triple.of(END_PORTAL, false, false)
                            )
                    ))
                    .setTransparencyState(RenderStateShard.NO_TRANSPARENCY)
                    .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                    .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
                    .setCullState(RenderStateShard.NO_CULL)
                    .createCompositeState(false)
    );
}