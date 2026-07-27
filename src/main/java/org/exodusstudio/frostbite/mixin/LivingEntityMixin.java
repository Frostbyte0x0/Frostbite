package org.exodusstudio.frostbite.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.common.contracts.ContractAttributes;
import org.exodusstudio.frostbite.common.contracts.LivingContractInfo;
import org.exodusstudio.frostbite.common.util.DataHelper;
import org.exodusstudio.frostbite.common.util.TE;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements TE {
    @Unique
    LivingEntity frostbite$entity = (LivingEntity) ((Object) this);

    @Override
    public float getInnerTemp() {
        return DataHelper.getFloat(frostbite$entity, "inner_temperature");
    }

    @Override
    public void setInnerTemp(float temp) {
        DataHelper.setData(frostbite$entity, "inner_temperature", temp);
    }

    @Override
    public float getOuterTemp() {
        return DataHelper.getFloat(frostbite$entity, "outer_temperature");
    }

    @Override
    public void setOuterTemp(float temp) {
        DataHelper.setData(frostbite$entity, "outer_temperature", temp);
    }

    @Override
    public LivingEntity instance() {
        return frostbite$entity;
    }


    @Inject(at = @At("TAIL"), method = "isSensitiveToWater", cancellable = true)
    private void isSensitiveToWater(CallbackInfoReturnable<Boolean> cir) {
        if (frostbite$entity instanceof LivingEntity && LivingContractInfo.hasAppliedAttribute(frostbite$entity, ContractAttributes.HYDROPHOBIA)) {
            cir.setReturnValue(true);
        }
    }
}
