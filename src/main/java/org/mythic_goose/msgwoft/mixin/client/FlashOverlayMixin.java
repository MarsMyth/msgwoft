package org.mythic_goose.msgwoft.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.mythic_goose.msgwoft.client.FlashbangClientHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class FlashOverlayMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void renderFlashOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        float intensity = FlashbangClientHandler.flashIntensity;
        if (intensity <= 0.0f) return;

        int screenW = guiGraphics.guiWidth();
        int screenH = guiGraphics.guiHeight();
        int alpha = (int) (intensity * 255);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.fill(0, 0, screenW, screenH, (alpha << 24) | 0x00FFFFFF);
        RenderSystem.disableBlend();
    }
}