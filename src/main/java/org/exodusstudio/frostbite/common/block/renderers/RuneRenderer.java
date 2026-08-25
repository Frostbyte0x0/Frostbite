package org.exodusstudio.frostbite.common.block.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.block.block_entities.RuneBlockEntity;
import org.exodusstudio.frostbite.common.block.states.RuneRenderState;
import org.jspecify.annotations.Nullable;

public class RuneRenderer implements BlockEntityRenderer<RuneBlockEntity, RuneRenderState> {
    public RuneRenderer(BlockEntityRendererProvider.Context ignored) {}

    @Override
    public RuneRenderState createRenderState() {
        return new RuneRenderState();
    }

    @Override
    public void extractRenderState(RuneBlockEntity entity, RuneRenderState state, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderState.extractBase(entity, state, breakProgress);
        state.alliesDetected = entity.alliesDetected;
        state.health = entity.opened ? 0 : entity.health;
        state.maxHealth = entity.maxHealth;
        if (state.health / state.maxHealth > 0.8) {
            state.healthColour = ChatFormatting.DARK_GREEN;
        } else if (state.health / state.maxHealth > 0.6) {
            state.healthColour = ChatFormatting.GREEN;
        } else if (state.health / state.maxHealth > 0.4) {
            state.healthColour = ChatFormatting.YELLOW;
        } else if (state.health / state.maxHealth > 0.2) {
            state.healthColour = ChatFormatting.RED;
        } else {
            state.healthColour = ChatFormatting.DARK_RED;
        }
    }

    @Override
    public void submit(RuneRenderState state, PoseStack stack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        submitNodeCollector.submitNameTag(stack, new Vec3(0.5, 1.1, 0.5), 0,
                Component.translatable("rune.render.allies_left", state.alliesDetected).withStyle(ChatFormatting.LIGHT_PURPLE),
                true, state.lightCoords, camera);
        submitNodeCollector.submitNameTag(stack, new Vec3(0.5, 0.8, 0.5), 0,
                Component.translatable("rune.render.health", state.health, state.maxHealth).withStyle(state.healthColour),
                true, state.lightCoords, camera);
    }
}
