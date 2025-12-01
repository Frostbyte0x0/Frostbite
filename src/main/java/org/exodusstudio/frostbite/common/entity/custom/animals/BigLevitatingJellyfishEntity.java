package org.exodusstudio.frostbite.common.entity.custom.animals;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.registry.EffectRegistry;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

public class BigLevitatingJellyfishEntity extends LevitatingJellyfishEntity {
    public BigLevitatingJellyfishEntity(EntityType<? extends LevitatingJellyfishEntity> ignored, Level level) {
        super(EntityRegistry.BIG_LEVITATING_JELLYFISH.get(), level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LevitatingJellyfishEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.MOVEMENT_SPEED, 0.15);
    }

    @Override
    public void playerTouch(Player player) {
        super.playerTouch(player);
        player.addEffect(new MobEffectInstance(EffectRegistry.FATIGUE, 9600));
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (source.getEntity() instanceof Player && random.nextFloat() < 0.25f) {
            ((Player) source.getEntity()).addEffect(new MobEffectInstance(EffectRegistry.FATIGUE, 2400));
        }
        return super.hurtServer(level, source, damage);
    }
}
