package org.mythic_goose.msgwoft.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.world.phys.Vec3;

/**
 * Sent server → client when a flashbang detonates near a player.
 * intensity: 0.0 (edge of radius) – 1.0 (direct hit)
 */
public record FlashbangPacket(float intensity, double x, double y, double z) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<FlashbangPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("msgwoft", "flashbang"));

    public static final StreamCodec<FriendlyByteBuf, FlashbangPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> {
                        buf.writeFloat(pkt.intensity());
                        buf.writeDouble(pkt.x());
                        buf.writeDouble(pkt.y());
                        buf.writeDouble(pkt.z());
                    },
                    buf -> new FlashbangPacket(buf.readFloat(), buf.readDouble(), buf.readDouble(), buf.readDouble())
            );



    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /** Call from ModNetworking.register() */
    public static void register() {
        PayloadTypeRegistry.playS2C().register(TYPE, CODEC);
    }

    public static void send(ServerPlayer player, float intensity, Vec3 pos) {
        ServerPlayNetworking.send(player, new FlashbangPacket(intensity, pos.x, pos.y, pos.z));
    }
}