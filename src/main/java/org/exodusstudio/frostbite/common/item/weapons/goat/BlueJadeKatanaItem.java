package org.exodusstudio.frostbite.common.item.weapons.goat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.item.weapons.ChargeAttackWeapon;
import org.exodusstudio.frostbite.common.registry.AttachementRegistry;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;
import org.exodusstudio.frostbite.common.rendering.RenderToolkit;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class BlueJadeKatanaItem extends ChargeAttackWeapon {
    public BlueJadeKatanaItem(Properties pProperties) {
        super(pProperties, "blue_jade_katana_charge_attack");
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        startChargeAttack(player);


//        float yaw = player.getYRot() * ((float) Math.PI / 180f);
//
//        BlockPos pos = BlockPos.containing(player.position().add(
//                -Mth.sin(yaw) * 1.4, player.getEyeHeight() / 2.0f, Mth.cos(yaw) * 1.4f
//        ));
//
//        List<LivingEntity> targets = level.getEntitiesOfClass(
//                LivingEntity.class, new AABB(pos).inflate(3.0, 1.0, 3.0),
//                (entity) -> entity != player);
//
////        stack.damage(1, user, EquipmentSlot.MAINHAND);
//
//        targets.forEach(target -> {
//
//            double distance = player.distanceToSqr(target);
//
//            if (distance > 6.0 && distance < 36.0) {
//                if (!(target instanceof ArmorStand)) {
//                    target.push(
//                            0.4,
//                            Mth.sin(player.getYHeadRot() * 0.017453292F),
//                            -Mth.cos(player.getYHeadRot() * 0.017453292F)
//                    );
//                }
//
//                target.hurt(target.damageSources().playerAttack(player), 5.5f);
//            }
//        });
//        player.swing(usedHand);
//        level.playSound(
//                null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS,
//                1.0f,
//                0.9f + level.getRandom().nextFloat() * 0.2f
//        );
//        renderSlashes(player);
        return InteractionResult.SUCCESS;
    }




    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 6.0,
                        AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }

    public static void renderSlashes(Player player) {
        if (!(player.level() instanceof ServerLevel world)) return;

        for (int i = 0; i <= 10; i++) {
            float angle = player.getYHeadRot() + (i * 20) - 60;
            float rad = angle * ((float) Math.PI / 180f);

            double d = -Mth.sin(rad);
            double e = Mth.cos(rad);
            double f = Mth.randomBetween(player.level().getRandom(), 0.5f, 2);

            world.sendParticles(ParticleRegistry.SLASH_PARTICLE.get(),
                    player.getX() + d, player.getY() + f, player.getZ() + e,
                    0, d, 0.0, e, 0.0);
        }
    }

    public static void renderChargeAttack(LivingEntity user, PoseStack poseStack, LevelRenderState levelRenderState, SubmitNodeCollector output) {

        Vec3 look = user.getLookAngle();
        Vec3 camera = Minecraft.getInstance().gameRenderer.mainCamera().position();

        Vec3 beamPos = user.position()
                .add(0, user.getEyeHeight() * 0.35f, 0)
                .subtract(camera);

        Quaternionf rotation = new Quaternionf()
                .rotationTo(new Vector3f(0, 1, 0),
                        new Vector3f((float) look.x, (float) look.y, (float) look.z));

        poseStack.pushPose();

        poseStack.translate(beamPos.x, beamPos.y, beamPos.z);

        poseStack.mulPose(rotation);

        BeaconRenderer.submitBeaconBeam(
                poseStack,
                output,
                BeaconRenderer.BEAM_LOCATION,
                1.0f,
                user.level().getGameTime(),
                0,
                10,
                0xff00ffff,
                0.15f,
                0.25f
        );

        poseStack.popPose();

//        Minecraft mc = Minecraft.getInstance();
//        if (mc.level == null) return;
//
//        float partialTicks = mc.getDeltaTracker().getGameTimeDeltaPartialTick(!mc.level.tickRateManager().isEntityFrozen(user));
//        float t = (levelRenderState.gameTime + partialTicks - user.getData(AttachementRegistry.CHARGE_ATTACK_START)) / 20f;
//        poseStack.pushPose();
//        poseStack.translate(
//                -mc.gameRenderer.mainCamera().position().x,
//                -mc.gameRenderer.mainCamera().position().y,
//                -mc.gameRenderer.mainCamera().position().z);
//        poseStack.translate(
//                user.getPosition(partialTicks).x,
//                user.getPosition(partialTicks).y + 2.0f,
//                user.getPosition(partialTicks).z);
//        output.submitCustomGeometry(
//                poseStack,
//                RenderTypes.entityTranslucent(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/environement/celestial/space_blue.png")),
//                (pose, buffer) -> RenderToolkit.renderSphere(pose, buffer, 3 * t - 0.001f, 25));
////        output.submitCustomGeometry(poseStack, RenderTypes.endPortal(),
////                (pose, buffer) -> RenderToolkit.renderSphere(pose, buffer, 3 * t, 25));
//        poseStack.popPose();
//        if (t > 4) {
//            user.setData(AttachementRegistry.CURRENT_CHARGE_ATTACK, "");
//        }
    }
}