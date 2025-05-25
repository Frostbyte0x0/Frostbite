package org.exodusstudio.frostbite.common.entity.client.renderers.murdershrooms;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.murdershrooms.LightMurdershroomModel;
import org.exodusstudio.frostbite.common.entity.custom.murdershrooms.LightMurdershroomEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class LightMurdershroomRenderer extends MobRenderer<LightMurdershroomEntity, LivingEntityRenderState, LightMurdershroomModel> {
    public LightMurdershroomRenderer(EntityRendererProvider.Context context) {
        super(context, new LightMurdershroomModel(context.bakeLayer(ModModelLayers.LIGHT_MURDERSHROOM)), 0.45f);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntityRenderState lightMurdershroomRenderState) {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/light_murdershroom/light_murdershroom.png");
    }
}