package org.exodusstudio.frostbite.common.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.world.entity.LivingEntity;

public interface Renderable {
    void render(RenderableContext context);
    boolean shouldStopRendering(RenderableContext context);
    String getName();

    record RenderableContext(
            LivingEntity user,
            PoseStack poseStack,
            LevelRenderState levelRenderState,
            float secondsSinceStart,
            SubmitNodeCollector output
    ) {}
}
