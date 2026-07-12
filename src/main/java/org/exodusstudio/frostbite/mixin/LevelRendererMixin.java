package org.exodusstudio.frostbite.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.exodusstudio.frostbite.common.item.weapons.goat.BlueJadeKatanaItem;
import org.exodusstudio.frostbite.common.registry.AttachementRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Inject(at = @At("HEAD"), method = "submitEntities")
    private void submitEntities(PoseStack poseStack, LevelRenderState levelRenderState, SubmitNodeCollector output, CallbackInfo ci) {
        assert Minecraft.getInstance().level != null;
        Minecraft.getInstance().level.getEntities().getAll().forEach((entity) -> {
            if (entity instanceof LivingEntity livingEntity && !livingEntity.getData(AttachementRegistry.CURRENT_CHARGE_ATTACK).isEmpty()) {

                switch (livingEntity.getData(AttachementRegistry.CURRENT_CHARGE_ATTACK)) {
                    case "blue_jade_katana_charge_attack" -> BlueJadeKatanaItem.renderChargeAttack(livingEntity, poseStack, levelRenderState, output);
                }
            }
        });
    }
}
