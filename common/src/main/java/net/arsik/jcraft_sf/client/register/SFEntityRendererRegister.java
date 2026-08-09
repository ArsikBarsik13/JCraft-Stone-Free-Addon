package net.arsik.jcraft_sf.client.register;

import net.arsik.jcraft_sf.client.renderer.StoneFreeRenderer;
import net.arsik.jcraft_sf.common.register.SFEntityTypeRegistry;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public interface SFEntityRendererRegister {
    static void register(EntityRendererRegistrar registrar) {
        registrar.register(SFEntityTypeRegistry.STONE_FREE, StoneFreeRenderer::new);
    }

    @FunctionalInterface
    interface EntityRendererRegistrar {
        <T extends Entity> void register(Supplier<? extends EntityType<? extends T>> type,
                                         EntityRendererProvider<T> rendererProvider);
    }
}
