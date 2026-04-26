package org.mythic_goose.msgwoft.init;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.mythic_goose.msgwoft.MSGWOFT;
import org.mythic_goose.msgwoft.creativetab.ModSections;
import org.mythic_goose.msgwoft.creativetab.Section;
import org.mythic_goose.msgwoft.creativetab.TabLayout;

import java.util.List;

public class ModCreativeTabs {
    public static CreativeModeTab CORE;

    public static void init() {
        List<Section> sections = ModSections.build();
        TabLayout.build(sections); // populates CACHED_ITEMS and SECTION_ROW

        CORE = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "msgwoft"),
                FabricItemGroup.builder()
                        .icon(() -> new ItemStack(ModItems.ROCK_SCANNER))
                        .title(Component.translatable("itemGroup.msgwoft"))
                        .displayItems((params, output) -> {
                            // Intentionally empty — CreativeModeTabMixin overrides buildContents
                        })
                        .build());
    }
}