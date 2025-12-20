package org.exodusstudio.frostbite.common.particle;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.util.RandomSource;
import org.joml.Quaternionf;

public class ExpandingCircleParticle extends SimpleAnimatedParticle {
    private final SpriteSet sprites;

    public ExpandingCircleParticle(
            ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite
    ) {
        super(level, x, y, z, sprite, 1.25F);
        this.friction = 0.96F;
        this.sprites = sprite;
        this.hasPhysics = true;
        this.quadSize = 2;
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
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

    public record Provider(SpriteSet sprite) implements ParticleProvider<ColorParticleOption> {
        public Particle createParticle(
                ColorParticleOption options,
                ClientLevel clientLevel,
                double p_233920_,
                double p_233921_,
                double p_233922_,
                double p_233923_,
                double p_233924_,
                double p_233925_,
                RandomSource randomSource
        ) {
            ExpandingCircleParticle particle = new ExpandingCircleParticle(
                    clientLevel, p_233920_, p_233921_, p_233922_, p_233923_, p_233924_, p_233925_, this.sprite
            );

            particle.setParticleSpeed(p_233923_, p_233924_, p_233925_);
            particle.setLifetime((int) (options.getAlpha() * 255));
            particle.setColor(options.getRed(), options.getGreen(), options.getBlue());
            return particle;
        }
    }
}
