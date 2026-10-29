package org.exodusstudio.frostbite.fabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.common.event.custom.CriticalHitEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @ModifyExpressionValue(method = "attack", at = @At(value = "CONSTANT", args = "floatValue=1.5F"))
    private float frostbite$criticalHit(float multiplier, @Local(argsOnly = true) Entity target) {
        CriticalHitEvent event = new CriticalHitEvent((Player) (Object) this, target, multiplier);
        CriticalHitEvent.EVENT.invoker().accept(event);
        return event.getDamageMultiplier();
    }
}
