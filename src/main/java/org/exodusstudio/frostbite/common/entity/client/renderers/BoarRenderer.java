package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.BoarModel;
import org.exodusstudio.frostbite.common.entity.custom.animals.BoarEntity;

public class BoarRenderer extends MobRenderer<BoarEntity, LivingEntityRenderState, BoarModel> {
    public BoarRenderer(EntityRendererProvider.Context context) {
        super(context, new BoarModel(context.bakeLayer(ModModelLayers.BOAR)), 0.45f);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState renderState) {
        return Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/boar/boar.png");
    }
}
