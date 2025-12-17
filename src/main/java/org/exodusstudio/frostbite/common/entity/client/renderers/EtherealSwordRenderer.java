package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.EtherealSwordModel;
import org.exodusstudio.frostbite.common.entity.client.states.EtherealAnimationState;
import org.exodusstudio.frostbite.common.entity.custom.misc.EtherealSwordEntity;

public class EtherealSwordRenderer extends EtherealRenderer<EtherealSwordEntity, EtherealSwordModel> {
    public EtherealSwordRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public EtherealSwordModel createModel(EntityRendererProvider.Context context) {
        return new EtherealSwordModel(context.bakeLayer(ModModelLayers.ETHEREAL_SWORD));
    }

    @Override
    public void extractRenderState(EtherealSwordEntity entity, EtherealAnimationState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.animationState.copyFrom(entity.animationState);
        reusedState.yRot = entity.getYRot(partialTick);
    }

    @Override
    public Identifier getTextureLocation() {
        return Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/ethereal_sword/ethereal_sword.png");
    }
}
