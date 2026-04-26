package org.mythic_goose.msgwoft.item.materials;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.mythic_goose.msgwoft.MSGWOFT;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ModArmorMaterials {
    public static final Holder<ArmorMaterial> COPPER_ARMOR_MATERIAL = registerArmorMaterial("copper_armor",
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), typeIntegerEnumMap -> {
                typeIntegerEnumMap.put(ArmorItem.Type.HELMET, 2);
                typeIntegerEnumMap.put(ArmorItem.Type.CHESTPLATE, 4);
                typeIntegerEnumMap.put(ArmorItem.Type.LEGGINGS, 3);
                typeIntegerEnumMap.put(ArmorItem.Type.BOOTS, 1);
            }), 20, SoundEvents.ARMOR_EQUIP_IRON, () -> Ingredient.of(Items.COPPER_INGOT),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "copper_armor"))), 0,0));


    public static Holder<ArmorMaterial> registerArmorMaterial(String name, Supplier<ArmorMaterial> materialSupplier) {
        return Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL, ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, name), materialSupplier.get());
    }
}
