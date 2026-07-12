package org.exodusstudio.frostbite.common.item.weapons;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import org.exodusstudio.frostbite.common.registry.AttachementRegistry;

public abstract class ChargeAttackWeapon extends ComboWeapon {
    public final String chargeAttack;

    public ChargeAttackWeapon(Properties properties, String chargeAttack, ComboWeapon.ComboStep... comboSteps) {
        super(properties, comboSteps);
        this.chargeAttack = chargeAttack;
    }

    public void startChargeAttack(LivingEntity user) {
//        if (user.getCooldowns().isOnCooldown(this)) return;
//        user.getCooldowns().addCooldown(this, 20);
//        user.startUsingItem(user.getUsedItemHand());

        if ( Minecraft.getInstance().level == null) return; // TODO: fix this, has to run on server side, not client side
        user.setData(AttachementRegistry.CHARGE_ATTACK_START, Minecraft.getInstance().level.getGameTime());
        user.setData(AttachementRegistry.CURRENT_CHARGE_ATTACK, chargeAttack);
    }
}
