package net.arsik.jcraft_sf.fabric.datagen;

import net.arsik.jcraft_sf.fabric.datagen.movesets.StoneFreeMoveSetProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class StoneFreeDataGen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGen) {
        FabricDataGenerator.Pack pack = dataGen.createPack();
        pack.addProvider(StoneFreeMoveSetProvider::new);
        pack.addProvider(StoneFreeStandDataProvider::new);
    }
}
