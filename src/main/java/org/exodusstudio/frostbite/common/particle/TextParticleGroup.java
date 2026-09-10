package org.exodusstudio.frostbite.common.particle;

import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;
import net.minecraft.world.phys.Vec3;

public class TextParticleGroup extends ParticleGroup<DamageParticle> {
    private final TextParticleRenderState renderState = new TextParticleRenderState();

    public TextParticleGroup(ParticleEngine engine) {
        super(engine);
    }

    @Override
    public ParticleGroupRenderState extractRenderState(Frustum frustum, Camera camera, float partialTickTime) {
        for (DamageParticle particle : this.particles) {
            Vec3 pos = particle.getPos();
            if (frustum.pointInFrustum(pos.x, pos.y, pos.z)) {
                particle.extract(this.renderState, camera, partialTickTime);
            }
        }

        return this.renderState;
    }
}
