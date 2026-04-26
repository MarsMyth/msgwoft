package org.mythic_goose.msgwoft.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import org.mythic_goose.msgwoft.init.ModSounds;

public class FlashbangPacketHandler {

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(FlashbangPacket.TYPE, (packet, context) -> {
            float intensity = packet.intensity();

            if (intensity > 0.0f) {
                context.client().execute(() -> {
                    LocalPlayer player = context.client().player;
                    if (player == null) return;

                    net.minecraft.sounds.SoundEvent ring = intensity >= 0.6f
                            ? ModSounds.FB_RING1
                            : ModSounds.FB_RING2;

                    context.client().getSoundManager().play(
                            SimpleSoundInstance.forUI(ring, 1.0F, intensity)
                    );
                });
            }
        });
    }
}