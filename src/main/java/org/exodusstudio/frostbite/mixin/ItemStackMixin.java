package org.exodusstudio.frostbite.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.common.contracts.Contract;
import org.exodusstudio.frostbite.common.contracts.ContractAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Unique
    ItemStack frostbite$stack = (ItemStack) ((Object) this);

    @Inject(at = @At("TAIL"), method = "processDurabilityChange(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;)I", cancellable = true)
    private void processDurabilityChange(int amount, ServerLevel level, LivingEntity player, CallbackInfoReturnable<Integer> cir) {
        Contract c = Contract.getContract(frostbite$stack);
        if (c != null && c.hasAttribute(ContractAttributes.BRITTLE)) {
            cir.setReturnValue(cir.getReturnValue() + c.allScalableAttributes().get(ContractAttributes.BRITTLE));
        }
    }
}
