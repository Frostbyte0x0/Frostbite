package org.exodusstudio.frostbite.common.entity.custom.elves;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public abstract class ElfEntity extends Monster {
    protected ElfEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50)
                .add(Attributes.FOLLOW_RANGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }
}
