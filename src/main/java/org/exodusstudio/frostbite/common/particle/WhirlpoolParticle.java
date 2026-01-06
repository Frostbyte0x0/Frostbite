package org.exodusstudio.frostbite.common.particle;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.joml.Quaternionf;

public class WhirlpoolParticle extends SingleQuadParticle {
    private final SpriteSet sprites;

    public WhirlpoolParticle(
            ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite
    ) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite.first());
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
    public SingleQuadParticle.Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    public void extract(QuadParticleRenderState reusedState, Camera camera, float partialTicks) {
        Quaternionf quaternionf = new Quaternionf();
        quaternionf.rotationX((float) -Math.PI / 2);
        this.extractRotatedQuad(reusedState, camera, quaternionf, partialTicks);
        quaternionf.rotationX((float) Math.PI / 2);
        this.extractRotatedQuad(reusedState, camera, quaternionf, partialTicks);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
    }

    public record Provider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(
                SimpleParticleType options,
                ClientLevel clientLevel,
                double p_233920_,
                double p_233921_,
                double p_233922_,
                double p_233923_,
                double p_233924_,
                double p_233925_,
                RandomSource random
        ) {
            WhirlpoolParticle drainParticle = new WhirlpoolParticle(
                    clientLevel, p_233920_, p_233921_, p_233922_, p_233923_, p_233924_, p_233925_, this.sprite
            );
            drainParticle.quadSize = 2.5f;
            drainParticle.setParticleSpeed(p_233923_, p_233924_, p_233925_);
            drainParticle.setLifetime(100);
            return drainParticle;
        }
    }
}
