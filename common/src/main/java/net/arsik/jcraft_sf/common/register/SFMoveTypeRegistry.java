package net.arsik.jcraft_sf.common.register;

import net.arsik.jcraft_sf.StoneFree;
import net.arsik.jcraft_sf.common.attack.CocoonAttack;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.arna.jcraft.api.JRegistries;
import net.arna.jcraft.api.attack.MoveType;
import net.arsik.jcraft_sf.common.attack.LowGrappleAttack;

public interface SFMoveTypeRegistry {
    DeferredRegister<MoveType<?>> REGISTRY = DeferredRegister.create(StoneFree.MOD_ID, JRegistries.MOVE_TYPE_REGISTRY_KEY);

    RegistrySupplier<MoveType<?>> COCOON = REGISTRY.register(
            StoneFree.id("cocoon"), CocoonAttack.Type.INSTANCE::cast);

    RegistrySupplier<MoveType<?>> LOW_GRAPPLE = REGISTRY.register(
            StoneFree.id("low_grapple"), LowGrappleAttack.Type.INSTANCE::cast);
}