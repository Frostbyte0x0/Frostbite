package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.ShamanModel;
import org.exodusstudio.frostbite.common.entity.client.states.ShamanRenderState;
import org.exodusstudio.frostbite.common.entity.custom.shaman.ShamanEntity;

public class ShamanRenderer extends StateMonsterRenderer<ShamanEntity, ShamanRenderState, ShamanModel> {
    public ShamanRenderer(EntityRendererProvider.Context context) {
        super(context, new ShamanModel(context.bakeLayer(ModModelLayers.SHAMAN)), 0.45f, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/shaman/shaman.png"));
    }

    @Override
    public ShamanRenderState createRenderState() {
        return new ShamanRenderState();
    }

    @Override
    public void extractRenderState(ShamanEntity shaman, ShamanRenderState state, float p_361157_) {
        super.extractRenderState(shaman, state, p_361157_);
        state.showShield = shaman.isShowingShield();
    }
}
