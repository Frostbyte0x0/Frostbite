package org.exodusstudio.frostbite.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> {
    @Unique
    Minecraft frostbite$mc = Minecraft.getInstance();

    @Inject(at = @At("HEAD"), method = "shouldShowName*", cancellable = true)
    private void shouldShowName(T entity, double distanceToCameraSq, CallbackInfoReturnable<Boolean> cir) {
        if (frostbite$shouldShowEntityOutlines()) {
            cir.setReturnValue(true);
        }
    }

    @Unique
    public boolean frostbite$shouldShowEntityOutlines() {
        assert frostbite$mc.player != null;
        return (frostbite$mc.player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistry.ADVANCED_THERMAL_LENS) ||
                frostbite$mc.player.getItemInHand(InteractionHand.OFF_HAND).is(ItemRegistry.ADVANCED_THERMAL_LENS))
                && frostbite$mc.player.isUsingItem() && frostbite$mc.options.getCameraType().isFirstPerson();
    }
}
