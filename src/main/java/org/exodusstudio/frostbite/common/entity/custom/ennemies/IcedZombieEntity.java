package org.exodusstudio.frostbite.common.entity.custom.ennemies;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.EffectRegistry;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.util.TemperatureEntity;

public class IcedZombieEntity extends Zombie implements TemperatureEntity {
    public IcedZombieEntity(EntityType<? extends Zombie> ignored, Level level) {
        super(EntityRegistry.ICED_ZOMBIE.get(), level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 35)
                .add(Attributes.FOLLOW_RANGE, 35)
                .add(Attributes.MOVEMENT_SPEED, 0.225)
                .add(Attributes.ATTACK_DAMAGE, 4)
                .add(Attributes.ARMOR, 3)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity entity) {
        boolean flag = super.doHurtTarget(level, entity);
        if (flag && entity instanceof LivingEntity livingEntity) {
            Frostbite.temperatureStorage.decreaseTemperature(livingEntity, 5, false);
            if (random.nextFloat() < 0.3f) {
                livingEntity.addEffect(new MobEffectInstance(EffectRegistry.COLD_WEAKNESS, 10 * 20, 0));
            }
        }

        return flag;
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public LivingEntity getInstance() {
        return this;
    }
}
