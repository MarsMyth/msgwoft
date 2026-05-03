package org.mythic_goose.msgwoft.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import org.mythic_goose.msgwoft.init.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public BlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {

        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.DRYING_RACK);

        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.CHEMISTRY_STATION)
                .add(ModBlocks.IDENTIFICATION_STATION);

        getOrCreateTagBuilder(BlockTags.INCORRECT_FOR_WOODEN_TOOL)
                .add(ModBlocks.DRYING_RACK)
                .add(ModBlocks.CHEMISTRY_STATION)
                .add(ModBlocks.IDENTIFICATION_STATION);
    }
}
