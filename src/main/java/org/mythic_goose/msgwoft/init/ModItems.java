package org.mythic_goose.msgwoft.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import org.mythic_goose.msgwoft.MSGWOFT;
import org.mythic_goose.msgwoft.item.*;
import org.mythic_goose.msgwoft.item.beta.StabilizationDeviceItem;
import org.mythic_goose.msgwoft.item.SynchronisedOrbItem;
import org.mythic_goose.msgwoft.item.materials.ModArmorMaterials;
import org.mythic_goose.msgwoft.item.materials.ModTiers;

public class ModItems {
    public static Item LUCK_MODULE;
    public static Item EFFICIENCY_MODULE;
    
    public static Item SYNCHRONISED_ORB;
    public static Item STABILIZATION_DEVICE;

    public static Item VOID_SHARD;

    public static Item ROCK_SCANNER;
    public static Item DATA_SPECIMEN;
    public static Item IDENTIFIED_DATA_SPECIMEN;

    public static Item WRITTEN_RECIPE;

    public static Item FLASHBANG;

    public static Item CIRCUIT_BOARD;

    public static Item RAVEN_SPAWN_EGG;
    public static Item MONSTER_JERKY;

    public static Item COPPER_HELMET;
    public static Item COPPER_CHESTPLATE;
    public static Item COPPER_LEGGINGS;
    public static Item COPPER_BOOTS;
    public static Item COPPER_NUGGET;
    public static Item COPPER_SWORD;
    public static Item COPPER_AXE;
    public static Item COPPER_PICKAXE;
    public static Item COPPER_SHOVEL;
    public static Item COPPER_HOE;

    public static void init() {
        LUCK_MODULE = createItem("luck_upgrade", new ModuleItem(new Item.Properties().stacksTo(10), "Luck"));
        EFFICIENCY_MODULE = createItem("efficiency_upgrade", new ModuleItem(new Item.Properties().stacksTo(10), "Efficiency"));

        SYNCHRONISED_ORB = createItem("synchronised_orb", new SynchronisedOrbItem(new Item.Properties()));
        STABILIZATION_DEVICE = createItem("stabilization_device", new StabilizationDeviceItem(new Item.Properties().stacksTo(1)));

        VOID_SHARD = createItem("ionised_void_shard", new IonisedVoidShardItem(new Item.Properties()));

        ROCK_SCANNER = createItem("rock_scanner", new RockScannerItem(new Item.Properties().stacksTo(1)));
        DATA_SPECIMEN = createItem("data_specimen", new DataSpecimenItem(new Item.Properties().stacksTo(1)));
        IDENTIFIED_DATA_SPECIMEN = createItem("identified_data_specimen", new IdentifiedDataSpecimenItem(new Item.Properties().stacksTo(1)));

        FLASHBANG = createItem("flashbang", new FlashbangItem(new Item.Properties().stacksTo(16)));

        WRITTEN_RECIPE = createItem("written_recipe", new WrittenRecipeItem(new Item.Properties().stacksTo(1)));

        CIRCUIT_BOARD = createItem("circuit_board", new Item(new Item.Properties()));

        RAVEN_SPAWN_EGG = createItem("raven_spawn_egg", new ColourlessSpawnEggItem(ModEntities.RAVEN, new Item.Properties()));

        MONSTER_JERKY = createItem("monster_jerky", new Item(new Item.Properties().food(Foods.COOKED_BEEF)));
        // Copper Age Backport

        COPPER_HELMET = createItem("copper_helmet", new ArmorItem(ModArmorMaterials.COPPER_ARMOR_MATERIAL, ArmorItem.Type.HELMET,
                new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(11))));
        COPPER_CHESTPLATE = createItem("copper_chestplate", new ArmorItem(ModArmorMaterials.COPPER_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(11))));
        COPPER_LEGGINGS = createItem("copper_leggings", new ArmorItem(ModArmorMaterials.COPPER_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(11))));
        COPPER_BOOTS = createItem("copper_boots", new ArmorItem(ModArmorMaterials.COPPER_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(11))));
        COPPER_NUGGET = createItem("copper_nugget", new Item(new Item.Properties()));

        COPPER_SWORD = createItem("copper_sword", new SwordItem(ModTiers.COPPER,
                new Item.Properties().attributes(SwordItem.createAttributes(ModTiers.COPPER, 3, -2.4F))));
        COPPER_PICKAXE = createItem("copper_pickaxe", new PickaxeItem(ModTiers.COPPER,
                new Item.Properties().attributes(PickaxeItem.createAttributes(ModTiers.COPPER, 1.0F, -2.8F))));
        COPPER_AXE = createItem("copper_axe", new AxeItem(ModTiers.COPPER,
                new Item.Properties().attributes(AxeItem.createAttributes(ModTiers.COPPER, 7.0F, -3.2F))));
        COPPER_SHOVEL = createItem("copper_shovel", new ShovelItem(ModTiers.COPPER,
                new Item.Properties().attributes(ShovelItem.createAttributes(ModTiers.COPPER, 1.5F, -3.0F))));
        COPPER_HOE = createItem("copper_hoe", new HoeItem(ModTiers.COPPER,
                new Item.Properties().attributes(HoeItem.createAttributes(ModTiers.COPPER, -1.0F, -2.0F))));
    }

    private static Item createItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, name), item);
    }
}
