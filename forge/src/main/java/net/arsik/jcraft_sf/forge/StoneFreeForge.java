package net.arsik.jcraft_sf.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.arsik.jcraft_sf.StoneFree;

@Mod(StoneFree.MOD_ID)
public final class StoneFreeForge {
    public StoneFreeForge(FMLJavaModLoadingContext ctx) {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(StoneFree.MOD_ID, ctx.getModEventBus());

        // Run our common setup.
        StoneFree.init();
    }
}
