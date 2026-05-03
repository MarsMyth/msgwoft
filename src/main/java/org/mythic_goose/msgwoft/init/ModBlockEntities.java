package org.mythic_goose.msgwoft.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.mythic_goose.msgwoft.MSGWOFT;
import org.mythic_goose.msgwoft.block.entity.*;

public class ModBlockEntities {

    public static final BlockEntityType<IdentificationStationBlockEntity> IDENTIFICATION_STATION_ENTITY =
            Registry.register(
                    BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "identification_station"),
                    BlockEntityType.Builder.of(
                            IdentificationStationBlockEntity::new,
                            ModBlocks.IDENTIFICATION_STATION
                    ).build()
            );

    public static final BlockEntityType<ChemistryStationBlockEntity> CHEMISTRY_STATION_ENTITY =
            Registry.register(
                    BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "chemistry_station"),
                    BlockEntityType.Builder.of(
                            ChemistryStationBlockEntity::new,
                            ModBlocks.CHEMISTRY_STATION
                    ).build()
            );

    public static final BlockEntityType<DimensionalWarpgateBlockEntity> DIMENSIONAL_WARPGATE_ENTITY =
            Registry.register(
                    BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "dimensional_warpgate"),
                    BlockEntityType.Builder.of(
                            DimensionalWarpgateBlockEntity::new,
                            ModBlocks.DIMENSIONAL_WARPGATE
                    ).build()
            );

    public static final BlockEntityType<OverworldReturnGateBlockEntity> OVERWORLD_RETURN_ENTITY =
            Registry.register(
                    BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "overworld_return_gate"),
                    BlockEntityType.Builder.of(
                            OverworldReturnGateBlockEntity::new,
                            ModBlocks.OVERWORLD_RETURN_GATE
                    ).build()
            );

    public static final BlockEntityType<DryingRackBlockEntity> DRYING_RACK =
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "drying_rack"),
                    BlockEntityType.Builder.of(
                            DryingRackBlockEntity::new,
                            ModBlocks.DRYING_RACK
                    ).build());

    public static void register() {
        MSGWOFT.LOGGER.info("Registering Block Entities for " + MSGWOFT.MOD_ID);
    }
}