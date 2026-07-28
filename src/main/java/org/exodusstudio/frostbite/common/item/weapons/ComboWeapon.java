package org.exodusstudio.frostbite.common.item.weapons;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.exodusstudio.frostbite.common.component.MapStringIntData;
import org.exodusstudio.frostbite.common.contracts.Contract;
import org.exodusstudio.frostbite.common.contracts.ContractAttributes;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.util.helpers.DataHelper;

import java.util.List;
import java.util.Map;

public abstract class ComboWeapon extends Item {
    private final ComboStep[] steps;
    protected final int chargeRequired;

    // TODO: Animations + smoothing

    public ComboWeapon(Properties properties, int chargeRequired, ComboStep... steps) {
        properties.component(DataComponentTypeRegistry.MAP_STRING_INT.get(), new MapStringIntData(Map.of("charge", 0)));
        super(properties);
        this.chargeRequired = chargeRequired;
        this.steps = steps;
    }

    public ComboWeapon(Properties properties, int chargeRequired, Map<String, Integer> map, ComboStep... steps) {
        DataHelper.safelyAddValueToMap(map, "charge", 0);
        properties.component(DataComponentTypeRegistry.MAP_STRING_INT.get(), new MapStringIntData(map));
        super(properties);
        this.chargeRequired = chargeRequired;
        this.steps = steps;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (getCharge(stack) >= chargeRequired) {
            doChargeAttack(level, player, usedHand);
            setCharge(stack, 0);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public abstract void doChargeAttack(Level level, LivingEntity user, InteractionHand usedHand);

    public static void genericSweepAttack(Level level, LivingEntity user, InteractionHand usedHand) {
        float yaw = user.getYRot() * ((float) Math.PI / 180f);

        BlockPos pos = BlockPos.containing(user.position().add(
                -Mth.sin(yaw) * 1.4, user.getEyeHeight() / 2.0f, Mth.cos(yaw) * 1.4f
        ));

        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class, new AABB(pos).inflate(3.0, 1.0, 3.0),
                (entity) -> entity != user);

//        stack.damage(1, user, EquipmentSlot.MAINHAND);

        targets.forEach(target -> {
            double distance = user.distanceToSqr(target);
            if (distance < 6.0 || distance >= 36.0) return;

            if (!(target instanceof ArmorStand)) {
                target.push(
                        0.4,
                        Mth.sin(user.getYHeadRot() * 0.017453292F),
                        -Mth.cos(user.getYHeadRot() * 0.017453292F)
                );
            }

            if (user.level() instanceof ServerLevel serverLevel) {
                if (user instanceof Player player) {
                    target.hurtServer(serverLevel, target.damageSources().playerAttack(player), 5.5f);
                } else {
                    target.hurtServer(serverLevel, target.damageSources().mobAttack(user), 5.5f);
                }
            }
        });
        user.swing(usedHand);
        level.playSound(
                null, user.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS,
                1.0f,
                0.9f + level.getRandom().nextFloat() * 0.2f
        );
    }

    public static float getDamageBonus(DamageSource damageSource) {
        if (damageSource.getEntity() instanceof LivingEntity attacker) {
            ItemStack stack = attacker.getItemInHand(attacker.getUsedItemHand());
            if (stack.getItem() instanceof ComboWeapon comboWeapon) {
                ComboStep currentStep = comboWeapon.getComboStep(attacker);
                if (currentStep != null) {
                    float ret = 0;
                    float t = Math.abs(0.5f - getStepProgress(attacker));

                    if (t < currentStep.critTolerance) ret = currentStep.extraDamage * 2;
                    if (t < currentStep.tolerance) ret = currentStep.extraDamage;
                    attacks(attacker);
                    return ret;
                }
            }
        }
        return 0;
    }

    public static void attacks(LivingEntity entity) {
        ItemStack stack = entity.getItemInHand(entity.getUsedItemHand());
        if (stack.getItem() instanceof ComboWeapon comboWeapon) {
            float timeSinceLastHit = getTimeSinceLastHit(entity);
            float index = getComboIndex(entity);
            int stepCount = comboWeapon.getComboStepCount();
            ComboStep currentStep = comboWeapon.getComboStep(entity);
            float t = Math.abs(0.5f - getStepProgress(entity));

            if (
                currentStep != null &&
                timeSinceLastHit <= currentStep.delayToNext * 2 &&
                t <= currentStep.tolerance &&
                index < comboWeapon.steps.length
            ) {
                if (t < currentStep.critTolerance) {
                    entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                            SoundEvents.ANVIL_LAND, entity.getSoundSource(), 0.5f, 1f + index / (2f * stepCount));
                    increaseCharge(stack, 2);
                }
                else if (t < currentStep.tolerance) {
                    entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                            SoundEvents.ARROW_HIT_PLAYER, entity.getSoundSource(), 0.5f, 1f + index / (2f * stepCount));
                    increaseCharge(stack, 1);
                }
            }

            if (
                currentStep != null &&
                timeSinceLastHit <= currentStep.delayToNext * 2 &&
                t <= currentStep.tolerance &&
                index < comboWeapon.steps.length - 1
            ) {
                increaseComboIndex(entity);
            } else {
                resetComboIndex(entity);
            }

            setLastHit(entity, comboWeapon);
        }
    }

    public static boolean shouldShowComboOverlay(Player player) {
        if (player.getItemInHand(player.getUsedItemHand()).getItem() instanceof ComboWeapon comboWeapon) {
            float timeSinceLastHit = getTimeSinceLastHit(player);
            ComboStep currentStep = comboWeapon.getComboStep(player);

            return currentStep != null && timeSinceLastHit < currentStep.delayToNext * 2 && !Minecraft.getInstance().gui.hud.isHidden();
        }
        return false;
    }

    public static void setLastHit(LivingEntity entity, ComboWeapon comboWeapon) {
        DataHelper.setData(entity, "last_hit", (int) entity.level().getGameTime());
        ComboStep currentStep = comboWeapon.getComboStep(entity);
        if (currentStep != null) {
            DataHelper.setData(entity, "combo_length", currentStep.delayToNext);
        }
    }

    public static float getStepProgress(LivingEntity entity) {
        float lastHit = ComboWeapon.getTimeSinceLastHit(entity);
        float comboLength = ComboWeapon.getComboLength(entity);
        return lastHit / (2 * comboLength);
    }

    public static float getComboLength(LivingEntity entity) {
        return DataHelper.getFloat(entity, "combo_length");
    }

    public static float getTimeSinceLastHit(LivingEntity entity) {
        return (entity.level().getGameTime() - DataHelper.getInt(entity, "last_hit")) / 20f;
    }

    public static void resetComboIndex(LivingEntity entity) {
        DataHelper.setData(entity, "combo_index", 0);
    }

    public static void increaseComboIndex(LivingEntity entity) {
        DataHelper.setData(entity, "combo_index", getComboIndex(entity) + 1);
    }

    public static int getComboIndex(LivingEntity entity) {
        return DataHelper.getInt(entity, "combo_index");
    }

    public ComboStep getComboStep(LivingEntity entity) {
        int index = getComboIndex(entity);
        return index >= 0 && index < steps.length ? steps[index] : null;
    }

    public int getComboStepCount() {
        return steps.length;
    }

    public static int getCharge(ItemStack stack) {
        return DataHelper.getInt(stack, "charge");
    }

    public static void setCharge(ItemStack stack, int charge) {
        if (stack.getItem() instanceof ComboWeapon c)
            DataHelper.setData(stack, "charge", Math.clamp(charge, 0, c.chargeRequired));
    }

    public static void increaseCharge(ItemStack stack, int charge) {
        Contract c = Contract.getContract(stack);
        if (c != null && c.hasAttribute(ContractAttributes.SHARP)) {
            setCharge(stack, getCharge(stack) + charge * (1 + c.allScalableAttributes().get(ContractAttributes.SHARP) / 100));
            return;
        }
        setCharge(stack, getCharge(stack) + charge);
    }

    public int chargeRequired() {
        return chargeRequired;
    }

    public record ComboStep(float extraDamage, float delayToNext, float tolerance, float critTolerance) {}
}
