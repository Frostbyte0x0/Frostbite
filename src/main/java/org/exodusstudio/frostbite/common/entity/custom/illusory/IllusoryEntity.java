package org.exodusstudio.frostbite.common.entity.custom.illusory;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.Monster;
import org.exodusstudio.frostbite.common.particle.DrainParticleOptions;

public abstract class IllusoryEntity {
    private static final RandomSource random = RandomSource.create();

    public static void revealIllusion(Monster monster) {
        for (int i = 0; i < 10; i++) {
            if (monster.level() instanceof ClientLevel) {
                monster.level().addParticle(new DrainParticleOptions(0), monster.getX(), monster.getY(), monster.getZ(), 0, 0, 0);
                monster.level().addParticle(ParticleTypes.DRAGON_BREATH, monster.getX(), monster.getY(), monster.getZ(), 0, 0, 0);
            }
        }
        monster.level().playSound(null, monster.getOnPos(), SoundEvents.LAVA_EXTINGUISH, SoundSource.HOSTILE, 1f, monster.level().getRandom().nextFloat() * 0.1F + 0.9F);
        //monster.discard();
    }
}
