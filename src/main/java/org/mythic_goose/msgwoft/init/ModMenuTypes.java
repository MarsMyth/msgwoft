package org.mythic_goose.msgwoft.init;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import org.mythic_goose.msgwoft.MSGWOFT;
import org.mythic_goose.msgwoft.screen.block.ChemistryStationMenu;
import org.mythic_goose.msgwoft.screen.block.IdentificationStationMenu;
import org.mythic_goose.msgwoft.screen.item.WrittenRecipeMenu;

public class ModMenuTypes {

    public static final MenuType<IdentificationStationMenu> IDENTIFICATION_STATION =
            Registry.register(
                    BuiltInRegistries.MENU,
                    ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "identification_station"),
                    new ExtendedScreenHandlerType<>(
                            IdentificationStationMenu::new,
                            StreamCodec.of(
                                    (buf, pos) -> buf.writeBlockPos(pos),
                                    (buf) -> buf.readBlockPos()
                            )
                    )
            );

    public static final MenuType<ChemistryStationMenu> CHEMISTRY_STATION =
            Registry.register(
                    BuiltInRegistries.MENU,
                    ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "chemistry_station"),
                    new ExtendedScreenHandlerType<>(
                            ChemistryStationMenu::new,
                            StreamCodec.of(
                                    (buf, pos) -> buf.writeBlockPos(pos),
                                    (buf) -> buf.readBlockPos()
                            )
                    )
            );

    // Sends the assigned recipe index (int) from server to client.
    public static final MenuType<WrittenRecipeMenu> WRITTEN_RECIPE =
            Registry.register(
                    BuiltInRegistries.MENU,
                    ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "written_recipe"),
                    new ExtendedScreenHandlerType<>(
                            WrittenRecipeMenu::new,   // (containerId, inv, buf) constructor
                            StreamCodec.of(
                                    (buf, index) -> buf.writeInt(index),
                                    (buf) -> buf.readInt()
                            )
                    )
            );

    public static void init() {
        MSGWOFT.LOGGER.info("Registering Menu Types for " + MSGWOFT.MOD_ID);
    }
}