package org.exodusstudio.frostbite.common.item.weapons;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.component.CooldownData;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.util.Renderable;

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
        properties.component(DataComponentTypeRegistry.COOLDOWN, new CooldownData(-1));
        super(properties, chargeRequired, comboSteps);
        this.seriousAttack = seriousAttack.getName();
        this.cooldown = chargeAttackCooldown;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (CooldownData.canUse(player.getItemInHand(usedHand), level.getGameTime(), cooldown)) {
            doCooldownAttack(level, player, usedHand);
            CooldownData.setLastUsed(player.getItemInHand(usedHand));
            return InteractionResult.SUCCESS;
        }
        if (getCharge(stack) >= chargeRequired && player.isShiftKeyDown()) {
            doChargeAttack(level, player, usedHand);
            setCharge(stack, 0);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public abstract void doCooldownAttack(Level level, LivingEntity user, InteractionHand usedHand);

    public float getCooldown() {
        return cooldown;
    }
}
