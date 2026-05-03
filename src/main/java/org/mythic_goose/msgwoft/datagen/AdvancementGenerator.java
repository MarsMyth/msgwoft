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
                        AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
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
                        Component.translatable("advancements.msgwoft.clear_skies.drying_jerky.title"), // The title
                        Component.translatable("advancements.msgwoft.clear_skies.drying_jerky.desc"), // The description
                        null,
                        AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                        true, // Show toast top right
                        true, // Announce to chat
                        false // Hidden in the advancement tab
                )
                // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                .addCriterion("got_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MONSTER_JERKY))
                .save(consumer, "msgwoft:clear_skies/drying_jerky");
    }
}
