package org.exodusstudio.frostbite.common.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class SlashParticle extends SingleQuadParticle {
    private final SpriteSet sprites;


    public SlashParticle(ClientLevel level, double x, double y, double z, double size, SpriteSet sprites) {
        super(level, x, y, z, 0.0F, 0.0F, 0.0F, sprites.first());
        this.sprites = sprites;
        this.lifetime = 8;
        float col = this.random.nextFloat() * 0.6F + 0.4F;
        this.rCol = col;
        this.gCol = col;
        this.bCol = col;
        this.quadSize = 1.0F - (float)size * 0.5F;
        this.setSpriteFromAge(sprites);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;

        this.oRoll = this.random.nextFloat() * (float)Math.PI * 2.0f;
        this.roll = oRoll;
    }

    @Override
    public int getLightCoords(float a) {
        return 15728880;
    }

    @Override
    public void tick() {
        super.tick();
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
        }
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return Layer.OPAQUE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new SlashParticle(level, x, y, z, xAux, this.sprites);
        }
    }
}
