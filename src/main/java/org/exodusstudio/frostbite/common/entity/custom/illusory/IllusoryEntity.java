package org.exodusstudio.frostbite.common.entity.custom.illusory;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.Monster;

public abstract class IllusoryEntity {
    private static final RandomSource random = RandomSource.create();

    public static void revealIllusion(Monster monster) {
        for (int i = 0; i < 10; i++) {
            monster.level().addParticle(ParticleTypes.DRAGON_BREATH, false, true,
                    monster.getX(), monster.getY(), monster.getZ(),
                    ((double)0.5F - random.nextDouble()) * 0.15,
                    0.1F,
                    ((double)0.5F - random.nextDouble()) * 0.15);
        }
        monster.level().playSound(null, monster.getOnPos(), SoundEvents.LAVA_EXTINGUISH, SoundSource.HOSTILE, 1f, monster.level().getRandom().nextFloat() * 0.1F + 0.9F);
        //monster.discard();
    }
}
