package org.mythic_goose.msgwoft.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import org.mythic_goose.msgwoft.init.ModItems;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItems.COPPER_HELMET)
                .add(ModItems.COPPER_CHESTPLATE)
                .add(ModItems.COPPER_LEGGINGS)
                .add(ModItems.COPPER_BOOTS)
        ;

        getOrCreateTagBuilder(ItemTags.MEAT)
                .add(ModItems.BEEF_JERKY)
                .add(ModItems.CHICKEN_JERKY)
                .add(ModItems.CLOWNFISH_JERKY)
                .add(ModItems.FISH_JERKY)
                .add(ModItems.MONSTER_JERKY)
                .add(ModItems.MUTTON_JERKY)
                .add(ModItems.PORK_JERKY)
                .add(ModItems.PUFFERFISH_JERKY)
                .add(ModItems.RABBIT_JERKY)
                .add(ModItems.SALMON_JERKY)
        ;
    }
}
