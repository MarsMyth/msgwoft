package org.mythic_goose.msgwoft.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.mythic_goose.msgwoft.MSGWOFT;
import org.mythic_goose.msgwoft.block.entity.ChemistryStationBlockEntity;
import org.mythic_goose.msgwoft.recipe.ChemistryStationRecipe;

import java.util.ArrayList;
import java.util.List;

public record ChemistryCraftPacket(BlockPos pos, List<ChemistryStationRecipe.Color> sequence) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ChemistryCraftPacket> TYPE =
            new CustomPacketPayload.Type<>(
                    ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "chemistry_craft"));

    public static final StreamCodec<FriendlyByteBuf, ChemistryCraftPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, pkt) -> {
                        buf.writeBlockPos(pkt.pos());
                        buf.writeVarInt(pkt.sequence().size());
                        for (ChemistryStationRecipe.Color c : pkt.sequence()) {
                            buf.writeVarInt(c.ordinal());
                        }
                    },
                    buf -> {
                        BlockPos pos = buf.readBlockPos();
                        int size = buf.readVarInt();
                        List<ChemistryStationRecipe.Color> seq = new ArrayList<>(size);
                        for (int i = 0; i < size; i++) {
                            seq.add(ChemistryStationRecipe.Color.values()[buf.readVarInt()]);
                        }
                        return new ChemistryCraftPacket(pos, seq);
                    }
            );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void registerServer() {
        PayloadTypeRegistry.playC2S().register(TYPE, STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            context.server().execute(() -> {
                if (player.level().getBlockEntity(payload.pos()) instanceof ChemistryStationBlockEntity be) {
                    be.tryCraft(payload.sequence());
                }
            });
        });
    }
}