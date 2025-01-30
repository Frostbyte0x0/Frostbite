package org.exodusstudio.frostbite.common.entity.client;

import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.custom.murdershrooms.AgaricMurdershroomEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AgaricMurdershroomRenderer extends MobRenderer<AgaricMurdershroomEntity, AgaricMurdershroomRenderState, AgaricMurdershroomModel> {
    public AgaricMurdershroomRenderer(EntityRendererProvider.Context context) {
        super(context, new AgaricMurdershroomModel(context.bakeLayer(ModModelLayers.AGARIC_MURDERSHROOM)), 0.55f);
    }

    @Override
    public AgaricMurdershroomRenderState createRenderState() {
        return new AgaricMurdershroomRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(AgaricMurdershroomRenderState agaricMurdershroomRenderState) {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/agaric_murdershroom/agaric_murdershroom.png");
    }
}
