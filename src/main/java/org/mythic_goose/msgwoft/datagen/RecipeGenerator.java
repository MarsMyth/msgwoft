package org.mythic_goose.msgwoft.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.mythic_goose.msgwoft.init.ModBlocks;
import org.mythic_goose.msgwoft.init.ModItems;

import java.util.concurrent.CompletableFuture;

public class RecipeGenerator extends FabricRecipeProvider {
    public RecipeGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput recipeOutput) {

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.IDENTIFICATION_STATION)
                .pattern("xxx")
                .pattern("wdw")
                .pattern("www")
                .define('x', Blocks.POLISHED_TUFF.asItem())
                .define('d', Items.DIAMOND)
                .define('w', Blocks.OAK_PLANKS)
                .unlockedBy("get_diamonds", has(Items.DIAMOND))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHEMISTRY_STATION)
                .pattern("ddd")
                .pattern("www")
                .define('w', Blocks.POLISHED_TUFF.asItem())
                .define('d', Items.EMERALD)
                .unlockedBy("get_emeralds", has(Items.EMERALD))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.ROCK_SCANNER)
                .pattern(" g ")
                .pattern("gag")
                .pattern("sg ")
                .define('s', Items.STICK)
                .define('a', Items.AMETHYST_SHARD)
                .define('g', Items.GOLD_INGOT)
                .unlockedBy("get_amethyst", has(Items.AMETHYST_SHARD))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModBlocks.DRYING_RACK)
                .pattern("mmm")
                .define('m', Blocks.OAK_SLAB.asItem())
                .unlockedBy("get_material", has(Blocks.OAK_SLAB.asItem()))
                .save(recipeOutput);

        generateToolRecipes(ModItems.COPPER_SWORD, ModItems.COPPER_AXE, ModItems.COPPER_PICKAXE, ModItems.COPPER_SHOVEL, ModItems.COPPER_HOE,
                1, Items.COPPER_INGOT, recipeOutput);

        generateArmorRecipes(ModItems.COPPER_HELMET,ModItems.COPPER_CHESTPLATE,ModItems.COPPER_LEGGINGS,ModItems.COPPER_BOOTS,
                1, Items.COPPER_INGOT, recipeOutput);

        generateIngotToNugget(Items.COPPER_INGOT, ModItems.COPPER_NUGGET, recipeOutput);
    }

    private static void generateToolRecipes(Item swordOutput, Item axeOutput, Item pickaxeOutput, Item shovelOutput, Item hoeOutput, int outputCount, Item recipeInput, RecipeOutput recipeOutput){
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, pickaxeOutput, outputCount)
                .pattern("mmm")
                .pattern(" s ")
                .pattern(" s ")
                .define('s', Items.STICK)
                .define('m', recipeInput)
                .unlockedBy("get_material", has(recipeInput))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, axeOutput, outputCount)
                .pattern(" mm")
                .pattern(" sm")
                .pattern(" s ")
                .define('s', Items.STICK)
                .define('m', recipeInput)
                .unlockedBy("get_material", has(recipeInput))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, swordOutput, outputCount)
                .pattern(" m ")
                .pattern(" m ")
                .pattern(" s ")
                .define('s', Items.STICK)
                .define('m', recipeInput)
                .unlockedBy("get_material", has(recipeInput))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, shovelOutput, outputCount)
                .pattern(" m ")
                .pattern(" s ")
                .pattern(" s ")
                .define('s', Items.STICK)
                .define('m', recipeInput)
                .unlockedBy("get_material", has(recipeInput))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, hoeOutput, outputCount)
                .pattern(" mm")
                .pattern(" s ")
                .pattern(" s ")
                .define('s', Items.STICK)
                .define('m', recipeInput)
                .unlockedBy("get_material", has(recipeInput))
                .save(recipeOutput);
    }
    private static void generateArmorRecipes(Item helmetOutput, Item chestplateOutput, Item leggingsOutput, Item bootsOutput, int outputCount, Item recipeInput, RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, helmetOutput, outputCount)
                .pattern("mmm")
                .pattern("m m")
                .pattern("   ")
                .define('m', recipeInput)
                .unlockedBy("get_material", has(recipeInput))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, chestplateOutput, outputCount)
                .pattern("m m")
                .pattern("mmm")
                .pattern("mmm")
                .define('m', recipeInput)
                .unlockedBy("get_material", has(recipeInput))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, leggingsOutput, outputCount)
                .pattern("mmm")
                .pattern("m m")
                .pattern("m m")
                .define('m', recipeInput)
                .unlockedBy("get_material", has(recipeInput))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, bootsOutput, outputCount)
                .pattern("m m")
                .pattern("m m")
                .define('m', recipeInput)
                .unlockedBy("get_material", has(recipeInput))
                .save(recipeOutput);
    }
    private static void generateIngotToNugget(Item ingot, Item nugget, RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ingot)
                .pattern("sss")
                .pattern("sss")
                .pattern("sss")
                .define('s', nugget)
                .unlockedBy("need_nugget", has(nugget))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, nugget, 9)
                .pattern("s")
                .define('s', ingot)
                .unlockedBy("need_ingot", has(ingot))
                .save(recipeOutput);
    }
}
