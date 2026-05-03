package org.mythic_goose.msgwoft.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.mythic_goose.msgwoft.block.DryingRackBlock;
import org.mythic_goose.msgwoft.block.entity.DryingRackBlockEntity;
import org.mythic_goose.msgwoft.client.render.AlphaVertexConsumer;

@Environment(EnvType.CLIENT)
public class DryingRackRenderer implements BlockEntityRenderer<DryingRackBlockEntity> {
    private static final float FADE_START = 0.5f;

    private final ItemRenderer itemRenderer;

    public DryingRackRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(DryingRackBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        ItemStack inputStack  = be.getStoredItem();
        ItemStack outputStack = be.getOutputItem();
        if (inputStack.isEmpty()) return;

        // Get facing from block state
        BlockState state = be.getBlockState();
        Direction facing = state.getValue(DryingRackBlock.FACING);

        float progress   = be.getDryingProgress(partialTick);
        float fadeFactor = Math.clamp((progress - FADE_START) / (1.0f - FADE_START), 0f, 1f);
        float inputAlpha  = 1.0f - fadeFactor;
        float outputAlpha = fadeFactor;

        poseStack.pushPose();

        // Translate to block center, then rotate around Y based on facing
        poseStack.translate(0.5, 0.5, 0.5);
        float yRot = switch (facing) {
            case SOUTH -> 0f;
            case NORTH -> 180f;
            case WEST  -> 90f;
            case EAST  -> 270f;
            default    -> 0f;
        };
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(yRot));
        // Re-apply the original Z offset after rotation (was 1.0 - 0.02 from center = 0.48)
        poseStack.translate(0.0, 0.0, 0.48);
        poseStack.scale(0.8f, 0.8f, 0.8f);

        if (inputAlpha > 0.01f) {
            renderWithAlpha(inputStack, inputAlpha, be, poseStack, bufferSource, packedLight, packedOverlay);
        }
        if (outputAlpha > 0.01f && !outputStack.isEmpty()) {
            renderWithAlpha(outputStack, outputAlpha, be, poseStack, bufferSource, packedLight, packedOverlay);
        }

        poseStack.popPose();
    }

    private void renderWithAlpha(ItemStack stack, float alpha, DryingRackBlockEntity be,
                                 PoseStack poseStack, MultiBufferSource bufferSource,
                                 int packedLight, int packedOverlay) {

        MultiBufferSource alphaSource = renderType -> {
            // Remap any solid/entity_solid render types to cutout so
            // alpha pixels are discarded rather than rendered black
            RenderType remapped = remapToCutout(renderType);
            return new AlphaVertexConsumer(bufferSource.getBuffer(remapped), alpha);
        };

        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED,
                packedLight, packedOverlay, poseStack, alphaSource, be.getLevel(), 0);
    }

    private static RenderType remapToCutout(RenderType type) {
        // Item models that use solid render types need to go through cutout
        // so transparent pixels in the texture are actually discarded
        if (type == RenderType.solid() || type == RenderType.cutoutMipped()) {
            return RenderType.cutout();
        }
        return type;
    }
}