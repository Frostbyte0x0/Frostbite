package org.exodusstudio.frostbite.common.particle;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ButterflyParticle extends SingleQuadParticle {
    int atlasSize;
    private float angleFacing;
    private final float angleTilting;
    private final float flapSpeed;
    private final SpriteSet spriteSet;

    public ButterflyParticle(
            ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite
    ) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite.first());
        this.friction = 0.9F;
        this.hasPhysics = false;
        this.spriteSet = sprite;
        setFirstSprite();
        this.atlasSize = (int) (4 / this.uvShrinkRatio());
        this.angleTilting = Math.abs(random.nextFloat()) * (float) Math.PI / 4;
        this.angleFacing = random.nextFloat() * (float) Math.PI * 2;
        this.flapSpeed = random.nextFloat() * 0.5f + 0.5f;
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    public void extract(QuadParticleRenderState reusedState, Camera camera, float partialTicks) {
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
        this.extractRotatedQuad(reusedState, camera, quaternionf, partialTicks);

        setSecondSprite();
        quaternionf.rotationXYZ((float) Math.PI / 2, 0, (float) Math.PI);
        quaternionf.rotateAxis(-angleTilting, new Vector3f(1, 0, 0));
        quaternionf.rotateLocalY(angleFacing);
        quaternionf.rotateY(flapAngle);
        this.extractRotatedQuad(reusedState, camera, quaternionf, partialTicks);

        // Second wing
        flapAngle = -Math.sin((age + partialTicks) * flapSpeed);

        setFirstSprite();
        quaternionf.rotationX((float) -Math.PI / 2);
        quaternionf.rotateAxis(angleTilting, new Vector3f(1, 0, 0));
        quaternionf.rotateLocalY(angleFacing);
        quaternionf.rotateAxis((float) Math.PI, new Vector3f(0, 1, 0));
        quaternionf.rotateY(flapAngle);
        this.extractRotatedQuad(reusedState, camera, quaternionf, partialTicks);

        setSecondSprite();
        quaternionf.rotationXYZ((float) Math.PI / 2, 0, (float) Math.PI);
        quaternionf.rotateAxis(-angleTilting, new Vector3f(1, 0, 0));
        quaternionf.rotateLocalY(angleFacing);
        quaternionf.rotateAxis((float) Math.PI, new Vector3f(0, 1, 0));
        quaternionf.rotateY(flapAngle);
        this.extractRotatedQuad(reusedState, camera, quaternionf, partialTicks);
    }

    private float atlasSize() {
        float f = (float)this.sprite.contents().width() / (sprite.getU1() - sprite.getU0());
        float f1 = (float)this.sprite.contents().height() / (sprite.getV1() - sprite.getV0());
        return Math.max(f1, f);
    }

    public float uvShrinkRatio() {
        return 4.0F / this.atlasSize();
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
        public Particle createParticle(
                SimpleParticleType simpleParticleOption,
                ClientLevel clientLevel,
                double p_233920_,
                double p_233921_,
                double p_233922_,
                double p_233923_,
                double p_233924_,
                double p_233925_,
                RandomSource source
        ) {
            ButterflyParticle butterflyParticle = new ButterflyParticle(
                    clientLevel, p_233920_, p_233921_, p_233922_, p_233923_, p_233924_, p_233925_, this.sprite
            );

            butterflyParticle.setParticleSpeed(p_233923_, p_233924_, p_233925_);
            butterflyParticle.setLifetime(100);
            return butterflyParticle;
        }
    }
}
