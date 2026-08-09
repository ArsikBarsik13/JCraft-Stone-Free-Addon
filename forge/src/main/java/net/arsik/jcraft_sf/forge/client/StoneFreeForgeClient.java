package net.arsik.jcraft_sf.forge.client;

import net.arsik.jcraft_sf.StoneFree;
import net.arsik.jcraft_sf.client.StoneFreeClient;
import net.arsik.jcraft_sf.client.register.SFEntityRendererRegister;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = StoneFree.MOD_ID)
public class StoneFreeForgeClient {
    @SubscribeEvent
    public static void handleClientSetup(final FMLClientSetupEvent event) {
        StoneFreeClient.init();
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRegisterEntityRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        SFEntityRendererRegister.register(new SFEntityRendererRegister.EntityRendererRegistrar() {
            @Override
            public <T extends Entity> void register(Supplier<? extends EntityType<? extends T>> type, EntityRendererProvider<T> rendererProvider) {
                event.registerEntityRenderer(type.get(), rendererProvider);
            }
        });
    }
}
