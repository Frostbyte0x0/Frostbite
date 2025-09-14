package org.exodusstudio.frostbite.common.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class SwirlingLeafParticle extends TextureSheetParticle {
    private float rollSpeed;
    private float flipYSpeed;
    private float flipYAngle;
    private float flipXSpeed;
    private float flipXAngle;

    public SwirlingLeafParticle(
            ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite
    ) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.friction = 0.93F;
        this.rollSpeed = random.nextFloat() * 0.2f - 0.1f;
        this.roll = (float) (random.nextFloat() * Math.PI * 2);
        this.flipYSpeed = random.nextFloat() * 0.75f - 0.375f;
        this.flipYAngle = (float) (random.nextFloat() * Math.PI * 2);
        this.flipXSpeed = random.nextFloat() * 0.75f - 0.375f;
        this.flipXAngle = (float) (random.nextFloat() * Math.PI * 2);
        this.hasPhysics = false;
        this.setSpriteFromAge(sprite);
    }

    @Override
    public int getLightColor(float p_233902_) {
        return 240;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        if (age >= 40 && alpha > 0.01f) {
            this.alpha = 1f - ((age + partialTicks) - 40) / 20;
        }
        this.oRoll = this.roll;
        this.roll += partialTicks * rollSpeed;

        Quaternionf quaternionf = new Quaternionf();
        if (this.roll != 0.0F) {
            quaternionf.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));
        }
        flipYAngle += partialTicks * flipYSpeed;
        flipXAngle += partialTicks * flipXSpeed;
        quaternionf.rotateLocalY(flipYAngle);
        quaternionf.rotateLocalX(flipXAngle);
        this.renderRotatedQuad(buffer, renderInfo, quaternionf, partialTicks);
        quaternionf.rotateX((float) Math.PI);
        this.renderRotatedQuad(buffer, renderInfo, quaternionf, partialTicks);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.rollSpeed > 0.05f) {
            this.rollSpeed *= 0.99f;
        }
        if (this.flipYSpeed > 0.1f) {
            this.flipYSpeed *= 0.99f;
        }
        if (this.flipXSpeed > 0.1f) {
            this.flipXSpeed *= 0.99f;
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(
                SimpleParticleType simpleParticleOption,
                ClientLevel clientLevel,
                double p_233920_,
                double p_233921_,
                double p_233922_,
                double p_233923_,
                double p_233924_,
                double p_233925_
        ) {
            RandomSource random = RandomSource.create();

            SwirlingLeafParticle swirlingLeafParticle = new SwirlingLeafParticle(
                    clientLevel, p_233920_, p_233921_, p_233922_, p_233923_, p_233924_, p_233925_, this.sprite
            );

            Vec3 vec3 = new Vec3(p_233923_, p_233924_, p_233925_);

            swirlingLeafParticle.setParticleSpeed(
                    vec3.x * 1.5f + random.nextFloat() * 0.2f,
                    vec3.y * 1.5f + random.nextFloat() * 0.2f,
                    vec3.z * 1.5f + random.nextFloat() * 0.2f);
            swirlingLeafParticle.setLifetime(60);
            return swirlingLeafParticle;
        }
    }
}
