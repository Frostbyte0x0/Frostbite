package org.exodusstudio.frostbite.common.item.weapons.goat;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.item.weapons.ComboWeapon;
import org.exodusstudio.frostbite.common.item.weapons.SeriousAttackWeapon;
import org.exodusstudio.frostbite.common.item.weapons.ChargeAttackWeapon;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;
import org.exodusstudio.frostbite.common.registry.Renderables;
import org.exodusstudio.frostbite.common.rendering.RenderToolkit;
import org.exodusstudio.frostbite.common.util.Renderable;

public class BlueJadeKatanaItem extends SeriousAttackWeapon {
    public BlueJadeKatanaItem(Properties pProperties) {
        super(pProperties, 100, Renderables.BLUE_JADE_KATANA_CHARGE_ATTACK, 10,
                new ComboStep(2, 0.75f, 0.15f, 0.025f),
                new ComboStep(3, 0.65f, 0.15f, 0.025f),
                new ComboStep(4, 0.85f, 0.15f, 0.025f));
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 6.0,
                        AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }

    @Override
    public void doChargeAttack(Level level, LivingEntity user, InteractionHand usedHand) {
        Renderable.addRenderable(user, seriousAttack);
    }

    @Override
    public void doCooldownAttack(Level level, LivingEntity user, InteractionHand usedHand) {
        ComboWeapon.genericSweepAttack(level, user, usedHand);
        renderSlashes(user);
    }

    public static void renderSlashes(LivingEntity user) {
        if (!(user.level() instanceof ServerLevel world)) return;

        for (int i = 0; i <= 10; i++) {
            float angle = user.getYHeadRot() + (i * 20) - 60;
            float rad = angle * ((float) Math.PI / 180f);

            double d = -Mth.sin(rad);
            double e = Mth.cos(rad);
            double f = Mth.randomBetween(user.level().getRandom(), 0.5f, 2);

            world.sendParticles(ParticleRegistry.SLASH_PARTICLE.get(),
                    user.getX() + d, user.getY() + f, user.getZ() + e,
                    0, d, 0.0, e, 0.0);
        }
    }

    public static boolean shouldStopRendering(Renderable.RenderableContext context) {
        return context.secondsSinceStart() > 4;
    }

    public static void render(Renderable.RenderableContext context) {
        LivingEntity user = context.user();
        PoseStack poseStack = context.poseStack();
        SubmitNodeCollector output = context.output();

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        float partialTicks = mc.getDeltaTracker().getGameTimeDeltaPartialTick(!mc.level.tickRateManager().isEntityFrozen(user));
        poseStack.pushPose();
        poseStack.translate(
                -mc.gameRenderer.mainCamera().position().x,
                -mc.gameRenderer.mainCamera().position().y,
                -mc.gameRenderer.mainCamera().position().z);
        poseStack.translate(
                user.getPosition(partialTicks).x,
                user.getPosition(partialTicks).y + 2.0f,
                user.getPosition(partialTicks).z);
        output.submitCustomGeometry(
                poseStack,
                RenderTypes.entityTranslucent(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/environement/celestial/space_blue.png")),
                (pose, buffer) -> RenderToolkit.renderSphere(pose, buffer, 3 * context.secondsSinceStart() - 0.001f, 25));
//        output.submitCustomGeometry(poseStack, RenderTypes.endPortal(),
//                (pose, buffer) -> RenderToolkit.renderSphere(pose, buffer, 3 * context.secondsSinceStart(), 25));
        poseStack.popPose();
    }
}