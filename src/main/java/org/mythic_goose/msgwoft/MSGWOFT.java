package org.mythic_goose.msgwoft;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.server.MinecraftServer;
import org.mythic_goose.msgwoft.client.render.ModShaders;
import org.mythic_goose.msgwoft.entity.RavenEntity;
import org.mythic_goose.msgwoft.init.*;
import org.mythic_goose.msgwoft.network.ChemistryCraftPacket;
import org.mythic_goose.msgwoft.network.FlashbangPacket;
import org.mythic_goose.msgwoft.recipe.ChemistryStationRecipeManager;
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
        ModShaders.register();
        ModMobEffects.registerEffects();

        ServerLifecycleEvents.SERVER_STARTED.register(ChemistryStationRecipeManager::loadRecipes);

        FabricDefaultAttributeRegistry.register(
                ModEntities.RAVEN,
                RavenEntity.createRavenAttributes()
        );
	}

}