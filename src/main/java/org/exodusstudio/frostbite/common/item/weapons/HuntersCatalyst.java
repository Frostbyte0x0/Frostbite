package org.exodusstudio.frostbite.common.item.weapons;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.component.HuntersCatalystData;
import org.exodusstudio.frostbite.common.registry.DamageTypeRegistry;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;
import org.exodusstudio.frostbite.common.util.Renderable;
import org.exodusstudio.frostbite.common.util.Util;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class HuntersCatalyst extends Item {
    public static final Identifier BEAM_LOCATION =
            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/hunters_catalyst/hunters_catalyst.png");
    public static final int DURATION = 50;

    public HuntersCatalyst(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        setData(player.getUseItem(), new HuntersCatalystData(DURATION));
        Renderable.addRenderable(player, "hunters_catalyst_charge_attack");
        return InteractionResult.PASS;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {
        chargePellet(level, livingEntity, itemStack, ticksRemaining);

        setData(itemStack, new HuntersCatalystData(ticksRemaining));
        if (ticksRemaining <= 0) {
            releaseUsing(itemStack, level, livingEntity, ticksRemaining);
        }
    }

    public static void chargePellet(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {
        Vec3 start = livingEntity.getEyePosition();
        Vec3 look = livingEntity.getLookAngle();
        Vec3 end = start.add(look.scale(10));

        AABB box = livingEntity.getBoundingBox().expandTowards(look.scale(10)).inflate(1.0D);

        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                level,
                livingEntity,
                start,
                end,
                box,
                (entity -> !entity.isSpectator() && entity.isPickable()),
                1
        );

        if (entityHit != null) {
            Entity entity = entityHit.getEntity();
            entity.hurt(livingEntity.damageSources().source(DamageTypeRegistry.HUNTERS_CATALYST, livingEntity), 1);
        }

        if (ticksRemaining <= 1) {
            if (livingEntity.level() instanceof ServerLevel serverLevel) {
                double radius = 0.5;
                int points = 32;
                int ringCount = 4;
                double spacing = 2.0;

                for (int r = 0; r < ringCount; r++) {
                    double distance = 1.0f + r * spacing;
                    Vec3 center = livingEntity.position().add(look.scale(distance));

                    for (int i = 0; i < points; i++) {
                        Quaternionf q = Util.getRotationQuaternionAroundLookVector(i, points, livingEntity, look);

                        Vec3 base = new Vec3(0, radius, 0);
                        Vector3f rotated = base.toVector3f();
                        rotated.rotate(q);

                        Vec3 offset = new Vec3(rotated.x(), rotated.y(), rotated.z());
                        Vec3 pos = center.add(offset);

                        Vec3 velocity = offset.scale(0.5);

                        serverLevel.sendParticles(
                                ParticleTypes.SOUL_FIRE_FLAME,
                                pos.x, pos.y + 1, pos.z,
                                0, velocity.x, velocity.y, velocity.z, 1
                        );
                    }
                }
            }
        }
    }

    @Override
    public boolean useOnRelease(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity user) {
        return DURATION;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
        return ItemUseAnimation.BOW;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    private static HuntersCatalystData getData(ItemStack itemStack) {
        return itemStack.getOrDefault(
                DataComponentTypeRegistry.HUNTERS_CATALYST.get(),
                new HuntersCatalystData(DURATION)
        );
    }

    private static void setData(ItemStack itemStack, HuntersCatalystData data) {
        itemStack.set(DataComponentTypeRegistry.HUNTERS_CATALYST.get(), data);
    }

    public static boolean shouldStopRendering(Renderable.RenderableContext context) {
        ItemStack stack = context.user().getUseItem();

        if (stack.getItem() != ItemRegistry.HUNTERS_CATALYST.get()) return true;

        int ticks = stack.getOrDefault(
                DataComponentTypeRegistry.HUNTERS_CATALYST.get(),
                new HuntersCatalystData(DURATION)
        ).ticksRemaining();

        return ticks <= 1;
    }

    public static void render(Renderable.RenderableContext context) {
        LivingEntity user = context.user();
        SubmitNodeCollector output = context.output();
        Minecraft mc = Minecraft.getInstance();

        Vec3 look = user.getLookAngle();
        Vec3 camera = Minecraft.getInstance().gameRenderer.mainCamera().position();
        float partialTicks = mc.getDeltaTracker().getGameTimeDeltaPartialTick(!mc.level.tickRateManager().isEntityFrozen(user));

        Vec3 beamPos = user.getPosition(partialTicks)
                .add(0, user.getEyeHeight() * 0.5, 0)
                .subtract(camera);

        Quaternionf rotation = new Quaternionf()
                .rotationTo(new Vector3f(0, 1, 0),
                        new Vector3f((float) look.x, (float) look.y, (float) look.z));

        PoseStack stack = new PoseStack();
        stack.pushPose();

        stack.translate(beamPos.x, beamPos.y, beamPos.z);
        stack.mulPose(rotation);

        int ticks = DURATION - getData(context.user().getUseItem()).ticksRemaining();
        float progress = (ticks + partialTicks) / DURATION;
        float radius = progress < 0.95f ? 0.5f * (1 - progress) : (float) (150 * Math.pow(progress - 0.95, 2) + 0.025);
        int r = (int) Mth.lerp(progress, 242, 240);
        int g = (int) Mth.lerp(progress, 195, 26);
        int b = (int) Mth.lerp(progress, 41, 119);

        submitBeaconBeam(
            stack,
            output,
            BEAM_LOCATION,
            1,
            user.level().getGameTime() + partialTicks,
            0,
            10,
            ARGB.color(r, g, b),
            radius,
            0
            );

        stack.popPose();
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
