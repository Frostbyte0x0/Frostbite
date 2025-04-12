package org.exodusstudio.frostbite.common.entity.client.renderers.murdershrooms;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.murdershrooms.DecayingMurdershroomModel;
import org.exodusstudio.frostbite.common.entity.custom.murdershrooms.DecayingMurdershroomEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DecayingMurdershroomRenderer extends MobRenderer<DecayingMurdershroomEntity, LivingEntityRenderState, DecayingMurdershroomModel> {
    public DecayingMurdershroomRenderer(EntityRendererProvider.Context context) {
        super(context, new DecayingMurdershroomModel(context.bakeLayer(ModModelLayers.DECAYING_MURDERSHROOM)), 0.45f);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntityRenderState decayingMurdershroomRenderState) {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/decaying_murdershroom/decaying_murdershroom.png");
    }
}