package org.exodusstudio.frostbite.common.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.particle.options.TextParticleOption;

public class DamageParticle extends Particle {
    public static final ParticleRenderType RENDER_TYPE = new ParticleRenderType("FROSTBITE_TEXT", "FT");
    private static final float SCALE = 0.025f;
    private static final int FADE_START = 40;
    private static final int FADE_LENGTH = 10;
    private static final int LIGHT_COORDS = 15728880;

    private final FormattedCharSequence text;
    private final float width;
    private final float height;
    private final int color;

    public DamageParticle(
            ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, String text, int color
    ) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.friction = 0.8f;
        this.hasPhysics = true;
        this.gravity = 0;
        this.text = Component.literal(text).getVisualOrderText();
        this.width = Minecraft.getInstance().font.width(this.text);
        this.height = Minecraft.getInstance().font.lineHeight;
        this.color = color;
    }

    public void extract(TextParticleRenderState renderState, Camera camera, float partialTicks) {
        float currentAge = this.age + partialTicks;
        float alpha = currentAge <= FADE_START ? 1f : 1f - (currentAge - FADE_START) / FADE_LENGTH;
        if (alpha <= 0.01f) return;

        Vec3 cameraPos = camera.position();
        PoseStack poseStack = new PoseStack();
        poseStack.translate(
                Mth.lerp(partialTicks, this.xo, this.x) - cameraPos.x(),
                Mth.lerp(partialTicks, this.yo, this.y) - cameraPos.y(),
                Mth.lerp(partialTicks, this.zo, this.z) - cameraPos.z()
        );
        poseStack.mulPose(camera.rotation());
        poseStack.scale(SCALE, -SCALE, SCALE);

        renderState.add(poseStack, -this.width / 2f, -this.height / 2f, this.text,
                ARGB.color(alpha, this.color), LIGHT_COORDS);
    }

    @Override
    public ParticleRenderType getGroup() {
        return RENDER_TYPE;
    }

    public record Provider() implements ParticleProvider<TextParticleOption> {
        public Particle createParticle(
                TextParticleOption textParticleOption,
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
                    clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, textParticleOption.text(), textParticleOption.color()
            );

            damageParticle.setParticleSpeed(xSpeed, ySpeed, zSpeed);
            damageParticle.setLifetime(50);
            return damageParticle;
        }
    }
}
