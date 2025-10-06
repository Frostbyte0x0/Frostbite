package org.exodusstudio.frostbite.common.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ConfettiParticle extends TextureSheetParticle {
    private final float rotationSpeed;
    private final Vector3f rotationDir;
    private final RandomSource random = RandomSource.create();
    private final double ownOffset;
    private static double offset = 0;

    public ConfettiParticle(
            ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite
    ) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.friction = 0.96F;
        this.hasPhysics = true;
        this.rotationSpeed = random.nextFloat() * 0.5f + 0.5f;
        this.rotationDir = new Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat());
        this.setSpriteFromAge(sprite);
        this.ownOffset = getOffset();
    }

    private double getOffset() {
        offset += 0.00001f * random.nextDouble();
        offset %= 0.001f;
        return offset;
    }

    @Override
    public void tick() {
        super.tick();
        double newSpeed = this.yd - (double) this.age / 400;
        this.setParticleSpeed(this.xd, Math.clamp(newSpeed, -0.5, 2), this.zd);
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
        if (age > 60) {
            this.alpha = 1f - (float) (age - 60) / 40;
        }

        Quaternionf quaternionf = new Quaternionf();
        if (!this.onGround) {
            this.getFacingCameraMode().setRotation(quaternionf, camera, partialTicks);
            if (this.roll != 0.0F) {
                quaternionf.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));
            }
            quaternionf.rotateAxis(rotationSpeed * age, rotationDir);

            this.renderRotatedQuad(vertexConsumer, camera, quaternionf, partialTicks);
        } else {
            quaternionf.rotationX((float) -Math.PI / 2);
            this.renderRotatedQuad(vertexConsumer, camera, quaternionf, partialTicks);
            quaternionf.rotationX((float) Math.PI / 2);
            this.renderRotatedQuad(vertexConsumer, camera, quaternionf, partialTicks);
            this.setPos(this.x, this.y + ownOffset, this.z);
        }
    }

    @Override
    public int getLightColor(float p_233902_) {
        return 240;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

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
            ConfettiParticle confettiParticle = new ConfettiParticle(
                    clientLevel, p_233920_, p_233921_, p_233922_, p_233923_, p_233924_, p_233925_, this.sprite
            );

            confettiParticle.setColor(
                    colorParticleOption.getRed(),
                    colorParticleOption.getGreen(),
                    colorParticleOption.getBlue());

            confettiParticle.setSize(1.5f, 1.5f);
            confettiParticle.setParticleSpeed(p_233923_, p_233924_, p_233925_);
            confettiParticle.setLifetime(100);
            return confettiParticle;
        }
    }
}
