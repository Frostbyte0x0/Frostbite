package org.exodusstudio.frostbite.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.exodusstudio.frostbite.common.registry.AttachmentRegistry;
import org.exodusstudio.frostbite.common.util.TE;
import org.exodusstudio.frostbite.common.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements TE {
    @Unique
    LivingEntity frostbite$entity = (LivingEntity) ((Object) this);

    @Override
    public float getInnerTemp() {
        return Util.getFloat(frostbite$entity, "inner_temperature");
    }

    @Override
    public void setInnerTemp(float temp) {
        Util.setData(frostbite$entity, "inner_temperature", temp);
    }

    @Override
    public float getOuterTemp() {
        return Util.getFloat(frostbite$entity, "outer_temperature");
    }

    @Override
    public void setOuterTemp(float temp) {
        Util.setData(frostbite$entity, "outer_temperature", temp);
    }

    @Override
    public LivingEntity instance() {
        return frostbite$entity;
    }
}
