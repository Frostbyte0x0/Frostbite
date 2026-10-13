package org.exodusstudio.frostbite.common.item.weapons;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.client.overlays.FlashbangOverlay;
import org.exodusstudio.frostbite.common.registry.DamageTypeRegistry;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.exodusstudio.frostbite.common.registry.SoundRegistry;
import org.exodusstudio.frostbite.common.component.MapStringIntData;
import org.exodusstudio.frostbite.common.entity.custom.projectiles.ImpactPelletEntity;
import org.exodusstudio.frostbite.common.util.Renderable;
import org.exodusstudio.frostbite.common.util.helpers.Vec3Helper;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.function.Consumer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class HuntersCatalyst extends Item {
    public static final Identifier BEAM_LOCATION =
            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/hunters_catalyst/hunters_catalyst.png");
    public static final int DURATION = 50;
    private static final String FUEL = "catalyst_fuel";
    private static final String PELLET = "catalyst_pellet";
    private static final int NONE = 0, CHARGE = 1, IMPACT = 2, FLASH = 3;
    private static final int FLASH_BLINDNESS_TICKS = 80;
    private static final int FLASH_DEAGGRO_TICKS = 120;
    private static final Map<UUID, Long> FLASH_DEAGGRO = new HashMap<>();

    public HuntersCatalyst(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay,
                                Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.translatable(hasFuel(stack)
                ? "tooltip.frostbite.catalyst.loaded"
                : "tooltip.frostbite.catalyst.empty").withStyle(hasFuel(stack)
                ? ChatFormatting.GREEN : ChatFormatting.GRAY));

        int pellet = getPellet(stack);
        if (pellet != NONE) {
            String key = switch (pellet) {
                case CHARGE -> "item.frostbite.charge_pellet";
                case IMPACT -> "item.frostbite.impact_pellet";
                case FLASH -> "item.frostbite.flash_pellet";
                default -> "item.frostbite.charge_pellet";
            };
            tooltipAdder.accept(Component.translatable("tooltip.frostbite.catalyst.pellet",
                    Component.translatable(key)).withStyle(ChatFormatting.AQUA));
        } else {
            tooltipAdder.accept(Component.translatable("tooltip.frostbite.catalyst.no_pellet")
                    .withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack catalyst = player.getItemInHand(hand);
        if (!hasFuel(catalyst) || getPellet(catalyst) == NONE) return InteractionResult.PASS;
        if (level instanceof ServerLevel server) deaggroNearbyMobs(server, player);

        int pellet = getPellet(catalyst);
        if (pellet == IMPACT) {
            if (level instanceof ServerLevel server) {
                impactPellet(server, player);
                clearLoadAndSync(player, hand, catalyst);
            }
            return InteractionResult.SUCCESS;
        }
        if (pellet == FLASH) {
            if (level instanceof ServerLevel server) {
                flashPellet(server, player);
                clearLoadAndSync(player, hand, catalyst);
            }
            if (level.isClientSide()) FlashbangOverlay.trigger(FLASH_BLINDNESS_TICKS);
            return InteractionResult.SUCCESS;
        }

        player.startUsingItem(hand);
        setTicksRemaining(catalyst, DURATION);
        Renderable.addRenderable(player, "hunters_catalyst_charge_attack");
        player.playSound(SoundRegistry.CATALYST_CHARGE.get(), 1.0F, 1.0F);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {
        if (getPellet(itemStack) == CHARGE) {
            chargePellet(level, livingEntity, itemStack, ticksRemaining);
            if (ticksRemaining <= 1) {
                if (level instanceof ServerLevel server) burstCharge(server, livingEntity, itemStack);
                livingEntity.stopUsingItem();
            }
        }

        setTicksRemaining(itemStack, ticksRemaining);
        if (ticksRemaining <= 0) releaseUsing(itemStack, level, livingEntity, ticksRemaining);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        super.inventoryTick(stack, level, entity, slot);
        if (entity instanceof LivingEntity livingEntity
                && livingEntity.isUsingItem()
                && livingEntity.getUseItem().getItem() == this
                && getPellet(livingEntity.getUseItem()) == CHARGE) {
            ItemStack activeStack = livingEntity.getUseItem();
            int remaining = livingEntity.getUseItemRemainingTicks();
            chargePellet(level, livingEntity, activeStack, remaining);
            if (remaining <= 1) burstCharge(level, livingEntity, activeStack);
        }
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity user, int timeLeft) {
        int pellet = getPellet(stack);
        if (pellet == FLASH && level instanceof ServerLevel server) flashPellet(server, user);
        if (pellet == FLASH && level.isClientSide()) FlashbangOverlay.trigger(FLASH_BLINDNESS_TICKS);
        if (pellet == CHARGE && level instanceof ServerLevel server) burstCharge(server, user, stack);
        if (level instanceof ServerLevel) clearLoadAndSync(user, stack);
        return true;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack catalyst, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action != ClickAction.SECONDARY || other.isEmpty()) return false;
        if (!loadAmmo(catalyst, other)) return false;
        if (!access.set(other.copyWithCount(other.getCount() - 1))) {
            clearLoad(catalyst);
            return false;
        }
        player.getInventory().setChanged();
        player.containerMenu.broadcastChanges();
        return true;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack catalyst, Slot slot, ClickAction action, Player player) {
        if (action != ClickAction.SECONDARY || !slot.allowModification(player)) return false;
        ItemStack other = slot.getItem();
        if (!loadAmmo(catalyst, other)) return false;
        if (slot.remove(1).isEmpty()) {
            clearLoad(catalyst);
            return false;
        }
        slot.setChanged();
        player.getInventory().setChanged();
        player.containerMenu.broadcastChanges();
        return true;
    }

    public static void chargePellet(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {
        Vec3 start = livingEntity.getEyePosition();
        Vec3 look = livingEntity.getLookAngle();
        Vec3 end = start.add(look.scale(10));

        AABB box = new AABB(start, end).inflate(1.5D);

        float radius = Mth.lerp((float) ticksRemaining / DURATION, 0.5f, 0.08f);
        if (level instanceof ServerLevel serverLevel) {
            for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, box)) {
                if (target == livingEntity || target.isSpectator()) continue;
                target.hurtServer(serverLevel, chargeDamageSource(target, livingEntity), 1.0F);
            }
        }

        if (ticksRemaining <= 1) {
            if (livingEntity.level() instanceof ServerLevel serverLevel) {
                double ringRadius = 0.5;
                int points = 32;
                int ringCount = 4;
                double spacing = 2.0;

                for (int r = 0; r < ringCount; r++) {
                    double distance = 1.0f + r * spacing;
                    Vec3 center = livingEntity.position().add(look.scale(distance));

                    for (int i = 0; i < points; i++) {
                        Quaternionf q = Vec3Helper.getRotationQuaternionAroundLookVector(i, points, livingEntity, look);

                        Vec3 base = new Vec3(0, ringRadius, 0);
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

    public static void impactPellet(ServerLevel level, LivingEntity owner) {
        ImpactPelletEntity projectile = new ImpactPelletEntity(EntityRegistry.IMPACT_PELLET.get(), level, owner);
        Vec3 direction = owner.getLookAngle().normalize();
        projectile.setPos(owner.getEyePosition().add(direction.scale(0.75)));
        projectile.shoot(direction.x, direction.y, direction.z, 1.5F, 0.0F);
        level.addFreshEntity(projectile);
    }

    public static void flashPellet(ServerLevel level, LivingEntity owner) {
        level.playSound(null, owner.blockPosition(), SoundRegistry.CATALYST_BURST.get(), net.minecraft.sounds.SoundSource.PLAYERS, 2.0F, 0.8F);
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, owner.getBoundingBox().inflate(12))) {
            target.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.BLINDNESS, FLASH_BLINDNESS_TICKS, 0));
            if (target instanceof Mob mob) {
                FLASH_DEAGGRO.put(mob.getUUID(), level.getGameTime() + FLASH_DEAGGRO_TICKS);
                mob.setTarget(null);
                mob.setLastHurtByMob(null);
                mob.getBrain().eraseMemory(net.minecraft.world.entity.ai.memory.MemoryModuleType.ATTACK_TARGET);
                mob.getBrain().eraseMemory(net.minecraft.world.entity.ai.memory.MemoryModuleType.NEAREST_ATTACKABLE);
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

    private static int getTicksRemaining(ItemStack itemStack) {
        return getData(itemStack, "ticks_remaining");
    }

    private static void setTicksRemaining(ItemStack itemStack, int ticksRemaining) {
        setData(itemStack, "ticks_remaining", ticksRemaining);
    }

    public static boolean shouldStopRendering(Renderable.RenderableContext context) {
        ItemStack stack = context.user().getUseItem();

        if (stack.getItem() != ItemRegistry.HUNTERS_CATALYST.get()) return true;

        int ticks = getTicksRemaining(stack);

        return ticks <= 1;
    }

    private static void burstCharge(ServerLevel level, LivingEntity user, ItemStack catalyst) {
        if (!hasFuel(catalyst) || getPellet(catalyst) != CHARGE) return;

        Vec3 look = user.getLookAngle().normalize();
        user.push(-look.x * 1.2, -look.y * 1.2, -look.z * 1.2);
        level.playSound(null, user.blockPosition(), SoundRegistry.CATALYST_BURST.get(), net.minecraft.sounds.SoundSource.PLAYERS, 2.5F, 1.0F);
        Vec3 start = user.getEyePosition();
        Vec3 end = start.add(look.scale(10));
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, new AABB(start, end).inflate(1.5))) {
            if (target == user) continue;
            target.hurtServer(level, chargeDamageSource(target, user), 12.0F);
        }
        for (int distance = 1; distance <= 10; distance++) {
            Vec3 center = user.getEyePosition().add(look.scale(distance));
            for (int i = 0; i < 12; i++) {
                Quaternionf q = Vec3Helper.getRotationQuaternionAroundLookVector(i, 12, user, look);
                Vector3f offset = new Vector3f(0, 0.35f, 0).rotate(q);
                level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, center.x + offset.x(), center.y + offset.y(), center.z + offset.z(), 1, -look.x * .35, -look.y * .35, -look.z * .35, 0);
            }
        }
        clearLoadAndSync(user, catalyst);
    }

    private static int pelletType(ItemStack stack) {
        if (stack.is(ItemRegistry.CHARGE_PELLET.get())) return CHARGE;
        if (stack.is(ItemRegistry.IMPACT_PELLET.get())) return IMPACT;
        if (stack.is(ItemRegistry.FLASH_PELLET.get())) return FLASH;
        return NONE;
    }

    private static boolean hasFuel(ItemStack stack) { return getData(stack, FUEL) != 0; }
    private static int getPellet(ItemStack stack) { return getData(stack, PELLET); }
    private static int getData(ItemStack stack, String key) {
        MapStringIntData data = stack.get(DataComponentTypeRegistry.MAP_STRING_INT.get());
        return data == null ? 0 : data.map().getOrDefault(key, 0);
    }

    private static void setData(ItemStack stack, String key, int value) {
        Map<String, Integer> values = new HashMap<>();
        MapStringIntData existing = stack.get(DataComponentTypeRegistry.MAP_STRING_INT.get());
        if (existing != null) values.putAll(existing.map());
        values.put(key, value);
        stack.set(DataComponentTypeRegistry.MAP_STRING_INT.get(),
                new MapStringIntData(Map.copyOf(values)));
    }

    private static boolean loadAmmo(ItemStack catalyst, ItemStack ammo) {
        if (ammo.is(ItemRegistry.FIRE_POWDER.get()) && !hasFuel(catalyst)) {
            setData(catalyst, FUEL, 1);
            return true;
        }
        int pellet = pelletType(ammo);
        if (pellet != NONE && hasFuel(catalyst) && getPellet(catalyst) == NONE) {
            setData(catalyst, PELLET, pellet);
            return true;
        }
        return false;
    }
    private static void clearLoad(ItemStack stack) {
        setData(stack, FUEL, 0);
        setData(stack, PELLET, NONE);
    }

    private static void clearLoadAndSync(LivingEntity user, ItemStack stack) {
        if (user instanceof Player player) {
            clearLoadAndSync(player, player.getUsedItemHand(), stack);
        } else {
            clearLoad(stack);
        }
    }

    private static void clearLoadAndSync(Player player, InteractionHand hand, ItemStack stack) {
        clearLoad(stack);
        ItemStack clearedStack = stack.copy();
        player.setItemInHand(hand, clearedStack);
        player.getInventory().setChanged();
        player.containerMenu.broadcastChanges();
    }

    private static void deaggroNearbyMobs(ServerLevel level, Player player) {
        for (Mob mob : level.getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(16))) {
            mob.setTarget(null);
            mob.setLastHurtByMob(null);
            mob.getBrain().eraseMemory(net.minecraft.world.entity.ai.memory.MemoryModuleType.ATTACK_TARGET);
            mob.getBrain().eraseMemory(net.minecraft.world.entity.ai.memory.MemoryModuleType.NEAREST_ATTACKABLE);
        }
    }

    public static void tickFlashDeaggro(ServerLevel level) {
        Iterator<Map.Entry<UUID, Long>> iterator = FLASH_DEAGGRO.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, Long> entry = iterator.next();
            if (entry.getValue() <= level.getGameTime()) {
                iterator.remove();
                continue;
            }
            if (level.getEntity(entry.getKey()) instanceof Mob mob) {
                mob.setTarget(null);
                mob.setLastHurtByMob(null);
                mob.getBrain().eraseMemory(net.minecraft.world.entity.ai.memory.MemoryModuleType.ATTACK_TARGET);
                mob.getBrain().eraseMemory(net.minecraft.world.entity.ai.memory.MemoryModuleType.NEAREST_ATTACKABLE);
            }
        }
    }

    private static DamageSource chargeDamageSource(LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player) return target.damageSources().playerAttack(player);
        return target.damageSources().mobAttack(attacker);
    }

    public static void render(Renderable.RenderableContext context) {
        LivingEntity user = context.user();
        SubmitNodeCollector output = context.output();
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

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

        int ticks = DURATION - getTicksRemaining(context.user().getUseItem());
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
