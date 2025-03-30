package org.exodusstudio.frostbite.common.entity.client;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.custom.murdershrooms.AgaricMurdershroomEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AgaricMurdershroomRenderer extends MobRenderer<AgaricMurdershroomEntity, LivingEntityRenderState, AgaricMurdershroomModel> {
    public AgaricMurdershroomRenderer(EntityRendererProvider.Context context) {
        super(context, new AgaricMurdershroomModel(context.bakeLayer(ModModelLayers.AGARIC_MURDERSHROOM)), 0.45f);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntityRenderState agaricMurdershroomRenderState) {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/agaric_murdershroom/agaric_murdershroom.png");
    }
}
