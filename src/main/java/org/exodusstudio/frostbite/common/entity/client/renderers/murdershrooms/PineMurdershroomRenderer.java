package org.exodusstudio.frostbite.common.entity.client.renderers.murdershrooms;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.murdershrooms.PineMurdershroomModel;
import org.exodusstudio.frostbite.common.entity.custom.murdershrooms.PineMurdershroomEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PineMurdershroomRenderer extends MobRenderer<PineMurdershroomEntity, LivingEntityRenderState, PineMurdershroomModel> {
    public PineMurdershroomRenderer(EntityRendererProvider.Context context) {
        super(context, new PineMurdershroomModel(context.bakeLayer(ModModelLayers.PINE_MURDERSHROOM)), 0.45f);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntityRenderState pineMurdershroomRenderState) {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/pine_murdershroom/pine_murdershroom.png");
    }
}