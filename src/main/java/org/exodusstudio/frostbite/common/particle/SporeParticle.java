package org.exodusstudio.frostbite.common.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ColorParticleOption;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

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
            //sporeParticle.quadSize = 2.5f;
            sporeParticle.setParticleSpeed(p_233923_, p_233924_, p_233925_);
            sporeParticle.setLifetime(20);
            return sporeParticle;
        }
    }
}
