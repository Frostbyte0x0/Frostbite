package org.exodusstudio.frostbite.common.entity.client.renderers.murdershrooms;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.murdershrooms.MossyMurdershroomModel;
import org.exodusstudio.frostbite.common.entity.custom.murdershrooms.MossyMurdershroomEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MossyMurdershroomRenderer extends MobRenderer<MossyMurdershroomEntity, LivingEntityRenderState, MossyMurdershroomModel> {
    public MossyMurdershroomRenderer(EntityRendererProvider.Context context) {
        super(context, new MossyMurdershroomModel(context.bakeLayer(ModModelLayers.MOSSY_MURDERSHROOM)), 0.45f);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntityRenderState mossyMurdershroomRenderState) {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/mossy_murdershroom/mossy_murdershroom.png");
    }
}