package org.exodusstudio.frostbite.common.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.exodusstudio.frostbite.common.registry.AttachementRegistry;

import java.util.*;

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


    static void addRenderable(LivingEntity user, String renderable) {
        Map<UUID, List<Pair<String, Long>>> currentCharges = new HashMap<>(user.level().getData(AttachementRegistry.CURRENT_RENDERING_ATTACKS));
        if (currentCharges.get(user.getUUID()) != null) {
            currentCharges.put(user.getUUID(), new ArrayList<>(currentCharges.get(user.getUUID())));
            currentCharges.get(user.getUUID()).add(Pair.of(renderable, user.level().getGameTime()));
        } else {
            currentCharges.put(user.getUUID(), new ArrayList<>(List.of(Pair.of(renderable, user.level().getGameTime()))));
        }
        user.level().setData(AttachementRegistry.CURRENT_RENDERING_ATTACKS, currentCharges);
    }

    static void removeRenderable(LivingEntity user, String renderable) {
        Map<UUID, List<Pair<String, Long>>> currentCharges = new HashMap<>(user.level().getData(AttachementRegistry.CURRENT_RENDERING_ATTACKS));
        for (Pair<String, Long> pair : currentCharges.get(user.getUUID())) {
            if (pair.getFirst().equals(renderable)) {
                currentCharges.put(user.getUUID(), new ArrayList<>(currentCharges.get(user.getUUID())));
                currentCharges.get(user.getUUID()).remove(pair);
                break;
            }
        }
        user.level().setData(AttachementRegistry.CURRENT_RENDERING_ATTACKS, currentCharges);
    }
}
