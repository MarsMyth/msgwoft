package org.mythic_goose.msgwoft.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.world.item.ArmorItem;
import org.mythic_goose.msgwoft.init.ModBlocks;
import org.mythic_goose.msgwoft.init.ModItems;

public class ModelProvider extends FabricModelProvider {
    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        blockModelGenerators.createGenericCube(ModBlocks.IDENTIFICATION_STATION);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {

        itemModelGenerators.generateFlatItem(ModItems.SYNCHRONISED_ORB, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.VOID_SHARD, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.ROCK_SCANNER, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.DATA_SPECIMEN, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.LUCK_MODULE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.EFFICIENCY_MODULE, ModelTemplates.FLAT_ITEM);

        itemModelGenerators.generateFlatItem(ModItems.WRITTEN_RECIPE, ModelTemplates.FLAT_ITEM);

        itemModelGenerators.generateArmorTrims(((ArmorItem) ModItems.COPPER_HELMET));
        itemModelGenerators.generateArmorTrims(((ArmorItem) ModItems.COPPER_CHESTPLATE));
        itemModelGenerators.generateArmorTrims(((ArmorItem) ModItems.COPPER_LEGGINGS));
        itemModelGenerators.generateArmorTrims(((ArmorItem) ModItems.COPPER_BOOTS));

        itemModelGenerators.generateFlatItem(ModItems.COPPER_NUGGET, ModelTemplates.FLAT_ITEM);

        itemModelGenerators.generateFlatItem(ModItems.COPPER_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.COPPER_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.COPPER_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.COPPER_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.COPPER_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);

        itemModelGenerators.generateFlatItem(ModItems.STABILIZATION_DEVICE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.CIRCUIT_BOARD, ModelTemplates.FLAT_ITEM);
    }
}
