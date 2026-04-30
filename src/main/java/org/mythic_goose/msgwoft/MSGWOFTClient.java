package org.mythic_goose.msgwoft;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.CustomData;
import org.mythic_goose.msgwoft.block.entity.OverworldReturnGateBlockEntity;
import org.mythic_goose.msgwoft.block.entity.renderer.DimensionalWarpgateBlockEntityRenderer;
import org.mythic_goose.msgwoft.block.entity.renderer.OverworldReturnGateBlockEntityRenderer;
import org.mythic_goose.msgwoft.client.FlashbangClientHandler;
import org.mythic_goose.msgwoft.client.TemporaryLightManager;
import org.mythic_goose.msgwoft.client.render.ModShaders;
import org.mythic_goose.msgwoft.client.renderer.RavenRenderer;
import org.mythic_goose.msgwoft.client.tooltip.RecipeTooltipComponent;
import org.mythic_goose.msgwoft.client.tooltip.RecipeTooltipData;
import org.mythic_goose.msgwoft.init.*;
import org.mythic_goose.msgwoft.network.FlashbangPacketHandler;
import org.mythic_goose.msgwoft.network.SyncRecipesPacket;
import org.mythic_goose.msgwoft.recipe.ChemistryStationRecipeManager;
import org.mythic_goose.msgwoft.screen.item.WrittenRecipeScreen;

public class MSGWOFTClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModShaders.register();
        ModScreens.init();

        // Color for item & design
        ItemProperties.register(
                ModItems.IDENTIFIED_DATA_SPECIMEN,
                ResourceLocation.fromNamespaceAndPath("msgwoft", "design"),
                (stack, world, entity, seed) -> {
                    CustomData data = stack.get(DataComponents.CUSTOM_DATA);
                    if (data == null) return 0f;
                    int design = data.copyTag().getInt("Design");
                    return design / 10f; // 0=0.0, 1=0.1, 2=0.2
                }
        );

        ColorProviderRegistry.ITEM.register(
                (stack, tintIndex) -> {
                    if (tintIndex == 1) {
                        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
                        if (data != null) {
                            CompoundTag tag = data.copyTag();
                            if (tag.contains("Color")) {
                                return tag.getInt("Color"); // already has 0xFF alpha prefix
                            }
                        }
                        return 0xFFFFFFFF; // white, no tint
                    }
                    return 0xFFFFFFFF; // layer0 unaffected
                },
                ModItems.IDENTIFIED_DATA_SPECIMEN
        );

        // Invisible rendering
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHEMISTRY_STATION, RenderType.translucent());

        // Flashbang
        FlashbangClientHandler.register();
        FlashbangPacketHandler.register();
        EntityRendererRegistry.register(ModEntities.FLASHBANG, ThrownItemRenderer::new);

        // Raven
        EntityRendererRegistry.register(ModEntities.RAVEN, RavenRenderer::new);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            FlashbangClientHandler.tickFlash();
            TemporaryLightManager.tick();
        });

        MenuScreens.register(
                ModMenuTypes.WRITTEN_RECIPE,
                WrittenRecipeScreen::new
        );

        // Map RecipeTooltipData -> RecipeTooltipComponent for shift-hover preview
        TooltipComponentCallback.EVENT.register(data -> {
            if (data instanceof RecipeTooltipData(org.mythic_goose.msgwoft.recipe.ChemistryStationRecipe recipe)) {
                return new RecipeTooltipComponent(recipe);
            }
            return null;
        });

        BlockEntityRenderers.register(
                ModBlockEntities.DIMENSIONAL_WARPGATE_ENTITY,
                DimensionalWarpgateBlockEntityRenderer::new
        );
        BlockEntityRenderers.register(
                ModBlockEntities.OVERWORLD_RETURN_ENTITY,
                OverworldReturnGateBlockEntityRenderer::new
        );

        ClientPlayNetworking.registerGlobalReceiver(SyncRecipesPacket.TYPE, (pkt, ctx) -> {
            ctx.client().execute(() -> {
                ChemistryStationRecipeManager.loadFromPacket(pkt.recipes());
            });
        });
    }
}