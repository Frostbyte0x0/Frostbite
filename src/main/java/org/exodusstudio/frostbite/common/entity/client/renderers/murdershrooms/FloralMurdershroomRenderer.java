package org.exodusstudio.frostbite.common.entity.client.renderers.murdershrooms;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.murdershrooms.FloralMurdershroomModel;
import org.exodusstudio.frostbite.common.entity.custom.murdershrooms.FloralMurdershroomEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FloralMurdershroomRenderer extends MobRenderer<FloralMurdershroomEntity, LivingEntityRenderState, FloralMurdershroomModel> {
    public FloralMurdershroomRenderer(EntityRendererProvider.Context context) {
        super(context, new FloralMurdershroomModel(context.bakeLayer(ModModelLayers.FLORAL_MURDERSHROOM)), 0.45f);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntityRenderState floralMurdershroomRenderState) {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/floral_murdershroom/floral_murdershroom.png");
    }
}