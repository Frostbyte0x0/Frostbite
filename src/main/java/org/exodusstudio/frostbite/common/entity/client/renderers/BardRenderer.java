package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.BardModel;
import org.exodusstudio.frostbite.common.entity.custom.bard.BardEntity;

public class BardRenderer extends MobRenderer<BardEntity, LivingEntityRenderState, BardModel> {
    public BardRenderer(EntityRendererProvider.Context context) {
        super(context, new BardModel(context.bakeLayer(ModModelLayers.BARD)), 0.45f);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntityRenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/bard/bard.png");
    }
}
