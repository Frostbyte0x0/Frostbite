package org.exodusstudio.frostbite.common.item.weapons;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.exodusstudio.frostbite.common.registry.AttachementRegistry;

public abstract class ComboWeapon extends Item {
    private final ComboStep[] steps;

    // TODO: Animations + smoothing

    public ComboWeapon(Properties properties, ComboStep... steps) {
        super(properties);
        this.steps = steps;
    }

    public float getAttackDamageBonus(Entity victim, float ignoredDamage, DamageSource damageSource) {
        if (damageSource.getEntity() instanceof LivingEntity attacker) {
            if (attacker.getItemInHand(attacker.getUsedItemHand()).getItem() instanceof ComboWeapon comboWeapon) {
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
        if (entity.getItemInHand(entity.getUsedItemHand()).getItem() instanceof ComboWeapon comboWeapon) {
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
                if (t < currentStep.critTolerance) entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                        SoundEvents.ANVIL_LAND, entity.getSoundSource(), 0.5f, 1f + index / (2f * stepCount));
                else if (t < currentStep.tolerance) entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                        SoundEvents.ARROW_HIT_PLAYER, entity.getSoundSource(), 0.5f, 1f + index / (2f * stepCount));
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
        entity.setData(AttachementRegistry.LAST_HIT, entity.level().getGameTime());
        ComboStep currentStep = comboWeapon.getComboStep(entity);
        if (currentStep != null) {
            entity.setData(AttachementRegistry.COMBO_LENGTH, currentStep.delayToNext);
        }
    }

    public static float getStepProgress(LivingEntity entity) {
        float lastHit = ComboWeapon.getTimeSinceLastHit(entity);
        float comboLength = ComboWeapon.getComboLength(entity);
        return lastHit / (2 * comboLength);
    }

    public static float getComboLength(LivingEntity entity) {
        return entity.getData(AttachementRegistry.COMBO_LENGTH);
    }

    public static float getTimeSinceLastHit(LivingEntity entity) {
        return (entity.level().getGameTime() - entity.getData(AttachementRegistry.LAST_HIT)) / 20f;
    }

    public static void resetComboIndex(LivingEntity entity) {
        entity.setData(AttachementRegistry.COMBO_INDEX, 0);
    }

    public static void increaseComboIndex(LivingEntity entity) {
        entity.setData(AttachementRegistry.COMBO_INDEX, getComboIndex(entity) + 1);
    }

    public static int getComboIndex(LivingEntity entity) {
        return entity.getData(AttachementRegistry.COMBO_INDEX);
    }

    public ComboStep getComboStep(LivingEntity entity) {
        int index = getComboIndex(entity);
        return index >= 0 && index < steps.length ? steps[index] : null;
    }

    public int getComboStepCount() {
        return steps.length;
    }

    public record ComboStep(float extraDamage, float delayToNext, float tolerance, float critTolerance) {}
}
