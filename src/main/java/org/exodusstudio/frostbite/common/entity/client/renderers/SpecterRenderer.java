package org.exodusstudio.frostbite.common.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.SpecterModel;
import org.exodusstudio.frostbite.common.entity.client.states.SpecterRenderState;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.SpecterEntity;
import org.jetbrains.annotations.Nullable;

public class SpecterRenderer extends MobRenderer<SpecterEntity, SpecterRenderState, SpecterModel> {
    public static final Identifier SPECTER_SOLID_TEXTURE =
            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/specter/specter_solid.png");
    public static final Identifier SPECTER_TRANSPARENT_TEXTURE =
            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/specter/specter_transparent.png");

    public SpecterRenderer(EntityRendererProvider.Context context) {
        super(context, new SpecterModel(context.bakeLayer(ModModelLayers.SPECTER)), 0.45f);
    }

    @Override
    public void submit(SpecterRenderState state, PoseStack stack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        stack.scale(0.7f, 0.7f, 0.7f);
        super.submit(state, stack, nodeCollector, cameraRenderState);
    }

    @Override
    protected @Nullable RenderType getRenderType(SpecterRenderState renderState, boolean isVisible, boolean renderTranslucent, boolean appearsGlowing) {
        return renderState.isTransparent ?
                RenderTypes.entityTranslucent(SPECTER_TRANSPARENT_TEXTURE) :
                RenderTypes.entityCutoutNoCull(SPECTER_SOLID_TEXTURE);
    }

    @Override
    public SpecterRenderState createRenderState() {
        return new SpecterRenderState();
    }

    @Override
    public void extractRenderState(SpecterEntity specter, SpecterRenderState state, float partialTick) {
        super.extractRenderState(specter, state, partialTick);
        state.isTransparent = specter.isTransparent();
    }

    @Override
    public Identifier getTextureLocation(SpecterRenderState renderState) {
        return renderState.isTransparent ? SPECTER_TRANSPARENT_TEXTURE : SPECTER_SOLID_TEXTURE;
    }
}
