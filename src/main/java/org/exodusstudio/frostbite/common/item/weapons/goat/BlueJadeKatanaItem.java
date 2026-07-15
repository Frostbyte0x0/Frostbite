package org.exodusstudio.frostbite.common.item.weapons.goat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ARGB;
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


        float yaw = player.getYRot() * ((float) Math.PI / 180f);

        BlockPos pos = BlockPos.containing(player.position().add(
                -Mth.sin(yaw) * 1.4, player.getEyeHeight() / 2.0f, Mth.cos(yaw) * 1.4f
        ));

        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class, new AABB(pos).inflate(3.0, 1.0, 3.0),
                (entity) -> entity != player);

//        stack.damage(1, user, EquipmentSlot.MAINHAND);

        targets.forEach(target -> {

            double distance = player.distanceToSqr(target);

            if (distance > 6.0 && distance < 36.0) {
                if (!(target instanceof ArmorStand)) {
                    target.push(
                            0.4,
                            Mth.sin(player.getYHeadRot() * 0.017453292F),
                            -Mth.cos(player.getYHeadRot() * 0.017453292F)
                    );
                }

                target.hurt(target.damageSources().playerAttack(player), 5.5f);
            }
        });
        player.swing(usedHand);
        level.playSound(
                null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS,
                1.0f,
                0.9f + level.getRandom().nextFloat() * 0.2f
        );
        renderSlashes(player);
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
        Minecraft mc = Minecraft.getInstance();

        Vec3 look = user.getLookAngle();
        Vec3 camera = Minecraft.getInstance().gameRenderer.mainCamera().position();
        float partialTicks = mc.getDeltaTracker().getGameTimeDeltaPartialTick(!mc.level.tickRateManager().isEntityFrozen(user));

        Vec3 beamPos = user.getPosition(partialTicks)
                .add(0, user.getEyeHeight() * 0.75, 0)
//                .add(look.scale(1.5))
                .subtract(camera);

        Quaternionf rotation = new Quaternionf()
                .rotationTo(new Vector3f(0, 1, 0),
                        new Vector3f((float) look.x, (float) look.y, (float) look.z));

        PoseStack stack = new PoseStack();
        stack.pushPose();

        stack.translate(beamPos.x, beamPos.y, beamPos.z);
        stack.mulPose(rotation);

        submitBeaconBeam(
                stack,
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

        stack.popPose();

//        Minecraft mc = Minecraft.getInstance();
//        if (mc.level == null) return;
//

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

    public static void submitBeaconBeam(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, Identifier beamLocation, float scale, float animationTime, int beamStart, int height, int color, float solidBeamRadius, float beamGlowRadius) {
        int beamEnd = beamStart + height;
        poseStack.pushPose();
        float scroll = height < 0 ? animationTime : -animationTime;
        float texVOff = Mth.frac(scroll * 0.2F - (float)Mth.floor(scroll * 0.1F));
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(animationTime * 2.25F - 45.0F));
        float wnx;
        float enz;
        float wsx = -solidBeamRadius;
        float esz = -solidBeamRadius;
        float vv2 = -1.0F + texVOff;
        float vv1 = (float)height * scale * (0.5F / solidBeamRadius) + vv2;
        float finalWsx = wsx;
        float finalVv = vv2;
        float finalVv1 = vv1;
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.beaconBeam(beamLocation, false), (pose, buffer) -> renderPart(pose, buffer, color, beamStart, beamEnd, 0.0F, solidBeamRadius, solidBeamRadius, 0.0F, finalWsx, 0.0F, 0.0F, esz, 0.0F, 1.0F, finalVv1, finalVv));
        poseStack.popPose();
        wnx = -beamGlowRadius;
        float wnz = -beamGlowRadius;
        enz = -beamGlowRadius;
        wsx = -beamGlowRadius;
        vv2 = -1.0F + texVOff;
        vv1 = (float)height * scale + vv2;
        float finalWsx1 = wsx;
        float finalVv2 = vv2;
        float finalVv3 = vv1;
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.beaconBeam(beamLocation, true), (pose, buffer) -> renderPart(pose, buffer, ARGB.color(32, color), beamStart, beamEnd, wnx, wnz, beamGlowRadius, enz, finalWsx1, beamGlowRadius, beamGlowRadius, beamGlowRadius, 0.0F, 1.0F, finalVv3, finalVv2));
        poseStack.popPose();
    }

    private static void renderPart(PoseStack.Pose pose, VertexConsumer builder, int color, int beamStart, int beamEnd, float wnx, float wnz, float enx, float enz, float wsx, float wsz, float esx, float esz, float uu1, float uu2, float vv1, float vv2) {
        renderQuad(pose, builder, color, beamStart, beamEnd, wnx, wnz, enx, enz, uu1, uu2, vv1, vv2);
        renderQuad(pose, builder, color, beamStart, beamEnd, esx, esz, wsx, wsz, uu1, uu2, vv1, vv2);
        renderQuad(pose, builder, color, beamStart, beamEnd, enx, enz, esx, esz, uu1, uu2, vv1, vv2);
        renderQuad(pose, builder, color, beamStart, beamEnd, wsx, wsz, wnx, wnz, uu1, uu2, vv1, vv2);
    }

    private static void renderQuad(PoseStack.Pose pose, VertexConsumer builder, int color, int beamStart, int beamEnd, float wnx, float wnz, float enx, float enz, float uu1, float uu2, float vv1, float vv2) {
        addVertex(pose, builder, color, beamEnd, wnx, wnz, uu2, vv1);
        addVertex(pose, builder, color, beamStart, wnx, wnz, uu2, vv2);
        addVertex(pose, builder, color, beamStart, enx, enz, uu1, vv2);
        addVertex(pose, builder, color, beamEnd, enx, enz, uu1, vv1);
    }

    private static void addVertex(PoseStack.Pose pose, VertexConsumer builder, int color, int y, float x, float z, float u, float v) {
        builder.addVertex(pose, x, (float)y, z).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(pose, 0.0F, 1.0F, 0.0F);
    }
}