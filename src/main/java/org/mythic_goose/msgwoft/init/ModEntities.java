package org.mythic_goose.msgwoft.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.mythic_goose.msgwoft.MSGWOFT;
import org.mythic_goose.msgwoft.entity.FlashbangEntity;

public class ModEntities {

    public static final EntityType<FlashbangEntity> FLASHBANG = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "flashbang"),
            EntityType.Builder.<FlashbangEntity>of(FlashbangEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(10)
                    .updateInterval(10)
                    .build("flashbang")
    );

    public static void register() {
        // Calling this class loads the static fields, triggering registration
    }
}