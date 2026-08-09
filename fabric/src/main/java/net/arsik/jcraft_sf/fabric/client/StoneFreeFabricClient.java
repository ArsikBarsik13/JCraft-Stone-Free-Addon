package net.arsik.jcraft_sf.fabric.client;

import net.arsik.jcraft_sf.client.StoneFreeClient;
import net.arsik.jcraft_sf.client.register.SFEntityRendererRegister;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public final class StoneFreeFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        StoneFreeClient.init();

        // Register entity renderers
        SFEntityRendererRegister.register(new SFEntityRendererRegister.EntityRendererRegistrar() {
            @Override
            public <T extends Entity> void register(Supplier<? extends EntityType<? extends T>> type, EntityRendererProvider<T> rendererProvider) {
                EntityRendererRegistry.register(type.get(), rendererProvider);
            }
        });
    }
}
