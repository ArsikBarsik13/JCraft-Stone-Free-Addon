package net.arsik.jcraft_sf.common.register;

import dev.architectury.registry.registries.RegistrySupplier;
import net.arsik.jcraft_sf.StoneFree;
import net.minecraft.sounds.SoundEvent;

import static net.arsik.jcraft_sf.StoneFree.SFSOUNDS;

public interface SFSoundRegistry {

    static RegistrySupplier<SoundEvent> register(String name) {
        var event = SoundEvent.createVariableRangeEvent(StoneFree.id(name));
        return SFSOUNDS.register(event.getLocation().getPath(), () -> event);
    }

    RegistrySupplier<SoundEvent> SF_SUMMON = register("sfsummon");
    RegistrySupplier<SoundEvent> SF_BARRAGE = register("sfbarrage");

    static void init() {
        // intentionally left empty
    }
}
