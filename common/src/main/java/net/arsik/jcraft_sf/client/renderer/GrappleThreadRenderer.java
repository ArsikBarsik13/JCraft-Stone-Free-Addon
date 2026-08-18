package net.arsik.jcraft_sf.client.renderer;

import lombok.NonNull;
import mod.azure.azurelib.render.AzRendererPipelineContext;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.entity.AzEntityRendererPipeline;
import net.arna.jcraft.client.renderer.BaseModelRenderer;
import net.arna.jcraft.client.renderer.entity.AbstractEntityRenderer;
import net.arsik.jcraft_sf.StoneFree;
import net.arsik.jcraft_sf.common.entity.GrappleThreadEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.mojang.math.Axis;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Environment(EnvType.CLIENT)
public class GrappleThreadRenderer extends AbstractEntityRenderer<GrappleThreadEntity> {
    protected static final List<ResourceLocation> VARIANTS = IntStream.range(0, 3).mapToObj(
            i -> StoneFree.id("textures/entity/ice_branch/ice_branch_" + i + ".png")).toList();

    public static final String ID = "ice_branch";

    public GrappleThreadRenderer(final @NonNull EntityRendererProvider.Context context) {
        super(AzEntityRendererConfig.<GrappleThreadEntity>builder(
                                entity -> StoneFree.id(AbstractEntityRenderer.MODEL_STR_TEMPLATE.formatted(ID)),
                                entity -> VARIANTS.get(entity.getId() % 3))
                        .setAnimatorProvider(() -> new EntityAnimator<>(ID))
                        .setRenderEntry(contextPipeline -> {
                            final var animatable = contextPipeline.animatable();

                            GrappleThreadEntity.SPAWN.sendForEntity(animatable);

                            return contextPipeline;
                        })
                        .setModelRenderer((pc, layer) -> new BaseModelRenderer<>((AzEntityRendererPipeline<GrappleThreadEntity>) pc, layer) {
                            // weird edge case
                            @Override
                            protected void midRender(@NonNull AzRendererPipelineContext<UUID, GrappleThreadEntity> pc) {
                                var poseStack = pc.poseStack();
                                var animatable = pc.animatable();
                                var partialTick = pc.partialTick();

                                poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, animatable.yRotO, animatable.getYRot()) + 90.0f));
                                poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot())));
                            }
                        })
                        .build(),
                context);
    }

    @Override
    public ResourceLocation getTextureLocation(GrappleThreadEntity entity) {
        return null;
    }

}
