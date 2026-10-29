package org.exodusstudio.frostbite.common.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.monster.creeper.CreeperModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CreeperPowerLayer;
import net.minecraft.client.renderer.entity.state.CreeperRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.IcedCreeperEntity;

public class IcedCreeperRenderer extends MobRenderer<IcedCreeperEntity, CreeperRenderState, CreeperModel> {
    public IcedCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, new CreeperModel(context.bakeLayer(ModModelLayers.ICED_CREEPER)), 0.45f);
        this.addLayer(new CreeperPowerLayer(this, context.getModelSet()));
    }

    @Override
    public CreeperRenderState createRenderState() {
        return new CreeperRenderState();
    }

    @Override
    public Identifier getTextureLocation(CreeperRenderState renderState) {
        return Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/iced_creeper/iced_creeper.png");
    }

    @Override
    protected void scale(CreeperRenderState p_362568_, PoseStack p_114047_) {
        float f = p_362568_.swelling;
        float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
        f = Mth.clamp(f, 0.0F, 1.0F);
        f *= f;
        f *= f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        p_114047_.scale(f2, f3, f2);
    }

    @Override
    protected float getWhiteOverlayProgress(CreeperRenderState state) {
        float f = state.swelling;
        return (int)(f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
    }

    @Override
    public void extractRenderState(IcedCreeperEntity entity, CreeperRenderState state, float p_364659_) {
        super.extractRenderState(entity, state, p_364659_);
        state.swelling = entity.getSwelling(p_364659_);
        state.isPowered = entity.isPowered();
    }
}
