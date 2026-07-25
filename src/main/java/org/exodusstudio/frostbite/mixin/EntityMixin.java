package org.exodusstudio.frostbite.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.common.contracts.ContractAttributes;
import org.exodusstudio.frostbite.common.contracts.LivingContractInfo;
import org.exodusstudio.frostbite.common.util.TE;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Unique
    Entity frostbite$entity = (Entity) ((Object) this);

    @Inject(at = @At("HEAD"), method = "isFullyFrozen", cancellable = true)
    private void isFullyFrozen(CallbackInfoReturnable<Boolean> cir) {
        if (frostbite$entity instanceof LivingEntity livingEntity) {
            cir.setReturnValue(frostbite$entity.getTicksFrozen() >= frostbite$entity.getTicksRequiredToFreeze()
                    || ((TE) livingEntity).getInnerTemp() < 10 && livingEntity.canFreeze());
        }
    }

    @Inject(at = @At("HEAD"), method = "isInvisibleTo", cancellable = true)
    private void isInvisibleTo(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (LivingContractInfo.hasAppliedAttribute(player, ContractAttributes.UNAWARE) && frostbite$entity.isShiftKeyDown()) {
            cir.setReturnValue(true);
        }
    }
}
