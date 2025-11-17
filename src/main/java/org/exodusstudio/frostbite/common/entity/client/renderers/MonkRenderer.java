package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.MonkModel;
import org.exodusstudio.frostbite.common.entity.client.states.MonkRenderState;
import org.exodusstudio.frostbite.common.entity.custom.MonkEntity;

public class MonkRenderer extends MobRenderer<MonkEntity, MonkRenderState, MonkModel> {
    public MonkRenderer(EntityRendererProvider.Context context) {
        super(context, new MonkModel(context.bakeLayer(ModModelLayers.MONK)), 0.45f);
    }

    @Override
    public MonkRenderState createRenderState() {
        return new MonkRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(MonkRenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/monk/monk.png");
    }
}
