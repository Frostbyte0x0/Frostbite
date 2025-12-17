package org.exodusstudio.frostbite.common.entity.custom.bullets;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.level.Level;

public abstract class AbstractBullet extends AbstractArrow {
    protected AbstractBullet(EntityType<? extends AbstractArrow> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }
}
