package org.exodusstudio.frostbite.common.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
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
        this.lifetime = 100;
        this.dir = dir.normalize(0.01f);
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
        float f = (float)this.age / (float)this.lifetime;
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

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        super.render(buffer, renderInfo, partialTicks);
        if (age > 60) {
            this.alpha = 1f - (float) (age - 60) / 40;
        }
    }

    public void tick() {
        this.xd += dir.x*dir.x * 3;
        if (!this.onGround) {
            this.yd += dir.y*dir.y * 3;
            this.yd -= 0.004;
        }
        this.zd += dir.z*dir.z * 3;

        super.tick();
    }

    public record Provider(SpriteSet sprite) implements ParticleProvider<Vec3ParticleOptions> {
        public Particle createParticle(Vec3ParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            IcyBreathParticle particle = new IcyBreathParticle(level, x, y, z, xSpeed, ySpeed, zSpeed,
                    type.vec3().scale(0.01).toVector3f());
            particle.pickSprite(this.sprite);

            particle.setColor(
                    particle.random.nextInt(80) + 80,
                    particle.random.nextInt(30) + 30,
                    particle.random.nextInt(80) + 150
            );
            return particle;
        }
    }
}
