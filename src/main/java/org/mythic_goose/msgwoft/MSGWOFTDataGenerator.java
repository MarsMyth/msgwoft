package org.mythic_goose.msgwoft;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.mythic_goose.msgwoft.datagen.*;

public class MSGWOFTDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModelProvider::new);
        pack.addProvider(LanguageProvider::new);
        pack.addProvider(RecipeGenerator::new);
        pack.addProvider(ItemTagProvider::new);
        pack.addProvider(BlockTagProvider::new);
        pack.addProvider(BlockLootTablesProvider::new);
        pack.addProvider(AdvancementGenerator::new);

	}
}
