package org.exodusstudio.frostbite.common.entity.custom.helper;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class StateBossMonster extends StateMonsterEntity {
    protected StateBossMonster(EntityType<? extends Monster> p_21368_, Level p_21369_, int blendTicks) {
        super(p_21368_, p_21369_, blendTicks);
    }
}
