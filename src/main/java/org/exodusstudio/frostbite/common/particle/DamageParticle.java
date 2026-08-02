package org.exodusstudio.frostbite.common.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import org.exodusstudio.frostbite.common.particle.options.StringParticleOption;

public class DamageParticle extends SingleQuadParticle {
    private final String text;

    public DamageParticle(
            ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite, String text
    ) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite.first());
        this.friction = 0.8f;
        this.hasPhysics = true;
        this.gravity = 0;
        this.text = text;
        this.setSpriteFromAge(sprite);
    }

    @Override
    public void extract(QuadParticleRenderState reusedState, Camera camera, float partialTicks) {
        if (age + partialTicks >= lifetime) {
            this.remove();
            return;
        }
        if (age >= 40 && alpha > 0.01f) {
            this.alpha = 1f - ((age + partialTicks) - 40) / 10;
        }

        SubmitNodeStorage collector = new SubmitNodeStorage();

        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.translate(
                this.x,
                this.y,
                this.z
        );
//        float particleX = (float) (Mth.lerp(partialTicks, this.xo, this.x) - camera.position().x());
//        float particleY = (float) (Mth.lerp(partialTicks, this.yo, this.y) - camera.position().y());
//        float particleZ = (float) (Mth.lerp(partialTicks, this.zo, this.z) - camera.position().z());
//        poseStack.translate(
//                particleX,
//                particleY,
//                particleZ
//        );
        poseStack.mulPose(camera.rotation());
        collector.submitText(poseStack, 0, 0, Component.literal(this.text).getVisualOrderText(), false,
                Font.DisplayMode.NORMAL, 15728880,
                ARGB.colorFromFloat(this.alpha, this.rCol, this.gCol, this.bCol), 0, -10066330);

        reusedState.submit(collector, Minecraft.getInstance().levelRenderer.levelRenderState.cameraRenderState);
//        Minecraft.getInstance().gameRenderer.featureRenderDispatcher().renderAllFeatures(collector);

        super.extract(reusedState, camera, partialTicks);
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public record Provider(SpriteSet sprite) implements ParticleProvider<StringParticleOption> {
        public Particle createParticle(
                StringParticleOption stringParticleOption,
                ClientLevel clientLevel,
                double x,
                double y,
                double z,
                double xSpeed,
                double ySpeed,
                double zSpeed,
                RandomSource randomSource
        ) {
            DamageParticle damageParticle = new DamageParticle(
                    clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, this.sprite, stringParticleOption.text()
            );

            damageParticle.setSize(1.5f, 1.5f);
            damageParticle.setParticleSpeed(xSpeed, ySpeed, zSpeed);
            damageParticle.setLifetime(50);
            return damageParticle;
        }
    }
}
