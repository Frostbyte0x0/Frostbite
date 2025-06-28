package org.exodusstudio.frostbite.common.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.SquidRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.LevitatingJellyfishModel;
import org.exodusstudio.frostbite.common.entity.client.states.LevitatingJellyfishState;
import org.exodusstudio.frostbite.common.entity.custom.LevitatingJellyfishEntity;

public class LevitatingJellyfishRenderer extends LivingEntityRenderer<LevitatingJellyfishEntity, LevitatingJellyfishState, LevitatingJellyfishModel> {
    public LevitatingJellyfishRenderer(EntityRendererProvider.Context context) {
        super(context, new LevitatingJellyfishModel(context.bakeLayer(ModModelLayers.LEVITATING_JELLYFISH)), 0.45f);
    }

    @Override
    public LevitatingJellyfishState createRenderState() {
        return new LevitatingJellyfishState();
    }

    @Override
    public void render(LevitatingJellyfishState jellyfishState, PoseStack poseStack, MultiBufferSource bufferSource, int p_115313_) {
        super.render(jellyfishState, poseStack, bufferSource, p_115313_);
        this.model.setupAnim(jellyfishState);
    }

    @Override
    public void extractRenderState(LevitatingJellyfishEntity jellyfish, LevitatingJellyfishState state, float p_363384_) {
        super.extractRenderState(jellyfish, state, p_363384_);
        // state.tentacleAngle = Mth.lerp(p_363384_, jellyfish.oldTentacleAngle, jellyfish.tentacleAngle);
        state.swimmingAnimationState.copyFrom(jellyfish.swimmingAnimationState);
        state.xBodyRot = Mth.lerp(p_363384_, jellyfish.xBodyRotO, jellyfish.xBodyRot);
        state.zBodyRot = Mth.lerp(p_363384_, jellyfish.zBodyRotO, jellyfish.zBodyRot);
        state.moveCooldown = jellyfish.getMoveCooldown();
    }

    @Override
    public ResourceLocation getTextureLocation(LevitatingJellyfishState renderState) {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/levitating_jellyfish/levitating_jellyfish.png");
    }

    @Override
    protected void setupRotations(LevitatingJellyfishState state, PoseStack stack, float p_116026_, float p_116027_) {
        stack.translate(0.0F, state.isBaby ? 0.25F : 0.5F, 0.0F);
        stack.mulPose(Axis.YP.rotationDegrees(180.0F - p_116026_));
        stack.mulPose(Axis.XP.rotationDegrees(state.xBodyRot));
        stack.mulPose(Axis.YP.rotationDegrees(state.zBodyRot));
        stack.translate(0.0F, state.isBaby ? -0.3F : -0.6F, 0.0F);
    }
}
