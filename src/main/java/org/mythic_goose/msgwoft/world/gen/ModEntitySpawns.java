package org.mythic_goose.msgwoft.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import org.mythic_goose.msgwoft.init.ModEntities;

public class ModEntitySpawns {
    public static void addSpawns() {
        BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.IS_FOREST), MobCategory.CREATURE, ModEntities.RAVEN, 25, 2, 5);
        BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.IS_TAIGA), MobCategory.CREATURE, ModEntities.RAVEN, 35, 2, 5);

        SpawnPlacements.register(ModEntities.RAVEN, SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
    }
}
