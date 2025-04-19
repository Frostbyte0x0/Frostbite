package org.exodusstudio.frostbite.common.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.exodusstudio.frostbite.Frostbite;

public class SporeParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    public SporeParticle(
            ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite
    ) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.friction = 0.96F;
        this.sprites = sprite;
        this.hasPhysics = false;
        this.setSpriteFromAge(sprite);
    }

    @Override
    public int getLightColor(float p_233902_) {
        return 240;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public record Provider(SpriteSet sprite) implements ParticleProvider<ColorParticleOption> {
        private static final RandomSource random = RandomSource.create();
        private static final float variation = 10f;

        public Particle createParticle(
                ColorParticleOption colorParticleOption,
                ClientLevel clientLevel,
                double p_233920_,
                double p_233921_,
                double p_233922_,
                double p_233923_,
                double p_233924_,
                double p_233925_
        ) {
            SporeParticle sporeParticle = new SporeParticle(
                    clientLevel, p_233920_, p_233921_, p_233922_, p_233923_, p_233924_, p_233925_, this.sprite
            );

            Frostbite.LOGGER.debug("R: " + colorParticleOption.getRed());
            Frostbite.LOGGER.debug("G: " + colorParticleOption.getGreen());
            Frostbite.LOGGER.debug("B: " + colorParticleOption.getBlue());

            sporeParticle.setColor(colorParticleOption.getRed() + random.nextFloat() / variation,
                    colorParticleOption.getGreen() + random.nextFloat() / variation,
                    colorParticleOption.getBlue() + random.nextFloat() / variation);
            sporeParticle.setParticleSpeed(p_233923_, p_233924_, p_233925_);
            sporeParticle.setLifetime(20);
            return sporeParticle;
        }
    }
}
