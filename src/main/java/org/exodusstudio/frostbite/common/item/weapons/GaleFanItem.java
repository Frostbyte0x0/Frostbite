package org.exodusstudio.frostbite.common.item.weapons;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.component.ChargeData;
import org.exodusstudio.frostbite.common.component.ModeData;
import org.exodusstudio.frostbite.common.entity.custom.WindCircleEntity;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;
import org.exodusstudio.frostbite.common.util.MathsUtil;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Optional;

public class GaleFanItem extends Item {
    private static final float DIST = 8f;
    private static final float LOCK_ON_RANGE = 7f;
    private static final int TICKS_ALLOWED_FOR_SECOND_ATTACK = 60;
    private static final int COOLDOWN_TICKS = 100;
    private final RandomSource random = RandomSource.create();

    public GaleFanItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (isFirstAttack(stack)) {
            if (level instanceof ServerLevel serverLevel) {
                float angle = player.getYRot() + 90f;
                Vec3 pos = player.position().add(
                        Math.cos(angle * Math.PI / 180) * DIST,
                        0,
                        Math.sin(angle * Math.PI / 180) * DIST);
                WindCircleEntity entity = new WindCircleEntity(EntityRegistry.WIND_CIRCLE.get(), serverLevel);

                for (LivingEntity livingEntity : serverLevel.getEntitiesOfClass(LivingEntity.class, MathsUtil.squareAABB(pos, LOCK_ON_RANGE))) {
                    pos = livingEntity.position();
                    if (livingEntity instanceof Player) break;
                }

                entity.move(MoverType.SELF, pos);
                serverLevel.addFreshEntityWithPassengers(entity);
                serverLevel.gameEvent(GameEvent.ENTITY_PLACE, pos, GameEvent.Context.of(player));
            }
        } else {
            final int range = 10;
            Vec3 vec3 = player.position().add(player.getAttachments().get(EntityAttachment.WARDEN_CHEST, 0, player.getYRot()));
            Vec3 vec31 = player.getLookAngle();
            Vec3 vec32 = vec31.normalize();

            for (int i = 1; i < range; i++) {
                Vec3 vec33 = vec3.add(vec32.scale(i));
                for (int j = 0; j < 30; j++) {
                    float angle = (float) (j * Math.PI / 15);
                    float playerYAngle = (float) ((90 - player.getYRot()) * Math.PI / 180);
                    float playerXAngle = (float) (player.getXRot() * Math.PI / 180);
                    Quaternionf quaternion;
                    if (Math.abs(vec32.x) < 0.5f) {
                        quaternion = new Quaternionf()
                                .rotateLocalX(angle)
                                .rotateLocalZ(playerXAngle)
                                .rotateLocalY(playerYAngle);
                    } else {
                        quaternion = new Quaternionf()
                                .rotateLocalZ(angle)
                                .rotateLocalX(-playerXAngle)
                                .rotateLocalY((float) (playerYAngle + Math.PI / 2));
                    }

                    Vector3f add = vec32.toVector3f().rotate(quaternion).mul(2);

                    Vec3 vec34 = vec33.add(
                            add.x + random.nextFloat() * 2,
                            add.y + random.nextFloat() * 2,
                            add.z + random.nextFloat() * 2);
                    player.level().addAlwaysVisibleParticle(ParticleRegistry.SWIRLING_LEAF_PARTICLE.get(),
                            vec34.x, vec34.y, vec34.z,
                            vec32.x,
                            vec32.y,
                            vec32.z);
                }
            }

            player.playSound(SoundEvents.WARDEN_SONIC_BOOM, 3.0F, 1.0F);

            Vec3 end = vec3.add(vec32.scale(range));

            if (level instanceof ServerLevel serverLevel) {
                for (Entity entity1 : player.level().getEntities(player, new AABB(vec3, end).inflate(1.0))) {
                    AABB aabb = entity1.getBoundingBox().inflate(1);
                    Optional<Vec3> optional1 = aabb.clip(vec3, end);
                    if (optional1.isPresent()) {
                        //double d1 = 0.5 * (1.0 - player.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                        double d0 = 4 * (1.0 - player.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                        entity1.hurtServer(serverLevel, player.damageSources().generic(), 10f);
                        entity1.push(vec32.x() * d0, vec32.y() * d0, vec32.z() * d0);
                    }
                }
            }

            setTicksSinceFirstAttack(stack, TICKS_ALLOWED_FOR_SECOND_ATTACK);
        }

        setFirstAttack(stack, false);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, level, entity, slot);

        if (getTicksSinceFirstAttack(stack) >= TICKS_ALLOWED_FOR_SECOND_ATTACK) {
            setFirstAttack(stack, true);
            setTicksSinceFirstAttack(stack, 0);
            if (entity instanceof Player player) {
                player.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);
            }
        }

        if (!isFirstAttack(stack)) increaseTicksSinceFirstAttack(stack);
    }

    public boolean isFirstAttack(ItemStack stack) {
        return stack.get(DataComponentTypeRegistry.MODE).mode().equals("firstAttack");
    }

    public void setFirstAttack(ItemStack stack, boolean firstAttack) {
        ModeData modeData = new ModeData(firstAttack ? "firstAttack" : "secondAttack");
        stack.set(DataComponentTypeRegistry.MODE.get(), modeData);
    }

    public int getTicksSinceFirstAttack(ItemStack stack) {
        return stack.get(DataComponentTypeRegistry.CHARGE).charge();
    }

    public void setTicksSinceFirstAttack(ItemStack stack, int ticks) {
        ChargeData chargeData = new ChargeData(ticks);
        stack.set(DataComponentTypeRegistry.CHARGE.get(), chargeData);
    }

    public void increaseTicksSinceFirstAttack(ItemStack stack) {
        ChargeData chargeData = new ChargeData(getTicksSinceFirstAttack(stack) + 1);
        stack.set(DataComponentTypeRegistry.CHARGE.get(), chargeData);
    }
}