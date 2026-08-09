package net.arsik.jcraft_sf.common.register;

import net.arsik.jcraft_sf.StoneFree;
import net.arsik.jcraft_sf.common.stand.StoneFreeEntity;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public interface SFEntityTypeRegistry {
    DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(StoneFree.MOD_ID, Registries.ENTITY_TYPE);

    RegistrySupplier<EntityType<StoneFreeEntity>> STONE_FREE = REGISTRY.register("stone_free", () ->
            EntityType.Builder.of((EntityType.EntityFactory<StoneFreeEntity>)
                            (t, level) -> new StoneFreeEntity(level),
                            MobCategory.CREATURE)
                    .sized(0.6f, 1.8f)
                    .build("stone_free"));

    static void registerAttributes() {
        EntityAttributeRegistry.register(STONE_FREE, StoneFreeEntity::createMobAttributes);
    }
}
