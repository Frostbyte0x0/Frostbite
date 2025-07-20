package org.exodusstudio.frostbite.common.item.custom.weapons;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.entity.custom.WindCircleEntity;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.util.MathsUtil;

import java.util.Optional;

public class GaleFanItem extends Item {
    private static final float DIST = 8f;
    private static final float LOCK_ON_RANGE = 6f;
    private static final int TICKS_ALLOWED_FOR_SECOND_ATTACK = 100;
    private static final int COOLDOWN_TICKS = 100;
    private boolean firstAttack = true;
    private int tickSinceFirstAttack = 0;

    public GaleFanItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        if (firstAttack) {
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

                entity.moveTo(pos, 0.0F, 0.0F);
                serverLevel.addFreshEntityWithPassengers(entity);
                serverLevel.gameEvent(GameEvent.ENTITY_PLACE, pos, GameEvent.Context.of(player));
                firstAttack = false;
            }
        } else {
            final int range = 10;
            Vec3 vec3 = player.position().add(player.getAttachments().get(EntityAttachment.WARDEN_CHEST, 0, player.getYRot()));
            Vec3 vec31 = player.getLookAngle();
            Vec3 vec32 = vec31.normalize();

            for (int j = 1; j < range; j++) {
                Vec3 vec33 = vec3.add(vec32.scale(j));
                player.level().addAlwaysVisibleParticle(ParticleTypes.SONIC_BOOM, vec33.x, vec33.y, vec33.z, 1, 0.0, 0.0);
            }

            player.playSound(SoundEvents.WARDEN_SONIC_BOOM, 3.0F, 1.0F);

            Vec3 end = vec3.add(vec32.scale(range));

            if (level instanceof ServerLevel serverLevel) {
                for (Entity entity1 : player.level().getEntities(player, new AABB(vec3, end).inflate(1.0))) {
                    AABB aabb = entity1.getBoundingBox().inflate(1);
                    Optional<Vec3> optional1 = aabb.clip(vec3, end);
                    if (optional1.isPresent()) {
                        double d1 = 0.5 * (1.0 - player.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                        double d0 = 2.5 * (1.0 - player.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                        entity1.hurtServer(serverLevel, player.damageSources().generic(), 10f);
                        entity1.push(vec32.x() * d0, vec32.y() * d1, vec32.z() * d0);
                    }
                }
            }

            tickSinceFirstAttack = TICKS_ALLOWED_FOR_SECOND_ATTACK;
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (tickSinceFirstAttack >= TICKS_ALLOWED_FOR_SECOND_ATTACK) {
            firstAttack = true;
            tickSinceFirstAttack = 0;
            if (entity instanceof Player player) {
                player.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);
            }
        }

        if (!firstAttack) tickSinceFirstAttack++;
    }
}