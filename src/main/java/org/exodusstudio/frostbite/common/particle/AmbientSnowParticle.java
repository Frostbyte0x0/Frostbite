package org.exodusstudio.frostbite.common.particle;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;

public class AmbientSnowParticle extends BaseAshSmokeParticle {
    public AmbientSnowParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float quadSizeMultiplier, SpriteSet sprites) {
        super(level, x, y, z, 1, 1, 1, xSpeed, ySpeed, zSpeed,
                quadSizeMultiplier, sprites, 0.0F, 40, 0.0125F, false);
        this.rCol = (float) ARGB.red(12235202) / 255.0F;
        this.gCol = (float) ARGB.green(12235202) / 255.0F;
        this.bCol = (float) ARGB.blue(12235202) / 255.0F;
        this.quadSize *= 1.5f;
        this.xd = xSpeed * 1.5; //+ 0.075 * (level.getRandom().nextFloat() + 0.5f);
        this.yd = ySpeed * 1.5; //- 0.075 * (level.getRandom().nextFloat() + 0.5f);
        this.zd = zSpeed * 1.5; //+ 0.075 * (level.getRandom().nextFloat() + 0.5f);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.onGround) {
            this.remove();
        }
    }

    @Override
    public void extract(QuadParticleRenderState reusedState, Camera camera, float partialTick) {
        if (age + partialTick >= lifetime) {
            this.remove();
            return;
        }
        if (age > lifetime - 20) {
            this.alpha = (lifetime - age - partialTick) / 20;
        }

        super.extract(reusedState, camera, partialTick);
    }

    @Override
    public int getLightColor(float p_233902_) {
        return 240;
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(
                SimpleParticleType p_446679_,
                ClientLevel p_108526_,
                double p_108527_,
                double p_108528_,
                double p_108529_,
                double xd,
                double yd,
                double zd,
                RandomSource source
        ) {
            //double d0 = source.nextFloat() * -1.9 * source.nextFloat() * 0.1;
            //double d1 = source.nextFloat() * -0.5F * source.nextFloat() * 0.1 * 5.0F;
            //double d2 = source.nextFloat() * -1.9 * source.nextFloat() * 0.1;
            //return new AmbientSnowParticle(p_108526_, p_108527_, p_108528_, p_108529_, d0, d1, d2, 1.0F, this.sprites);
            return new AmbientSnowParticle(p_108526_, p_108527_, p_108528_, p_108529_, xd, yd, zd, 1.0F, this.sprites);
        }
    }
}
