package org.exodusstudio.frostbite.common.entity.custom.guards;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.util.Util;

public class HeavyGuardEntity extends GuardEntity {
    public HeavyGuardEntity(EntityType<? extends GuardEntity> ignored, Level level) {
        super(EntityRegistry.HEAVY_GUARD.get(), level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 125)
                .add(Attributes.FOLLOW_RANGE, 10)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    public AABB getAttackAABB() {
        return Util.squareAABB(position(), 2.5)
                .move(getViewVector(0).normalize().scale(2).add(0, 1.5, 0));
    }

    @Override
    public void doAttack(ServerLevel serverLevel) {
        serverLevel.getEntitiesOfClass(LivingEntity.class, getAttackAABB())
                .forEach(entity -> {
                    if (entity != this) entity.hurtServer(serverLevel, damageSources().mobAttack(this), 12);
                });
    }

    @Override
    public float getSecondsToDisableBlocking() {
        return 2.5f;
    }
}
