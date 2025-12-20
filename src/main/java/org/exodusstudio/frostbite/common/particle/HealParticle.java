package org.exodusstudio.frostbite.common.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.RandomSource;
import org.exodusstudio.frostbite.common.particle.options.BooleanParticleOption;

public class HealParticle extends SingleQuadParticle {
    public HealParticle(
            ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet set
    ) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, set.first());
        this.friction = 0.96F;
        this.hasPhysics = false;
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

    public SingleQuadParticle.Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public record Provider(SpriteSet sprite) implements ParticleProvider<BooleanParticleOption> {
        public Particle createParticle(
                BooleanParticleOption option,
                ClientLevel clientLevel,
                double p_233920_,
                double p_233921_,
                double p_233922_,
                double p_233923_,
                double p_233924_,
                double p_233925_,
                RandomSource randomSource
        ) {
            HealParticle healParticleParticle = new HealParticle(
                    clientLevel, p_233920_, p_233921_, p_233922_, p_233923_, p_233924_, p_233925_, this.sprite
            );

            if (option.bool()) {
                healParticleParticle.setSprite(sprite.get(1, 1));
            } else {
                healParticleParticle.setSprite(sprite.get(0, 1));
            }

            healParticleParticle.setParticleSpeed(p_233923_, p_233924_, p_233925_);
            return healParticleParticle;
        }
    }
}
