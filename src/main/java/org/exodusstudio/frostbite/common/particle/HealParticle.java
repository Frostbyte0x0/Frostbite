package org.exodusstudio.frostbite.common.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.exodusstudio.frostbite.common.particle.options.BooleanParticleOption;

public class HealParticle extends TextureSheetParticle {
    public HealParticle(
            ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite
    ) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.friction = 0.96F;
        this.hasPhysics = false;
        this.setSpriteFromAge(sprite);
        this.setLifetime(15);
    }

    @Override
    public void tick() {
        super.tick();
        this.setParticleSpeed(xd * 0.96, yd * 0.96, zd * 0.96);
    }

    @Override
    public int getLightColor(float p_233902_) {
        return 240;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider(SpriteSet sprite) implements ParticleProvider<BooleanParticleOption> {
        public Particle createParticle(
                BooleanParticleOption simpleParticleOption,
                ClientLevel clientLevel,
                double p_233920_,
                double p_233921_,
                double p_233922_,
                double p_233923_,
                double p_233924_,
                double p_233925_
        ) {
            HealParticle healParticleParticle = new HealParticle(
                    clientLevel, p_233920_, p_233921_, p_233922_, p_233923_, p_233924_, p_233925_, this.sprite
            );

            healParticleParticle.setParticleSpeed(p_233923_, p_233924_, p_233925_);
            return healParticleParticle;
        }
    }
}
