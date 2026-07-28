package org.exodusstudio.frostbite.common.item.weapons;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.util.helpers.DataHelper;
import org.exodusstudio.frostbite.common.util.Renderable;

import java.util.Map;

public abstract class SeriousAttackWeapon extends ComboWeapon {
    public final String seriousAttack;
    private final float cooldown;

    public SeriousAttackWeapon(
            Properties properties,
            int chargeRequired,
            Renderable seriousAttack,
            float chargeAttackCooldown,
            ComboWeapon.ComboStep... comboSteps
    ) {
        super(properties, chargeRequired, Map.of("last_used", -1), comboSteps);
        this.seriousAttack = seriousAttack.getName();
        this.cooldown = chargeAttackCooldown;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (canUse(player.getItemInHand(usedHand), level.getGameTime(), cooldown)) {
            doCooldownAttack(level, player, usedHand);
            DataHelper.setData(stack, "last_used", Math.toIntExact(player.level().getGameTime()));
            return InteractionResult.SUCCESS;
        }
        if (getCharge(stack) >= chargeRequired && player.isShiftKeyDown()) {
            doChargeAttack(level, player, usedHand);
            setCharge(stack, 0);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public static boolean canUse(ItemStack stack, long currentTime, float cooldownSeconds) {
        return secondsSinceLastUsed(stack, currentTime) >= cooldownSeconds;
    }

    public static int secondsSinceLastUsed(ItemStack stack, long currentTime) {
        return Math.toIntExact((currentTime - DataHelper.getInt(stack, "last_used")) / 20);
    }

    public abstract void doCooldownAttack(Level level, LivingEntity user, InteractionHand usedHand);

    public float getCooldown() {
        return cooldown;
    }
}
