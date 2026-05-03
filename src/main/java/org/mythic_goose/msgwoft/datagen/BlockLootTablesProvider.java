package org.mythic_goose.msgwoft.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import org.mythic_goose.msgwoft.init.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class BlockLootTablesProvider extends FabricBlockLootTableProvider {
    public BlockLootTablesProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {

        dropSelf(ModBlocks.DRYING_RACK);
        dropSelf(ModBlocks.CHEMISTRY_STATION);
        dropSelf(ModBlocks.IDENTIFICATION_STATION);
    }
}
