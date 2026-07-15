package org.exodusstudio.frostbite.common.item.weapons.goat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.item.weapons.ComboWeapon;

import java.util.List;

public class NaginataItem extends ComboWeapon {
    public NaginataItem(Properties properties) {
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
        player.getCooldowns().addCooldown(this.getDefaultInstance(), 25);

        float yaw = player.getYRot() * ((float) Math.PI / 180f);

        BlockPos pos = BlockPos.containing(player.position().add(
                -Mth.sin(yaw) * 1.4, player.getEyeHeight() / 2.0f, Mth.cos(yaw) * 1.4f
        ));

        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class, new AABB(pos).inflate(3.0, 1.0, 3.0),
                (entity) -> entity != player);

//        stack.damage(1, user, EquipmentSlot.MAINHAND);

        targets.forEach(target -> {

            double distance = player.distanceToSqr(target);

            if (distance > 6.0 && distance < 36.0) {
                if (!(target instanceof ArmorStand)) {
                    target.push(
                            0.4,
                            Mth.sin(player.getYHeadRot() * 0.017453292F),
                            -Mth.cos(player.getYHeadRot() * 0.017453292F)
                    );
                }

                target.hurt(target.damageSources().playerAttack(player), 5.5f);
            }
        });
        player.swing(hand);
            level.playSound(
                    null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS,
                    1.0f,
                    0.9f + level.getRandom().nextFloat() * 0.2f
            );
            spawnSweepParticles(player);
            player.push(0, 0.05, 0);
        return InteractionResult.SUCCESS;
    }

    private void spawnSweepParticles(Player player) {
        if (!(player.level() instanceof ServerLevel world)) return;

        for (int i = 0; i <= 6; i++) {
            float angle = player.getYHeadRot() + (i * 20) - 60;
            float rad = angle * ((float) Math.PI / 180f);

            double d = -Mth.sin(rad) * 3.0;
            double e = Mth.cos(rad) * 3.0;

            world.sendParticles(ParticleTypes.SWEEP_ATTACK,
                    player.getX() + d, player.getY() + 0.5, player.getZ() + e,
                    0, d, 0.0, e, 0.0);
        }
    }
}
