package org.exodusstudio.frostbite.common.item.weapons.goat;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.item.weapons.ComboWeapon;

public class NaginataItem extends ComboWeapon {
    public NaginataItem(Properties properties) {
        super(properties, 30,
                new ComboStep(2, 0.9f, 0.15f, 0.025f),
                new ComboStep(3, 0.65f, 0.15f, 0.025f),
                new ComboStep(3, 0.6f, 0.15f, 0.025f),
                new ComboStep(4, 0.7f, 0.15f, 0.025f));
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 2.0,
                        AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }

    @Override
    public void doChargeAttack(Level level, LivingEntity user, InteractionHand hand) {
        ComboWeapon.genericSweepAttack(level, user, hand);
        spawnSweepParticles(user);
        user.push(0, 0.05, 0);
    }

    private void spawnSweepParticles(LivingEntity user) {
        if (!(user.level() instanceof ServerLevel world)) return;

        for (int i = 0; i <= 6; i++) {
            float angle = user.getYHeadRot() + (i * 20) - 60;
            float rad = angle * ((float) Math.PI / 180f);

            double d = -Mth.sin(rad) * 3.0;
            double e = Mth.cos(rad) * 3.0;

            world.sendParticles(ParticleTypes.SWEEP_ATTACK,
                    user.getX() + d, user.getY() + 0.5, user.getZ() + e,
                    0, d, 0.0, e, 0.0);
        }
    }
}
