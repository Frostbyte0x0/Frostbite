package org.exodusstudio.frostbite.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.exodusstudio.frostbite.common.registry.AttachmentRegistry;
import org.exodusstudio.frostbite.common.util.TE;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements TE {
    @Unique
    LivingEntity frostbite$entity = (LivingEntity) ((Object) this);

    @Override
    public float getInnerTemp() {
        return frostbite$entity.getData(AttachmentRegistry.INNER_TEMPERATURE);
    }

    @Override
    public void setInnerTemp(float temp) {
        frostbite$entity.setData(AttachmentRegistry.INNER_TEMPERATURE, temp);
    }

    @Override
    public float getOuterTemp() {
        return frostbite$entity.getData(AttachmentRegistry.OUTER_TEMPERATURE);
    }

    @Override
    public void setOuterTemp(float temp) {
        frostbite$entity.setData(AttachmentRegistry.OUTER_TEMPERATURE, temp);
    }

    @Override
    public LivingEntity instance() {
        return frostbite$entity;
    }
}
