package org.exodusstudio.frostbite.common.item.weapons.goat;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.item.weapons.ComboWeapon;

public class DaggersItem extends ComboWeapon {
    public DaggersItem(Properties properties) {
        super(properties, 30,
                new ComboStep(2, 0.75f, 0.15f, 0.025f),
                new ComboStep(3, 0.65f, 0.15f, 0.025f),
                new ComboStep(4, 0.85f, 0.15f, 0.025f));
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 2.0,
                        AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }

    @Override
    public void doChargeAttack(Level level, LivingEntity user, InteractionHand hand) {
        user.startUsingItem(hand);

        if (!level.isClientSide()) {
            user.level().playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.SNOW_HIT, SoundSource.PLAYERS, 1.0f, 1);

            ((ServerLevel) level).sendParticles(ParticleTypes.CLOUD,
                    user.getX(), user.getY(), user.getZ(),
                    6,
                    0.2, 0.2, 0.2,
                    0.02);
            ((ServerLevel) level).sendParticles(ParticleTypes.FLAME,
                    user.getX(), user.getY(), user.getZ(),
                    6,
                    0.2, 0.2, 0.2,
                    0.02);
        }
        Vec3 dir = user.getLookAngle();
        Vec3 vel = user.getDeltaMovement();
        user.setDeltaMovement(vel.add(dir.x * 1.8 - vel.x * 0.5, dir.y * 0.8 - vel.y, dir.z * 1.8 - vel.z * 0.5));
        user.hurtMarked = true;
    }
}