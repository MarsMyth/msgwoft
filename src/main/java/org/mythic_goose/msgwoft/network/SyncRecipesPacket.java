package org.mythic_goose.msgwoft.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.mythic_goose.msgwoft.recipe.ChemistryStationRecipe;

import java.util.ArrayList;
import java.util.List;

public record SyncRecipesPacket(List<ChemistryStationRecipe> recipes) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncRecipesPacket> TYPE =
            new CustomPacketPayload.Type<>(
                    ResourceLocation.fromNamespaceAndPath("msgwoft", "sync_recipes"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncRecipesPacket> CODEC =
            StreamCodec.of(SyncRecipesPacket::encode, SyncRecipesPacket::decode);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private static void encode(RegistryFriendlyByteBuf buf, SyncRecipesPacket pkt) {
        buf.writeInt(pkt.recipes().size());
        for (ChemistryStationRecipe r : pkt.recipes()) {
            buf.writeUtf(r.getOutputItemId());
            buf.writeInt(r.getOutputCount());
            buf.writeInt(r.getIngredients().size());
            for (ChemistryStationRecipe.Ingredient ing : r.getIngredients()) {
                buf.writeUtf(ing.color().name());
                buf.writeUtf(ing.itemId());
            }
        }
    }

    private static SyncRecipesPacket decode(RegistryFriendlyByteBuf buf) {
        int count = buf.readInt();
        List<ChemistryStationRecipe> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String output = buf.readUtf();
            int outputCount = buf.readInt();
            int ingCount = buf.readInt();
            List<ChemistryStationRecipe.Ingredient> ings = new ArrayList<>();
            for (int j = 0; j < ingCount; j++) {
                ChemistryStationRecipe.Color color =
                        ChemistryStationRecipe.Color.valueOf(buf.readUtf());
                String itemId = buf.readUtf();
                ings.add(new ChemistryStationRecipe.Ingredient(color, itemId));
            }
            list.add(new ChemistryStationRecipe(output, outputCount, ings));
        }
        return new SyncRecipesPacket(list);
    }
}
