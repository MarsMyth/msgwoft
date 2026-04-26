package org.mythic_goose.msgwoft.client.render;

import net.minecraft.client.renderer.ShaderInstance;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.resources.ResourceLocation;
import org.mythic_goose.msgwoft.MSGWOFT;

public class ModShaders {

    public static ShaderInstance warpgateUnstableShader;
    public static ShaderInstance warpgateReturnShader;

    public static void register() {
        CoreShaderRegistrationCallback.EVENT.register(context -> {
            context.register(
                    ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "rendertype_warpgate_unstable"),
                    DefaultVertexFormat.POSITION,
                    shader -> warpgateUnstableShader = shader
            );
        });

        CoreShaderRegistrationCallback.EVENT.register(context -> {
            context.register(
                    ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "rendertype_warpgate_return"),
                    DefaultVertexFormat.POSITION,
                    shader -> warpgateReturnShader = shader
            );
        });
    }
}