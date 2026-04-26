package org.mythic_goose.msgwoft.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.joml.Quaternionf;
import org.mythic_goose.msgwoft.block.entity.DimensionalWarpgateBlockEntity;
import org.mythic_goose.msgwoft.client.render.ModRenderTypes;

public class DimensionalWarpgateBlockEntityRenderer
        implements BlockEntityRenderer<DimensionalWarpgateBlockEntity> {

    private static final int SEGMENTS = 64;
    private static final float RADIUS = 0.96f;

    public DimensionalWarpgateBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

    @Override
    public void render(DimensionalWarpgateBlockEntity be, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffers,
                       int packedLight, int packedOverlay) {

        float time = be.getTickCount() + partialTick;

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);

        // Face the camera
        Quaternionf cameraRot = Minecraft.getInstance()
                .getEntityRenderDispatcher().camera.rotation();
        poseStack.mulPose(cameraRot);

        float scale = be.isStabilized()
                ? 1.0f + 0.06f * (float) Math.sin(time * 0.1f)
                : 1.0f;
        poseStack.scale(scale, scale, scale);

        if (be.isStabilized()) {
            renderDisc(poseStack, buffers.getBuffer(RenderType.endPortal()));
        } else {
            renderDisc(poseStack, buffers.getBuffer(ModRenderTypes.WARPGATE_UNSTABLE));
        }

        poseStack.popPose();
    }

    private void renderDisc(PoseStack poseStack, VertexConsumer consumer) {
        PoseStack.Pose pose = poseStack.last();

        float[] xs = new float[SEGMENTS];
        float[] ys = new float[SEGMENTS];
        for (int i = 0; i < SEGMENTS; i++) {
            float angle = 2f * (float) Math.PI * i / SEGMENTS;
            xs[i] = RADIUS * (float) Math.cos(angle);
            ys[i] = RADIUS * (float) Math.sin(angle);
        }

        // Triangle fan via quads: center + two edge points + repeated last point
        for (int i = 0; i < SEGMENTS; i++) {
            int next = (i + 1) % SEGMENTS;
            consumer.addVertex(pose, 0f, 0f, 0f);
            consumer.addVertex(pose, xs[i], ys[i], 0f);
            consumer.addVertex(pose, xs[next], ys[next], 0f);
            consumer.addVertex(pose, xs[next], ys[next], 0f);
        }
    }

    @Override
    public boolean shouldRenderOffScreen(DimensionalWarpgateBlockEntity be) {
        return true;
    }
}