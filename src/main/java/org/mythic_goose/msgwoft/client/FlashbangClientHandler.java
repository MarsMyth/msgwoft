package org.mythic_goose.msgwoft.client;

import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;
import foundry.veil.api.client.render.light.renderer.LightRenderer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.mythic_goose.msgwoft.network.FlashbangPacket;

public class FlashbangClientHandler {

    /** Current flash white-out intensity (0 = none, 1 = full white). Ticks down each frame. */
    public static float flashIntensity = 0.0f;
    /** How quickly the flash fades per tick. */
    private static final float FADE_RATE = 0.015f;

    /**
     * Register the S2C packet receiver.
     * Call from your ClientModInitializer.
     */
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(FlashbangPacket.TYPE, (packet, context) -> {
            context.client().execute(() -> {
                // Only apply screen flash if intensity > 0
                if (packet.intensity() > 0) {
                    flashIntensity = packet.intensity();
                }
                // Always spawn the light so everyone sees it
                spawnVeilLight(context.client(), packet.intensity(),
                        new Vec3(packet.x(), packet.y(), packet.z()));
            });
        });
    }

    public static void tickFlash() {
        if (flashIntensity > 0) {
            flashIntensity = Math.max(0, flashIntensity - FADE_RATE);
        }
    }
    private static void spawnVeilLight(Minecraft mc, float intensity, Vec3 pos) {
        if (mc.level == null) return;

        PointLightData light = new PointLightData();
        light.setPosition((float) pos.x, (float) pos.y + 0.5f, (float) pos.z);
        light.setRadius(24.0f);                              // fixed radius, always visible
        light.setColor(1.0f, 0.98f, 0.9f);
        light.setBrightness(Math.max(15.0f, intensity * 16.0f)); // minimum brightness of 0.5

        LightRenderHandle<PointLightData> handle = VeilRenderSystem.renderer()
                .getLightRenderer()
                .addLight(light);

        TemporaryLightManager.addTemporary(handle, 10);
    }


}

