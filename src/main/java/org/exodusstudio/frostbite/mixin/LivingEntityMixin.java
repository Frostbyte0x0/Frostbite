package org.exodusstudio.frostbite.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.exodusstudio.frostbite.common.util.DataHelper;
import org.exodusstudio.frostbite.common.util.TE;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

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


//    @Inject(at = @At("TAIL"), method = "getMaxHealth", cancellable = true)
//    private void getMaxHealth(CallbackInfoReturnable<Float> cir) {
//        if (LivingContractInfo.hasAppliedAttribute(frostbite$entity, ContractAttributes.TANK)) {
//            cir.setReturnValue(cir.getReturnValue() + LivingContractInfo.getAppliedAttributeStat(frostbite$entity, ContractAttributes.TANK));
//        }
//    }
}
