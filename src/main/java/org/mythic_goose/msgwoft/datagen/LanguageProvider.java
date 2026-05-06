package org.mythic_goose.msgwoft.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.mythic_goose.msgwoft.init.ModBlocks;
import org.mythic_goose.msgwoft.init.ModItems;
import org.mythic_goose.msgwoft.init.ModSounds;

import java.util.concurrent.CompletableFuture;

public class LanguageProvider extends FabricLanguageProvider {
    public LanguageProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder translationBuilder) {

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

        translationBuilder.add(ModItems.RAVEN_SPAWN_EGG, "Raven Spawn Egg");
        translationBuilder.add("entity.msgwoft.raven", "Raven");

        translationBuilder.add(ModItems.BEEF_JERKY, "Beef Jerky");
        translationBuilder.add(ModItems.CHICKEN_JERKY, "Chicken Jerky");
        translationBuilder.add(ModItems.CLOWNFISH_JERKY, "Clownfish Jerky");
        translationBuilder.add(ModItems.FISH_JERKY, "Cod Jerky");
        translationBuilder.add(ModItems.MONSTER_JERKY, "Monster Jerky");
        translationBuilder.add(ModItems.MUTTON_JERKY, "Mutton Jerky");
        translationBuilder.add(ModItems.PORK_JERKY, "Pork Jerky");
        translationBuilder.add(ModItems.PUFFERFISH_JERKY, "Pufferfish Jerky");
        translationBuilder.add(ModItems.RABBIT_JERKY, "Rabbit Jerky");
        translationBuilder.add(ModItems.SALMON_JERKY, "Salmon Jerky");
        translationBuilder.add(ModBlocks.DRYING_RACK, "Drying Rack");

        translationBuilder.add("tooltip.repair_kit","§7Right-click to repair all your items to full");
        // Advancements

        translationBuilder.add("advancements.msgwoft.clear_skies.root.title", "MSGWOFT: Clear Skies");
        translationBuilder.add("advancements.msgwoft.clear_skies.root.desc",
                "You never know this item might be important");

        translationBuilder.add("advancements.msgwoft.clear_skies.researcher.title", "Data Analysis");
        translationBuilder.add("advancements.msgwoft.clear_skies.researcher.desc",
                "Gained your first Data Sample");

        translationBuilder.add("advancements.msgwoft.clear_skies.identification_station.title", "Identification Station Acquired");
        translationBuilder.add("advancements.msgwoft.clear_skies.identification_station.desc",
                "Build an Identification Station");
        translationBuilder.add("advancements.msgwoft.clear_skies.chemistry_station.title", "Chemistry Station Acquired");
        translationBuilder.add("advancements.msgwoft.clear_skies.chemistry_station.desc",
                "Build a Chemistry Station");

        translationBuilder.add("advancements.msgwoft.clear_skies.identify.title", "Worth the Wait");
        translationBuilder.add("advancements.msgwoft.clear_skies.identify.desc",
                "Got your first Identified Sample");

        translationBuilder.add("advancements.msgwoft.clear_skies.recipe.title", "Deep Dive");
        translationBuilder.add("advancements.msgwoft.clear_skies.recipe.desc",
                "Found a Written Recipe Item");

        translationBuilder.add("advancements.msgwoft.clear_skies.drying_rack.title", "Dryable");
        translationBuilder.add("advancements.msgwoft.clear_skies.drying_rack.desc",
                "Some things need to dry");

        translationBuilder.add("advancements.msgwoft.clear_skies.beef_jerky.title", "Beef Jerky");
        translationBuilder.add("advancements.msgwoft.clear_skies.beef_jerky.desc",
                "Dry Raw Beef into Beef Jerky");

        translationBuilder.add("advancements.msgwoft.clear_skies.chicken_jerky.title", "Chicken Jerky");
        translationBuilder.add("advancements.msgwoft.clear_skies.chicken_jerky.desc",
                "Dry Raw Chicken into Chicken Jerky");

        translationBuilder.add("advancements.msgwoft.clear_skies.clownfish_jerky.title", "Clownfish Jerky");
        translationBuilder.add("advancements.msgwoft.clear_skies.clownfish_jerky.desc",
                "Dry Clownfish into Clownfish Jerky");

        translationBuilder.add("advancements.msgwoft.clear_skies.fish_jerky.title", "Cod Jerky");
        translationBuilder.add("advancements.msgwoft.clear_skies.fish_jerky.desc",
                "Dry Raw Cod into Cod Jerky");

        translationBuilder.add("advancements.msgwoft.clear_skies.zombie_jerky.title", "Monster Jerky");
        translationBuilder.add("advancements.msgwoft.clear_skies.zombie_jerky.desc",
                "Dry Rotten Flesh into Monster Jerky");

        translationBuilder.add("advancements.msgwoft.clear_skies.mutton_jerky.title", "Mutton Jerky");
        translationBuilder.add("advancements.msgwoft.clear_skies.mutton_jerky.desc",
                "Dry Raw Mutton into Mutton Jerky");

        translationBuilder.add("advancements.msgwoft.clear_skies.pork_jerky.title", "Pork Jerky");
        translationBuilder.add("advancements.msgwoft.clear_skies.pork_jerky.desc",
                "Dry Raw Pork into Pork Jerky");

        translationBuilder.add("advancements.msgwoft.clear_skies.pufferfish_jerky.title", "Pufferfish Jerky");
        translationBuilder.add("advancements.msgwoft.clear_skies.pufferfish_jerky.desc",
                "Dry Pufferfish into Pufferfish Jerky");

        translationBuilder.add("advancements.msgwoft.clear_skies.rabbit_jerky.title", "Rabbit Jerky");
        translationBuilder.add("advancements.msgwoft.clear_skies.rabbit_jerky.desc",
                "Dry Raw Rabbit into Rabbit Jerky");

        translationBuilder.add("advancements.msgwoft.clear_skies.salmon_jerky.title", "Salmon Jerky");
        translationBuilder.add("advancements.msgwoft.clear_skies.salmon_jerky.desc",
                "Dry Raw Salmon into Salmon Jerky");

        translationBuilder.add("advancements.msgwoft.clear_skies.all_jerky.title", "Got All The Jerky");
        translationBuilder.add("advancements.msgwoft.clear_skies.all_jerky.desc",
                "Get every jerky (They give the same food value)");
    }
}
