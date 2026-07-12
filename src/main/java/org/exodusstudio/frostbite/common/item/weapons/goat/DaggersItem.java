package org.exodusstudio.frostbite.common.item.weapons.goat;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.item.weapons.ComboWeapon;

public class DaggersItem extends ComboWeapon {
    public DaggersItem(Properties properties) {
        super(properties,
                new ComboStep(3, 0.75f, 1.75f, 3),
                new ComboStep(4, 0.75f, 1.75f, 3),
                new ComboStep(5, 0.75f, 1.75f, 3));
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 3.0,
                        AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        player.startUsingItem(hand);

        if (!level.isClientSide()) {
            player.getCooldowns().addCooldown(this.getDefaultInstance(), 25);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.SNOW_HIT, SoundSource.PLAYERS, 1.0f, 1);

            ((ServerLevel) level).sendParticles(ParticleTypes.CLOUD,
                    player.getX(), player.getY(), player.getZ(),
                    6,
                    0.2, 0.2, 0.2,
                    0.02);
            ((ServerLevel) level).sendParticles(ParticleTypes.FLAME,
                    player.getX(), player.getY(), player.getZ(),
                    6,
                    0.2, 0.2, 0.2,
                    0.02);
        }
        Vec3 dir = player.getLookAngle();
        Vec3 vel = player.getDeltaMovement();
        player.setDeltaMovement(vel.add(dir.x * 1.8 - vel.x * 0.5, dir.y * 0.8 - vel.y, dir.z * 1.8 - vel.z * 0.5));
        player.hurtMarked = true;

        return InteractionResult.PASS;
    }
}