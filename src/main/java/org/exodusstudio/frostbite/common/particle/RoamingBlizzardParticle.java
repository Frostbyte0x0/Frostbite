package org.exodusstudio.frostbite.common.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.util.Mth;
import org.exodusstudio.frostbite.common.entity.custom.RoamingBlizzardEntity;

public class RoamingBlizzardParticle extends TextureSheetParticle {
    public RoamingBlizzardParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
        super(level, x, y, z);
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.x = x;
        this.y = y;
        this.z = z;
        this.quadSize = (this.random.nextFloat() * 0.3F + 0.5F) * 0.2f;
        this.lifetime = 20;
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().move(x, y, z));
        this.setLocationFromBoundingbox();
    }

    public float getQuadSize(float scaleFactor) {
        float f = Math.min(Mth.lerp((float) age / lifetime, 0, 15), 1);
        return this.quadSize * f;
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

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.move(xd, yd, zd);

        if (this.age++ >= this.lifetime) {
            this.remove();
        }

        if (level.getEntitiesOfClass(RoamingBlizzardEntity.class, this.getBoundingBox().inflate(0.25f)).isEmpty()) {
            this.quadSize *= 0.77f;
            if (quadSize < 0.1f) {
                this.remove();
            }
        }
    }

    public record Provider(SpriteSet sprite) implements ParticleProvider<ColorParticleOption> {

        public Particle createParticle(ColorParticleOption type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            RoamingBlizzardParticle particle = new RoamingBlizzardParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(this.sprite);

            particle.setColor(
                    type.getRed(),
                    type.getGreen(),
                    type.getBlue());
            return particle;
        }
    }
}
