package org.exodusstudio.frostbite.common.item.weapons;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import org.exodusstudio.frostbite.common.registry.AttachementRegistry;
import org.exodusstudio.frostbite.common.util.Renderable;

import java.util.*;

public abstract class ChargeAttackWeapon extends ComboWeapon {
    public final String chargeAttack;

    public ChargeAttackWeapon(Properties properties, Renderable chargeAttack, ComboWeapon.ComboStep... comboSteps) {
        super(properties, comboSteps);
        this.chargeAttack = chargeAttack.getName();
    }

    public void startChargeAttack(LivingEntity user) {
//        if (user.getCooldowns().isOnCooldown(this)) return;
//        user.getCooldowns().addCooldown(this, 20);
//        user.startUsingItem(user.getUsedItemHand());

        //if (Minecraft.getInstance().level == null) return; // TODO: fix this, has to run on server side, not client side
        user.setData(AttachementRegistry.CHARGE_ATTACK_START, Minecraft.getInstance().level.getGameTime());
        addChargeAttack(user, chargeAttack);
    }

    public static void addChargeAttack(LivingEntity user, String chargeAttackRenderable) {
        Map<UUID, List<Pair<String, Long>>> currentCharges = user.level().getData(AttachementRegistry.CURRENT_RENDERING_ATTACKS);
        if (currentCharges.get(user.getUUID()) != null) {
            currentCharges.get(user.getUUID()).add(Pair.of(chargeAttackRenderable, user.level().getGameTime()));
        } else {
            currentCharges.put(user.getUUID(), new ArrayList<>(List.of(Pair.of(chargeAttackRenderable, user.level().getGameTime()))));
        }
        user.level().setData(AttachementRegistry.CURRENT_RENDERING_ATTACKS, currentCharges);
    }

    public static void removeChargeAttack(LivingEntity user, String chargeAttackRenderable) {
        Map<UUID, List<Pair<String, Long>>> currentCharges = new HashMap<>(user.level().getData(AttachementRegistry.CURRENT_RENDERING_ATTACKS));
        for (Pair<String, Long> pair : currentCharges.get(user.getUUID())) {
            if (pair.getFirst().equals(chargeAttackRenderable)) {
                currentCharges.put(user.getUUID(), new ArrayList<>(currentCharges.get(user.getUUID())));
                currentCharges.get(user.getUUID()).remove(pair);
                break;
            }
        }
        user.level().setData(AttachementRegistry.CURRENT_RENDERING_ATTACKS, currentCharges);
    }
}
