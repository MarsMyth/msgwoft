package org.mythic_goose.msgwoft.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.mythic_goose.msgwoft.MSGWOFT;
import org.mythic_goose.msgwoft.block.*;

public class ModBlocks {

    public static Block IDENTIFICATION_STATION = createBlock("identification_station",
            new IdentificationStationBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_TUFF)));

    public static Block CHEMISTRY_STATION = createBlock("chemistry_station",
            new ChemistryStationBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_TUFF).noOcclusion()));

    public static Block DRYING_RACK = createBlock("drying_rack", new DryingRackBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()));

    private static Block createBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, name),
                new BlockItem(block, new Item.Properties()));
    }

    private static Block createBlockWithoutBlockItem(String name, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, name), block);
    }

    public static void init() {
        MSGWOFT.LOGGER.info("Maybe Something Good will occur from creating blocks");
    }
}
