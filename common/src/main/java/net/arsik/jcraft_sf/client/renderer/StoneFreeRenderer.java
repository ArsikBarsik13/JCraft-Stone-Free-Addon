package net.arsik.jcraft_sf.client.renderer;

import net.arsik.jcraft_sf.common.register.SFStandTypeRegistry;
import net.arsik.jcraft_sf.common.stand.StoneFreeEntity;
import net.arna.jcraft.client.renderer.entity.stands.StandEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class StoneFreeRenderer extends StandEntityRenderer<StoneFreeEntity> {
    public StoneFreeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, SFStandTypeRegistry.STONE_FREE.get());
    }
}
