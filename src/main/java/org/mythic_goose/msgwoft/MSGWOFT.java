package org.mythic_goose.msgwoft;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import org.mythic_goose.msgwoft.client.render.ModShaders;
import org.mythic_goose.msgwoft.entity.RavenEntity;
import org.mythic_goose.msgwoft.init.*;
import org.mythic_goose.msgwoft.network.ChemistryCraftPacket;
import org.mythic_goose.msgwoft.network.FlashbangPacket;
import org.mythic_goose.msgwoft.network.SyncRecipesPacket;
import org.mythic_goose.msgwoft.recipe.ChemistryStationRecipeManager;
import org.mythic_goose.msgwoft.util.ModLootTableModifiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MSGWOFT implements ModInitializer {
	public static final String MOD_ID = "msgwoft";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Maybe Something Good Will Occur From This");

        FlashbangPacket.register();
        ChemistryCraftPacket.registerServer();

        ModItems.init();
        ModBlocks.init();
        ModCreativeTabs.init();
        ModSounds.register();
        ModEntities.register();
        ModBlockEntities.register();
        ModMenuTypes.init();
        ModMobEffects.registerEffects();

        ServerLifecycleEvents.SERVER_STARTED.register(ChemistryStationRecipeManager::loadRecipes);

        FabricDefaultAttributeRegistry.register(
                ModEntities.RAVEN,
                RavenEntity.createRavenAttributes()
        );

        PayloadTypeRegistry.playS2C().register(SyncRecipesPacket.TYPE, SyncRecipesPacket.CODEC);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayNetworking.send(
                    handler.player,
                    new SyncRecipesPacket(ChemistryStationRecipeManager.getAllRecipes())
            );
        });

        ModLootTableModifiers.initialize();

        BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.IS_FOREST), MobCategory.CREATURE, ModEntities.RAVEN, 8, 2, 5);
	}

}