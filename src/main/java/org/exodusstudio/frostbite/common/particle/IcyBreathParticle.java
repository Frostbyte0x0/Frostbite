package org.exodusstudio.frostbite.common.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import org.exodusstudio.frostbite.common.particle.options.Vec3ParticleOption;
import org.joml.Vector3f;

public class IcyBreathParticle extends TextureSheetParticle {
    Vector3f dir;

    public IcyBreathParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, Vector3f dir) {
        super(level, x, y, z);
        this.hasPhysics = true;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.x = x;
        this.y = y;
        this.z = z;
        this.quadSize = (this.random.nextFloat() * 0.3F + 0.5F) * 0.2f;
        this.dir = dir;
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().move(x, y, z));
        this.setLocationFromBoundingbox();
    }

    public int getLightColor(float partialTick) {
        int i = super.getLightColor(partialTick);
        float f = (float) age / (float) lifetime;
        f *= f;
        f *= f;
        int j = i & 255;
        int k = i >> 16 & 255;
        k += (int)(f * 15.0F * 16.0F);
        if (k > 240) {
            k = 240;
        }

        return j | k << 16;
    }

    public void tick() {
        if (age > 40) {
            setAlpha(1 - (float) (age - 40) / 10);
        }

        xd += dir.x;
        if (!onGround) {
            yd += dir.y;
            yd -= 0.001;
        } else {
            yd = 0;
        }
        zd += dir.z;

        super.tick();
    }

    public record Provider(SpriteSet sprite) implements ParticleProvider<Vec3ParticleOption> {
        public Particle createParticle(Vec3ParticleOption type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            IcyBreathParticle particle = new IcyBreathParticle(level, x, y, z, xSpeed, ySpeed, zSpeed,
                    type.vec3f());
            particle.pickSprite(sprite);

            particle.setLifetime(50);
            particle.setColor(
                    (particle.random.nextInt(40) + 20) / 255f,
                    (particle.random.nextInt(40) + 20) / 255f,
                    (particle.random.nextInt(80) + 110) / 255f
            );
            return particle;
        }
    }
}
