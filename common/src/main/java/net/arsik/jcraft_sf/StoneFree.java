package net.arsik.jcraft_sf;

import dev.architectury.registry.registries.DeferredRegister;
import net.arsik.jcraft_sf.common.register.SFEntityTypeRegistry;
import net.arsik.jcraft_sf.common.register.SFMoveTypeRegistry;
import net.arsik.jcraft_sf.common.register.SFSoundRegistry;
import net.arsik.jcraft_sf.common.register.SFStandTypeRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public final class StoneFree {
    public static final String MOD_ID = "jcraft_sf";
    public static final String BASE_CONTROLLER = "base_controller";

    public static final DeferredRegister<SoundEvent> SFSOUNDS = DeferredRegister.create(MOD_ID, Registries.SOUND_EVENT);

    public static void init() {
        SFEntityTypeRegistry.REGISTRY.register();
        SFStandTypeRegistry.REGISTRY.register();
        SFMoveTypeRegistry.REGISTRY.register();

        SFEntityTypeRegistry.registerAttributes();

        SFSoundRegistry.init();
        SFSOUNDS.register();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
