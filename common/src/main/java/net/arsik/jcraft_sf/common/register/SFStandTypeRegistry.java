package net.arsik.jcraft_sf.common.register;

import net.arsik.jcraft_sf.StoneFree;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.arna.jcraft.api.JRegistries;
import net.arna.jcraft.api.stand.StandType;

public interface SFStandTypeRegistry {
    DeferredRegister<StandType> REGISTRY = DeferredRegister.create(StoneFree.MOD_ID, JRegistries.STAND_TYPE_REGISTRY_KEY);

    RegistrySupplier<StandType> STONE_FREE = REGISTRY.register(StoneFree.id("stone_free"), () ->
            StandType.of(StoneFree.id("stone_free"), SFEntityTypeRegistry.STONE_FREE));
}
