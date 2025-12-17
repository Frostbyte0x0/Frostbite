package org.exodusstudio.frostbite.common.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

public class ButterflyParticle extends SingleQuadParticle {
    int atlasSize;
    private float angleFacing;
    private final float angleTilting;
    private final float flapSpeed;
    private final SpriteSet spriteSet;

    public ButterflyParticle(
            ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite
    ) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.friction = 0.9F;
        this.hasPhysics = false;
        this.spriteSet = sprite;
        setFirstSprite();
        this.atlasSize = (int) (4 / this.sprite.uvShrinkRatio());
        this.angleTilting = Math.abs(random.nextFloat()) * (float) Math.PI / 4;
        this.angleFacing = random.nextFloat() * (float) Math.PI * 2;
        this.flapSpeed = random.nextFloat() * 0.5f + 0.5f;
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
        if (age >= 60 && alpha > 0.01f) {
            this.alpha = 1f - ((age + partialTicks) - 60) / 40;
        }

        Quaternionf quaternionf = new Quaternionf();

        // First wing
        float flapAngle = Math.sin((age + partialTicks) * flapSpeed);

        setFirstSprite();
        quaternionf.rotationX((float) -Math.PI / 2);
        quaternionf.rotateAxis(angleTilting, new Vector3f(1, 0, 0));
        quaternionf.rotateLocalY(angleFacing);
        quaternionf.rotateY(flapAngle);
        this.renderRotatedQuad(vertexConsumer, camera, quaternionf, partialTicks);

        setSecondSprite();
        quaternionf.rotationXYZ((float) Math.PI / 2, 0, (float) Math.PI);
        quaternionf.rotateAxis(-angleTilting, new Vector3f(1, 0, 0));
        quaternionf.rotateLocalY(angleFacing);
        quaternionf.rotateY(flapAngle);
        this.renderRotatedQuad(vertexConsumer, camera, quaternionf, partialTicks);

        // Second wing
        flapAngle = -Math.sin((age + partialTicks) * flapSpeed);

        setFirstSprite();
        quaternionf.rotationX((float) -Math.PI / 2);
        quaternionf.rotateAxis(angleTilting, new Vector3f(1, 0, 0));
        quaternionf.rotateLocalY(angleFacing);
        quaternionf.rotateAxis((float) Math.PI, new Vector3f(0, 1, 0));
        quaternionf.rotateY(flapAngle);
        this.renderRotatedQuad(vertexConsumer, camera, quaternionf, partialTicks);

        setSecondSprite();
        quaternionf.rotationXYZ((float) Math.PI / 2, 0, (float) Math.PI);
        quaternionf.rotateAxis(-angleTilting, new Vector3f(1, 0, 0));
        quaternionf.rotateLocalY(angleFacing);
        quaternionf.rotateAxis((float) Math.PI, new Vector3f(0, 1, 0));
        quaternionf.rotateY(flapAngle);
        this.renderRotatedQuad(vertexConsumer, camera, quaternionf, partialTicks);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age == 1) {
            this.yd = (flapSpeed * 0.7f);
        }

        this.xd = -(Math.sin(angleFacing) + (flapSpeed - 0.5f) * 0.5f) * 0.05f;
        if (this.age > 30)
            this.yd = -Math.sin(angleTilting) * 0.05f;
        else
            this.angleFacing += 0.05f;
        this.zd = -(Math.cos(angleFacing) + (flapSpeed - 0.5f) * 0.5f) * 0.05f;
    }

    private void setFirstSprite() {
        this.sprite = spriteSet.get(0, this.lifetime);
    }

    private void setSecondSprite() {
        this.sprite = spriteSet.get(this.lifetime, this.lifetime);
    }

    @Override
    public int getLightColor(float p_233902_) {
        return 240;
    }

    public record Provider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @Nullable Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double v, double v1, double v2, double v3, double v4, double v5, RandomSource randomSource) {
            ButterflyParticle butterflyParticle = new ButterflyParticle(
                    clientLevel, v, v1, v2, v3, v4, v5, this.sprite
            );

            butterflyParticle.setParticleSpeed(v3, v4, v5);
            butterflyParticle.setLifetime(100);
            return butterflyParticle;
        }
    }
}
