package org.mythic_goose.msgwoft.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import org.mythic_goose.msgwoft.init.ModItems;

public class ModLootTableModifiers {

    private static final ResourceKey<LootTable>[] CHEST_LOOT_TABLES = new ResourceKey[]{
            keyOf("chests/abandoned_mineshaft"),
            keyOf("chests/ancient_city"),
            keyOf("chests/ancient_city_ice_box"),
            keyOf("chests/bastion_bridge"),
            keyOf("chests/bastion_hoglin_stable"),
            keyOf("chests/bastion_other"),
            keyOf("chests/bastion_treasure"),
            keyOf("chests/buried_treasure"),
            keyOf("chests/desert_pyramid"),
            keyOf("chests/end_city_treasure"),
            keyOf("chests/igloo_chest"),
            keyOf("chests/jungle_temple"),
            keyOf("chests/jungle_temple_dispenser"),
            keyOf("chests/nether_bridge"),
            keyOf("chests/pillager_outpost"),
            keyOf("chests/ruined_portal"),
            keyOf("chests/shipwreck_map"),
            keyOf("chests/shipwreck_supply"),
            keyOf("chests/shipwreck_treasure"),
            keyOf("chests/simple_dungeon"),
            keyOf("chests/spawn_bonus_chest"),
            keyOf("chests/stronghold_corridor"),
            keyOf("chests/stronghold_crossing"),
            keyOf("chests/stronghold_library"),
            keyOf("chests/underwater_ruin_big"),
            keyOf("chests/underwater_ruin_small"),
            keyOf("chests/village/village_armorer"),
            keyOf("chests/village/village_butcher"),
            keyOf("chests/village/village_cartographer"),
            keyOf("chests/village/village_desert_house"),
            keyOf("chests/village/village_fisher"),
            keyOf("chests/village/village_fletcher"),
            keyOf("chests/village/village_mason"),
            keyOf("chests/village/village_plains_house"),
            keyOf("chests/village/village_savanna_house"),
            keyOf("chests/village/village_shepherd"),
            keyOf("chests/village/village_snowy_house"),
            keyOf("chests/village/village_taiga_house"),
            keyOf("chests/village/village_tannery"),
            keyOf("chests/village/village_temple"),
            keyOf("chests/village/village_toolsmith"),
            keyOf("chests/village/village_weaponsmith"),
            keyOf("chests/woodland_mansion"),
    };

    private static ResourceKey<LootTable> keyOf(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.withDefaultNamespace(path));
    }

    public static void initialize() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            for (ResourceKey<LootTable> chestKey : CHEST_LOOT_TABLES) {
                if (chestKey.equals(key)) {
                    tableBuilder.withPool(
                            LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1))
                                    .when(LootItemRandomChanceCondition.randomChance(0.05f))
                                    .add(LootItem.lootTableItem(ModItems.WRITTEN_RECIPE))
                    );
                }
            }
        });
    }
}