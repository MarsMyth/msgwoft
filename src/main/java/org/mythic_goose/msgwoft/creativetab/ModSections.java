package org.mythic_goose.msgwoft.creativetab;

import net.minecraft.network.chat.Component;
import org.mythic_goose.msgwoft.init.ModBlocks;
import org.mythic_goose.msgwoft.init.ModItems;

import java.util.List;

public class ModSections {

    public static List<Section> ALL = List.of();

    public static List<Section> build() {
        ALL = List.of(
                new SectionColored(
                        "core",
                        Component.translatable("itemGroup.msgwoft.core"),
                        0xFF1a1a2e,   // ARGB banner background
                        0xFFFFFFFF,
                        List.of(
                                ModItems.ROCK_SCANNER,
                                ModItems.WRITTEN_RECIPE,
                                ModBlocks.IDENTIFICATION_STATION.asItem(),
                                ModBlocks.CHEMISTRY_STATION.asItem(),
                                ModItems.LUCK_MODULE,
                                ModItems.EFFICIENCY_MODULE,
                                ModItems.VOID_SHARD,
                                ModItems.FLASHBANG,
                                ModItems.CIRCUIT_BOARD,
                                ModItems.RAVEN_SPAWN_EGG,
                                ModBlocks.DRYING_RACK.asItem(),
                                ModItems.MONSTER_JERKY
                        )
                ),
                new SectionColored(
                        "modules",
                        Component.translatable("itemGroup.msgwoft.dimensional_vortex"),
                        0xFF1a2e1a,
                        0xFFFFFFFF,
                        List.of(
                                ModItems.SYNCHRONISED_ORB
                        )
                ),
                new SectionColored(
                        "copperbackport",
                        Component.translatable("itemGroup.msgwoft.copperbackport"),
                        0xFFcc6600,
                        0xFFFFFFFF,
                        List.of(
                                ModItems.COPPER_HELMET,
                                ModItems.COPPER_CHESTPLATE,
                                ModItems.COPPER_LEGGINGS,
                                ModItems.COPPER_BOOTS,

                                ModItems.COPPER_NUGGET,

                                ModItems.COPPER_SWORD,
                                ModItems.COPPER_AXE,
                                ModItems.COPPER_PICKAXE,
                                ModItems.COPPER_SHOVEL,
                                ModItems.COPPER_HOE
                        )
                ),
                SectionTextured.of(
                        "msgwoft",
                        "beta_tools",
                        Component.literal("Beta Stage - Unfinished"),
                        0xFFFFFFFF,
                        List.of(
                                ModItems.STABILIZATION_DEVICE,
                                ModBlocks.DIMENSIONAL_WARPGATE.asItem(),
                                ModBlocks.OVERWORLD_RETURN_GATE.asItem()
                        )
                )
//              Example of a textured section
//                SectionTextured.of(
//                        MSGWOFT.MOD_ID, "name",
//                        Component.translatable("itemgroup.msgwoft.name"),
//                        0xFFFFAAAA,
//                        List.of(
//
//                        )
//                )
        );
        return ALL;
    }
}