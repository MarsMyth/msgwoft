package org.mythic_goose.msgwoft.datagen;

import io.netty.util.internal.shaded.org.jctools.queues.MpscArrayQueue;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import org.mythic_goose.msgwoft.init.ModBlocks;
import org.mythic_goose.msgwoft.init.ModItems;

import java.util.concurrent.CompletableFuture;

public class LanguageProvider extends FabricLanguageProvider {
    public LanguageProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder translationBuilder) {

        translationBuilder.add(ModItems.SYNCHRONISED_ORB, "Synchronised Orb");
        translationBuilder.add(ModItems.VOID_SHARD, "Ionised Void Shard");

        translationBuilder.add(ModItems.ROCK_SCANNER, "Rock Scanner");
        translationBuilder.add(ModItems.DATA_SPECIMEN, "Data Specimen");
        translationBuilder.add(ModItems.IDENTIFIED_DATA_SPECIMEN, "Data Specimen");

        translationBuilder.add(ModItems.LUCK_MODULE, "Luck Module");
        translationBuilder.add(ModItems.EFFICIENCY_MODULE, "Efficiency Module");
        translationBuilder.add(ModItems.FLASHBANG, "Flashbang");
        translationBuilder.add(ModItems.WRITTEN_RECIPE, "Written Recipe");

        translationBuilder.add(ModBlocks.IDENTIFICATION_STATION, "Identification Station");
        translationBuilder.add(ModBlocks.CHEMISTRY_STATION, "Chemistry Station");
        translationBuilder.add("container.msgwoft.identification_station", "Identification Station");

        translationBuilder.add("itemGroup.msgwoft", "MSGWOFT");
        translationBuilder.add("itemGroup.msgwoft.core", "Clear Skies");
        translationBuilder.add("itemGroup.msgwoft.dimensional_vortex", "Dimensional Vortex");
        translationBuilder.add("itemGroup.msgwoft.copperbackport", "Copper Age Backport");

        translationBuilder.add("container.msgwoft.chemistry_station", "Chemistry Station");

        translationBuilder.add("tooltip.module.when_in_identity", "§9When in Identification Station");
        translationBuilder.add("tooltip.module.when_in_chemistry", "§9When in Chemistry Station");

        translationBuilder.add("tooltip.preventing_durability", "§6This shard is preventing durability drain");

        translationBuilder.add("tooltip.efficiency_module.identify", "§6Decreases wait time by 10% (from 5 mins)");
        translationBuilder.add("tooltip.luck_module.identify", "§6+10% Chance to Boost Stats");
        translationBuilder.add("tooltip.luck_module.chemistry", "§6+5% Probability");

        translationBuilder.add(ModItems.COPPER_HELMET, "Copper Helmet");
        translationBuilder.add(ModItems.COPPER_CHESTPLATE, "Copper Chestplate");
        translationBuilder.add(ModItems.COPPER_LEGGINGS, "Copper Leggings");
        translationBuilder.add(ModItems.COPPER_BOOTS, "Copper Boots");

        translationBuilder.add(ModItems.COPPER_NUGGET, "Copper Nugget");

        translationBuilder.add(ModItems.COPPER_SWORD, "Copper Sword");
        translationBuilder.add(ModItems.COPPER_PICKAXE, "Copper Pickaxe");
        translationBuilder.add(ModItems.COPPER_AXE, "Copper Axe");
        translationBuilder.add(ModItems.COPPER_SHOVEL, "Copper Shovel");
        translationBuilder.add(ModItems.COPPER_HOE, "Copper Hoe");

        translationBuilder.add("tooltip.beta_item", "§d§o[Beta Item]");
        translationBuilder.add("tooltip.no_recipe", "§d§o[Missing Recipe]");

        translationBuilder.add(ModItems.STABILIZATION_DEVICE, "Stabilization Device");
        translationBuilder.add(ModItems.CIRCUIT_BOARD, "Circuit Board");

        translationBuilder.add(ModBlocks.DIMENSIONAL_WARPGATE, "Dimensional Rift");
        translationBuilder.add(ModBlocks.OVERWORLD_RETURN_GATE, "Dimensional Rift - (Return Gate)");

        // Advancements

        translationBuilder.add("advancements.msgwoft.clear_skies.root.title", "MSGWOFT: Clear Skies");
        translationBuilder.add("advancements.msgwoft.clear_skies.root.desc",
                "You never know this item might be important. Check some ores out");

        translationBuilder.add("advancements.msgwoft.clear_skies.researcher.title", "Data Analysis");
        translationBuilder.add("advancements.msgwoft.clear_skies.researcher.desc",
                "Gained your first Data Sample - Now Identify it");
    }
}
