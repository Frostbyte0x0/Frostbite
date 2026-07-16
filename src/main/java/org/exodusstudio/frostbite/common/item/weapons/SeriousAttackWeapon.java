package org.exodusstudio.frostbite.common.item.weapons;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
        if (CooldownData.canUse(player.getItemInHand(usedHand), level.getGameTime(), cooldown) && player.isShiftKeyDown()) {
            doCooldownAttack(level, player, usedHand);
            CooldownData.setLastUsed(player.getItemInHand(usedHand));
            return InteractionResult.SUCCESS;
        }
        return super.use(level, player, usedHand);
    }

    public abstract void doCooldownAttack(Level level, LivingEntity user, InteractionHand usedHand);
}
