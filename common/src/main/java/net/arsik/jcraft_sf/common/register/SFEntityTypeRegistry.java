package net.arsik.jcraft_sf.common.register;

import net.arna.jcraft.JCraft;
import net.arna.jcraft.api.registry.JEntityTypeRegistry;
import net.arsik.jcraft_sf.StoneFree;
import net.arsik.jcraft_sf.common.entity.GrappleThreadEntity;
import net.arsik.jcraft_sf.common.entity.stand.StoneFreeEntity;
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

    RegistrySupplier<EntityType<GrappleThreadEntity>> GRAPPLE_THREAD = REGISTRY.register(JCraft.id("grapple_thread"),
            () -> EntityType.Builder.of(
                            JEntityTypeRegistry.WorldOnlyEntityFactory.from(GrappleThreadEntity::new),
                            MobCategory.MISC
                    ).sized((float) GrappleThreadEntity.LENGTH, (float) GrappleThreadEntity.LENGTH)
                    .clientTrackingRange(6)
                    .updateInterval(20)
                    .build("grapple_thread")
    );

    static void registerAttributes() {
        EntityAttributeRegistry.register(STONE_FREE, StoneFreeEntity::createMobAttributes);
    }
}