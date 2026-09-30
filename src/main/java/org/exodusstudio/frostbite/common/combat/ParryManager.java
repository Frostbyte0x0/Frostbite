package org.exodusstudio.frostbite.common.combat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import org.exodusstudio.frostbite.common.registry.Tags;
import org.exodusstudio.frostbite.common.util.helpers.DataHelper;

/** Server-authoritative parry state and rules. */
public final class ParryManager {
    private static final String ACTIVE = "frostbite_parrying";
    private static final String TICKS = "frostbite_parry_ticks";
    public static final int PERFECT_WINDOW = 5;
    public static final int PARRY_WINDOW = 15;
    public static final int MAX_DURATION = 40;

    private ParryManager() {}

    public static boolean canParry(Player player) {
        return player.getMainHandItem().is(Tags.PARRY_WEAPONS)
                || player.getOffhandItem().is(Tags.PARRY_WEAPONS);
    }

    public static boolean isParrying(LivingEntity entity) {
        return DataHelper.getInt(entity, ACTIVE) != 0;
    }

    public static int ticks(LivingEntity entity) {
        return DataHelper.getInt(entity, TICKS);
    }

    public static void start(Player player) {
        if (!canParry(player)) return;
        DataHelper.setData(player, ACTIVE, 1);
        DataHelper.setData(player, TICKS, 0);
    }

    public static void stop(LivingEntity entity) {
        DataHelper.setData(entity, ACTIVE, 0);
        DataHelper.setData(entity, TICKS, 0);
    }

    public static void tick(Player player) {
        if (!isParrying(player)) return;
        if (!canParry(player)) {
            stop(player);
            return;
        }
        DataHelper.setData(player, TICKS, ticks(player) + 1);
        if (ticks(player) >= MAX_DURATION) {
            stop(player);
            return;
        }
        player.setSprinting(false);
        var movement = player.getDeltaMovement();
        player.setDeltaMovement(movement.x * 0.15, Math.min(0.0, movement.y), movement.z * 0.15);
    }

    public static boolean isAttackingDamage(DamageSource source) {
        if (source.is(DamageTypes.FALL) || source.is(DamageTypes.FLY_INTO_WALL)
                || source.is(DamageTypes.FELL_OUT_OF_WORLD)) return false;
        return source.getEntity() instanceof LivingEntity
                || source.getDirectEntity() instanceof LivingEntity;
    }

    /** Returns the damage multiplier and ends the active parry for the first two windows. */
    public static float damageMultiplier(Player player) {
        int tick = ticks(player);
        if (tick < PERFECT_WINDOW) {
            stop(player);
            return 0.0f;
        }
        if (tick < PARRY_WINDOW) {
            stop(player);
            return 0.4f;
        }
        stop(player);
        return 0.7f;
    }
}
