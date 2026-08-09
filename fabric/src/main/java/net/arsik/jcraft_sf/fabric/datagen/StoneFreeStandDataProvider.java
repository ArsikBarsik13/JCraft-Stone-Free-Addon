package net.arsik.jcraft_sf.fabric.datagen;

import net.arsik.jcraft_sf.common.register.SFStandTypeRegistry;
import net.arsik.jcraft_sf.common.stand.StoneFreeEntity;
import net.arna.jcraft.api.datagen.JCraftStandDataProvider;
import net.arna.jcraft.api.stand.StandData;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

public class StoneFreeStandDataProvider extends JCraftStandDataProvider {
    public StoneFreeStandDataProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    protected void configure(BiConsumer<ResourceLocation, StandData> provider) {
        provider.accept(SFStandTypeRegistry.STONE_FREE.getId(), StoneFreeEntity.DATA);
    }
}
