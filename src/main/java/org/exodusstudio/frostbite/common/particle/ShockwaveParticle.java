package org.exodusstudio.frostbite.common.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class ShockwaveParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    public ShockwaveParticle(
            ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite
    ) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.friction = 0.96F;
        this.sprites = sprite;
        this.hasPhysics = false;
        this.setSpriteFromAge(sprite);
    }

    @Override
    public void tick() {
        super.tick();
        this.setParticleSpeed(this.xd*0.965, this.yd*0.965, this.zd*0.965);
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
    public record Provider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(
                SimpleParticleType colorParticleOption,
                ClientLevel clientLevel,
                double p_233920_,
                double p_233921_,
                double p_233922_,
                double p_233923_,
                double p_233924_,
                double p_233925_
        ) {
            ShockwaveParticle shockwaveParticle = new ShockwaveParticle(
                    clientLevel, p_233920_, p_233921_, p_233922_, p_233923_, p_233924_, p_233925_, this.sprite
            );

            shockwaveParticle.setParticleSpeed(p_233923_, p_233924_, p_233925_);
            shockwaveParticle.setLifetime(20);
            return shockwaveParticle;
        }
    }
}
