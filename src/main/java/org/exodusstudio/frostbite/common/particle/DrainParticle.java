package org.exodusstudio.frostbite.common.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import org.joml.Quaternionf;

public class DrainParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    public DrainParticle(
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

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float p_233987_) {
        this.alpha = (float) (Math.cos((double) this.age / 20));
        Quaternionf quaternionf = new Quaternionf();
        quaternionf.rotationX((float) -Math.PI / 2);
        this.renderRotatedQuad(vertexConsumer, camera, quaternionf, p_233987_);
        quaternionf.rotationX((float) Math.PI / 2);
        this.renderRotatedQuad(vertexConsumer, camera, quaternionf, p_233987_);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
    }

    public record Provider(SpriteSet sprite) implements ParticleProvider<DrainParticleOptions> {
        public Particle createParticle(
                DrainParticleOptions drainParticleOptions,
                ClientLevel clientLevel,
                double p_233920_,
                double p_233921_,
                double p_233922_,
                double p_233923_,
                double p_233924_,
                double p_233925_
        ) {
            DrainParticle drainParticle = new DrainParticle(
                    clientLevel, p_233920_, p_233921_, p_233922_, p_233923_, p_233924_, p_233925_, this.sprite
            );
            drainParticle.quadSize = 2.5f;
            drainParticle.setParticleSpeed(p_233923_, p_233924_, p_233925_);
            drainParticle.setLifetime(20);
            return drainParticle;
        }
    }
}
