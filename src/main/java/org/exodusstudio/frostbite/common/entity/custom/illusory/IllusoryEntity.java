package org.exodusstudio.frostbite.common.entity.custom.illusory;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;

public abstract class IllusoryEntity {
    private static final RandomSource random = RandomSource.create();

    public static void revealIllusion(Monster monster) {
        Player player = Minecraft.getInstance().player;
        assert player != null;
            for (int i = 0; i < 80; i++) {
            player.level().addAlwaysVisibleParticle(
                    ColorParticleOption.create(ParticleRegistry.SPORE_PARTICLE.get(), 0x400b6b),
                    monster.getX() + 0.5f * random.nextDouble() - Math.sin(monster.yHeadRot * Math.PI / 180) / 1.5f,
                    player.getY() + 0.5f * random.nextDouble() + 1.25f,
                    monster.getZ() + 0.5f * random.nextDouble() + Math.cos(monster.yHeadRot * Math.PI / 180) / 1.5f,
                    (0.5D - random.nextDouble()) * 0.3,
                    (0.5D - random.nextDouble()) * 0.3,
                    (0.5D - random.nextDouble()) * 0.3);
            
        }

        monster.level().playSound(null, monster.getOnPos(), SoundEvents.LAVA_EXTINGUISH, SoundSource.HOSTILE, 1f, monster.level().getRandom().nextFloat() * 0.1F + 0.9F);
        monster.discard();
    }
}
