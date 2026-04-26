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
                        ModItems.ROCK_SCANNER, // The display icon
                        Component.translatable("advancements.msgwoft.clear_skies.researcher.title"), // The title
                        Component.translatable("advancements.msgwoft.clear_skies.researcher.desc"), // The description
                        null,
                        AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                        true, // Show toast top right
                        true, // Announce to chat
                        false // Hidden in the advancement tab
                )
                // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                .addCriterion("got_dirt", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ROCK_SCANNER))
                .save(consumer, "msgwoft:clear_skies/root");

    }
}
