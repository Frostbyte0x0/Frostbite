package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.RainFrogModel;
import org.exodusstudio.frostbite.common.entity.custom.animals.RainFrogEntity;

public class RainFrogRenderer extends MobRenderer<RainFrogEntity, LivingEntityRenderState, RainFrogModel> {
    public RainFrogRenderer(EntityRendererProvider.Context context) {
        super(context, new RainFrogModel(context.bakeLayer(ModModelLayers.RAIN_FROG)), 0.45f);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntityRenderState agaricMurdershroomRenderState) {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/rain_frog/rain_frog.png");
    }
}
