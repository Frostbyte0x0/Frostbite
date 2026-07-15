package org.exodusstudio.frostbite.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.exodusstudio.frostbite.common.item.weapons.ChargeAttackWeapon;
import org.exodusstudio.frostbite.common.registry.AttachementRegistry;
import org.exodusstudio.frostbite.common.registry.Renderables;
import org.exodusstudio.frostbite.common.util.Renderable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Inject(at = @At("HEAD"), method = "submitEntities")
    private void submitEntities(PoseStack poseStack, LevelRenderState levelRenderState, SubmitNodeCollector output, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        assert mc.level != null;
        HashMap<UUID, Pair<String, Long>> toRemove = new HashMap<>();

        mc.level.getData(AttachementRegistry.CURRENT_RENDERING_ATTACKS).forEach((uuid, chargeAttackRenderables) -> {
            for (Pair<String, Long> chargeAttackRenderable : chargeAttackRenderables) {
                LivingEntity user = (LivingEntity) mc.level.getEntity(uuid);
                if (user != null) {
                    Renderable renderable = Renderables.RENDERABLES.get(chargeAttackRenderable.getFirst());
                    float partialTicks = mc.getDeltaTracker().getGameTimeDeltaPartialTick(!mc.level.tickRateManager().isEntityFrozen(user));

                    if (renderable.shouldStopRendering(new Renderable.RenderableContext(
                            user,
                            poseStack,
                            levelRenderState,
                            (levelRenderState.gameTime + partialTicks - chargeAttackRenderable.getSecond()) / 20f,
                            output
                    ))) {
                        toRemove.put(uuid, chargeAttackRenderable);
                        continue;
                    }

                    renderable.render(new Renderable.RenderableContext(
                            user,
                            poseStack,
                            levelRenderState,
                            (levelRenderState.gameTime + partialTicks - chargeAttackRenderable.getSecond()) / 20f,
                            output
                    ));
                }
            }
        });

        for (Map.Entry<UUID, Pair<String, Long>> entry : toRemove.entrySet()) {
            LivingEntity user = (LivingEntity) Minecraft.getInstance().level.getEntity(entry.getKey());
            if (user == null) continue;
            String chargeAttackRenderable = entry.getValue().getFirst();
            ChargeAttackWeapon.removeChargeAttack(user, chargeAttackRenderable);
        }
        mc.level.setData(AttachementRegistry.CURRENT_RENDERING_ATTACKS, mc.level.getData(AttachementRegistry.CURRENT_RENDERING_ATTACKS));
    }
}
