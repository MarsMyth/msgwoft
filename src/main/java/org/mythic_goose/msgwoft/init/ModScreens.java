package org.mythic_goose.msgwoft.init;

import net.minecraft.client.gui.screens.MenuScreens;
import org.mythic_goose.msgwoft.MSGWOFT;
import org.mythic_goose.msgwoft.screen.block.ChemistryStationScreen;
import org.mythic_goose.msgwoft.screen.block.IdentificationStationScreen;

public class ModScreens {

    public static void init() {
        MSGWOFT.LOGGER.info("Initializing Screens for " + MSGWOFT.MOD_ID);
        MenuScreens.register(ModMenuTypes.IDENTIFICATION_STATION, IdentificationStationScreen::new);
        MenuScreens.register(ModMenuTypes.CHEMISTRY_STATION, ChemistryStationScreen::new);
    }
}