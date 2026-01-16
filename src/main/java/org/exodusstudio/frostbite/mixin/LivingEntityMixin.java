package org.exodusstudio.frostbite.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.exodusstudio.frostbite.common.registry.AttachementRegistry;
import org.exodusstudio.frostbite.common.util.TE;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements TE {
    @Unique
    LivingEntity frostbite$entity = (LivingEntity) ((Object) this);

    @Override
    public float getInnerTemp() {
        return frostbite$entity.getData(AttachementRegistry.INNER_TEMPERATURE);
    }

    @Override
    public void setInnerTemp(float temp) {
        frostbite$entity.setData(AttachementRegistry.INNER_TEMPERATURE, temp);
    }

    @Override
    public float getOuterTemp() {
        return frostbite$entity.getData(AttachementRegistry.OUTER_TEMPERATURE);
    }

    @Override
    public void setOuterTemp(float temp) {
        frostbite$entity.setData(AttachementRegistry.OUTER_TEMPERATURE, temp);
    }

    @Override
    public LivingEntity instance() {
        return frostbite$entity;
    }
}
