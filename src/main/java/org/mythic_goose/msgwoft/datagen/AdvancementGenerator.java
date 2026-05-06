package org.mythic_goose.msgwoft.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.mythic_goose.msgwoft.init.ModBlocks;
import org.mythic_goose.msgwoft.init.ModItems;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AdvancementGenerator extends FabricAdvancementProvider {
    public AdvancementGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer) {
        AdvancementHolder rootAdvancement = Advancement.Builder.advancement()
                .display(
                        ModItems.ROCK_SCANNER, // The display icon
                        Component.translatable("advancements.msgwoft.clear_skies.root.title"), // The title
                        Component.translatable("advancements.msgwoft.clear_skies.root.desc"), // The description
                        ResourceLocation.withDefaultNamespace("textures/gui/advancements/backgrounds/adventure.png"), // Background image used
                        AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                        true, // Show toast top right
                        true, // Announce to chat
                        false // Hidden in the advancement tab
                )
                // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                .addCriterion("got_dirt", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ROCK_SCANNER))
                .save(consumer, "msgwoft:clear_skies/root");

        AdvancementHolder researcherAdvancement = Advancement.Builder.advancement()
                .parent(rootAdvancement)
                .display(
                        ModItems.DATA_SPECIMEN, // The display icon
                        Component.translatable("advancements.msgwoft.clear_skies.researcher.title"), // The title
                        Component.translatable("advancements.msgwoft.clear_skies.researcher.desc"), // The description
                        null,
                        AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                        true, // Show toast top right
                        true, // Announce to chat
                        false // Hidden in the advancement tab
                )
                // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                .addCriterion("got_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.DATA_SPECIMEN))
                .save(consumer, "msgwoft:clear_skies/specimen");

        AdvancementHolder identificationTableAdvancement = Advancement.Builder.advancement()
                .parent(rootAdvancement)
                .display(
                        ModBlocks.IDENTIFICATION_STATION.asItem(), // The display icon
                        Component.translatable("advancements.msgwoft.clear_skies.identification_station.title"), // The title
                        Component.translatable("advancements.msgwoft.clear_skies.identification_station.desc"), // The description
                        null,
                        AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                        true, // Show toast top right
                        false, // Announce to chat
                        false // Hidden in the advancement tab
                )
                // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                .addCriterion("got_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.IDENTIFICATION_STATION.asItem()))
                .save(consumer, "msgwoft:clear_skies/identification_station");
        AdvancementHolder chemistryTableAdvancement = Advancement.Builder.advancement()
                .parent(rootAdvancement)
                .display(
                        ModBlocks.CHEMISTRY_STATION.asItem(), // The display icon
                        Component.translatable("advancements.msgwoft.clear_skies.chemistry_station.title"), // The title
                        Component.translatable("advancements.msgwoft.clear_skies.chemistry_station.desc"), // The description
                        null,
                        AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                        true, // Show toast top right
                        false, // Announce to chat
                        false // Hidden in the advancement tab
                )
                // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                .addCriterion("got_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.CHEMISTRY_STATION.asItem()))
                .save(consumer, "msgwoft:clear_skies/chemistry_station");

        AdvancementHolder identifiedAdvancement = Advancement.Builder.advancement()
                .parent(researcherAdvancement)
                .display(
                        ModItems.IDENTIFIED_DATA_SPECIMEN, // The display icon
                        Component.translatable("advancements.msgwoft.clear_skies.identify.title"), // The title
                        Component.translatable("advancements.msgwoft.clear_skies.identify.desc"), // The description
                        null,
                        AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                        true, // Show toast top right
                        true, // Announce to chat
                        false // Hidden in the advancement tab
                )
                // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                .addCriterion("got_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.IDENTIFIED_DATA_SPECIMEN))
                .save(consumer, "msgwoft:clear_skies/identify");

        AdvancementHolder recipeGainAdvancement = Advancement.Builder.advancement()
                .parent(researcherAdvancement)
                .display(
                        ModItems.WRITTEN_RECIPE, // The display icon
                        Component.translatable("advancements.msgwoft.clear_skies.recipe.title"), // The title
                        Component.translatable("advancements.msgwoft.clear_skies.recipe.desc"), // The description
                        null,
                        AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                        true, // Show toast top right
                        true, // Announce to chat
                        false // Hidden in the advancement tab
                )
                // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                .addCriterion("got_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.WRITTEN_RECIPE))
                .save(consumer, "msgwoft:clear_skies/recipe_gain");

        AdvancementHolder dryingAdvancement = Advancement.Builder.advancement()
                .parent(rootAdvancement)
                .display(
                        ModBlocks.DRYING_RACK.asItem(), // The display icon
                        Component.translatable("advancements.msgwoft.clear_skies.drying_rack.title"), // The title
                        Component.translatable("advancements.msgwoft.clear_skies.drying_rack.desc"), // The description
                        null,
                        AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                        true, // Show toast top right
                        true, // Announce to chat
                        false // Hidden in the advancement tab
                )
                // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                .addCriterion("got_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.DRYING_RACK.asItem()))
                .save(consumer, "msgwoft:clear_skies/drying_rack");

        AdvancementHolder dryingJerkyAdvancement = Advancement.Builder.advancement()
                .parent(dryingAdvancement)
                .display(
                        ModItems.MONSTER_JERKY, // The display icon
                        Component.translatable("advancements.msgwoft.clear_skies.zombie_jerky.title"), // The title
                        Component.translatable("advancements.msgwoft.clear_skies.zombie_jerky.desc"), // The description
                        null,
                        AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                        true, // Show toast top right
                        true, // Announce to chat
                        false // Hidden in the advancement tab
                )
                // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                .addCriterion("got_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MONSTER_JERKY))
                .save(consumer, "msgwoft:clear_skies/drying_jerky_monster");

        AdvancementHolder dryingBeefAdvancement = Advancement.Builder.advancement()
                .parent(dryingAdvancement)
                .display(
                        ModItems.BEEF_JERKY, // The display icon
                        Component.translatable("advancements.msgwoft.clear_skies.beef_jerky.title"), // The title
                        Component.translatable("advancements.msgwoft.clear_skies.beef_jerky.desc"), // The description
                        null,
                        AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                        true, // Show toast top right
                        true, // Announce to chat
                        false // Hidden in the advancement tab
                )
                // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                .addCriterion("got_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.BEEF_JERKY))
                .save(consumer, "msgwoft:clear_skies/drying_jerky_beef");

        AdvancementHolder dryingChickenAdvancement = Advancement.Builder.advancement()
                .parent(dryingAdvancement)
                .display(
                        ModItems.CHICKEN_JERKY, // The display icon
                        Component.translatable("advancements.msgwoft.clear_skies.chicken_jerky.title"), // The title
                        Component.translatable("advancements.msgwoft.clear_skies.chicken_jerky.desc"), // The description
                        null,
                        AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                        true, // Show toast top right
                        true, // Announce to chat
                        false // Hidden in the advancement tab
                )
                // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                .addCriterion("got_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CHICKEN_JERKY))
                .save(consumer, "msgwoft:clear_skies/drying_jerky_chicken");

        AdvancementHolder dryingClownfishAdvancement = Advancement.Builder.advancement()
                .parent(dryingAdvancement)
                .display(
                        ModItems.CLOWNFISH_JERKY, // The display icon
                        Component.translatable("advancements.msgwoft.clear_skies.clownfish_jerky.title"), // The title
                        Component.translatable("advancements.msgwoft.clear_skies.clownfish_jerky.desc"), // The description
                        null,
                        AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                        true, // Show toast top right
                        true, // Announce to chat
                        false // Hidden in the advancement tab
                )
                // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                .addCriterion("got_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CLOWNFISH_JERKY))
                .save(consumer, "msgwoft:clear_skies/drying_jerky_clownfish");

        AdvancementHolder dryingFishAdvancement = Advancement.Builder.advancement()
                .parent(dryingAdvancement)
                .display(
                        ModItems.FISH_JERKY, // The display icon
                        Component.translatable("advancements.msgwoft.clear_skies.fish_jerky.title"), // The title
                        Component.translatable("advancements.msgwoft.clear_skies.fish_jerky.desc"), // The description
                        null,
                        AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                        true, // Show toast top right
                        true, // Announce to chat
                        false // Hidden in the advancement tab
                )
                // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                .addCriterion("got_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.FISH_JERKY))
                .save(consumer, "msgwoft:clear_skies/drying_jerky_fish");

        AdvancementHolder dryingMuttonAdvancement = Advancement.Builder.advancement()
                .parent(dryingAdvancement)
                .display(
                        ModItems.MUTTON_JERKY, // The display icon
                        Component.translatable("advancements.msgwoft.clear_skies.mutton_jerky.title"), // The title
                        Component.translatable("advancements.msgwoft.clear_skies.mutton_jerky.desc"), // The description
                        null,
                        AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                        true, // Show toast top right
                        true, // Announce to chat
                        false // Hidden in the advancement tab
                )
                // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                .addCriterion("got_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MUTTON_JERKY))
                .save(consumer, "msgwoft:clear_skies/drying_jerky_mutton");

        AdvancementHolder dryingPorkAdvancement = Advancement.Builder.advancement()
                .parent(dryingAdvancement)
                .display(
                        ModItems.PORK_JERKY, // The display icon
                        Component.translatable("advancements.msgwoft.clear_skies.pork_jerky.title"), // The title
                        Component.translatable("advancements.msgwoft.clear_skies.pork_jerky.desc"), // The description
                        null,
                        AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                        true, // Show toast top right
                        true, // Announce to chat
                        false // Hidden in the advancement tab
                )
                // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                .addCriterion("got_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.PORK_JERKY))
                .save(consumer, "msgwoft:clear_skies/drying_jerky_pork");

        AdvancementHolder dryingPufferfishAdvancement = Advancement.Builder.advancement()
                .parent(dryingAdvancement)
                .display(
                        ModItems.PUFFERFISH_JERKY, // The display icon
                        Component.translatable("advancements.msgwoft.clear_skies.pufferfish_jerky.title"), // The title
                        Component.translatable("advancements.msgwoft.clear_skies.pufferfish_jerky.desc"), // The description
                        null,
                        AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                        true, // Show toast top right
                        true, // Announce to chat
                        false // Hidden in the advancement tab
                )
                // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                .addCriterion("got_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.PUFFERFISH_JERKY))
                .save(consumer, "msgwoft:clear_skies/drying_jerky_pufferfish");

        AdvancementHolder dryingRabbitAdvancement = Advancement.Builder.advancement()
                .parent(dryingAdvancement)
                .display(
                        ModItems.RABBIT_JERKY, // The display icon
                        Component.translatable("advancements.msgwoft.clear_skies.rabbit_jerky.title"), // The title
                        Component.translatable("advancements.msgwoft.clear_skies.rabbit_jerky.desc"), // The description
                        null,
                        AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                        true, // Show toast top right
                        true, // Announce to chat
                        false // Hidden in the advancement tab
                )
                // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                .addCriterion("got_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RABBIT_JERKY))
                .save(consumer, "msgwoft:clear_skies/drying_jerky_rabbit");

        AdvancementHolder dryingSalmonAdvancement = Advancement.Builder.advancement()
                .parent(dryingAdvancement)
                .display(
                        ModItems.SALMON_JERKY, // The display icon
                        Component.translatable("advancements.msgwoft.clear_skies.salmon_jerky.title"), // The title
                        Component.translatable("advancements.msgwoft.clear_skies.salmon_jerky.desc"), // The description
                        null,
                        AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                        true, // Show toast top right
                        true, // Announce to chat
                        false // Hidden in the advancement tab
                )
                // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                .addCriterion("got_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SALMON_JERKY))
                .save(consumer, "msgwoft:clear_skies/drying_jerky_salmon");

        AdvancementHolder dryingAllAdvancement = Advancement.Builder.advancement()
                .parent(dryingAdvancement)
                .display(
                        ModItems.CLOWNFISH_JERKY, // The display icon
                        Component.translatable("advancements.msgwoft.clear_skies.all_jerky.title"), // The title
                        Component.translatable("advancements.msgwoft.clear_skies.all_jerky.desc"), // The description
                        null,
                        AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                        true, // Show toast top right
                        true, // Announce to chat
                        false // Hidden in the advancement tab
                )
                // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                .addCriterion("got_item1", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.BEEF_JERKY))
                .addCriterion("got_item2", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CHICKEN_JERKY))
                .addCriterion("got_item3", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CLOWNFISH_JERKY))
                .addCriterion("got_item4", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.FISH_JERKY))
                .addCriterion("got_item5", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MONSTER_JERKY))
                .addCriterion("got_item6", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SALMON_JERKY))
                .addCriterion("got_item7", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MUTTON_JERKY))
                .addCriterion("got_item8", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.PORK_JERKY))
                .addCriterion("got_item9", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.PUFFERFISH_JERKY))
                .addCriterion("got_item10", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RABBIT_JERKY))
                .addCriterion("got_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SALMON_JERKY))
                .save(consumer, "msgwoft:clear_skies/drying_jerky_all");
    }
}
