package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.BigLevitatingJellyfishModel;
import org.exodusstudio.frostbite.common.entity.client.states.LevitatingJellyfishRenderState;
import org.exodusstudio.frostbite.common.entity.custom.animals.BigLevitatingJellyfishEntity;
import org.jetbrains.annotations.Nullable;

public class BigLevitatingJellyfishRenderer extends MobRenderer<BigLevitatingJellyfishEntity, LevitatingJellyfishRenderState, BigLevitatingJellyfishModel> {
    public BigLevitatingJellyfishRenderer(EntityRendererProvider.Context context) {
        super(context, new BigLevitatingJellyfishModel(context.bakeLayer(ModModelLayers.BIG_LEVITATING_JELLYFISH)), 0.45f);
    }

    @Override
    public LevitatingJellyfishRenderState createRenderState() {
        return new LevitatingJellyfishRenderState();
    }

    @Override
    protected @Nullable RenderType getRenderType(LevitatingJellyfishRenderState renderState, boolean isVisible, boolean renderTranslucent, boolean appearsGlowing) {
        return RenderTypes.entityTranslucent(getTextureLocation(renderState));
    }

    @Override
    public void extractRenderState(BigLevitatingJellyfishEntity entity, LevitatingJellyfishRenderState state, float p_361157_) {
        super.extractRenderState(entity, state, p_361157_);
        state.idleAnimationState.copyFrom(entity.idleAnimationState);
    }

    @Override
    public Identifier getTextureLocation(LevitatingJellyfishRenderState renderState) {
        return Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/big_levitating_jellyfish/big_levitating_jellyfish.png");
    }
}
