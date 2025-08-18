package org.exodusstudio.frostbite.common.item;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;

import java.util.Arrays;

public class ConfettiPopperItem extends Item {
    private static RandomSource random = RandomSource.create();

    public ConfettiPopperItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        level.playSound(null, player.blockPosition(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 1f, random.nextFloat() * 0.35f + 0.75f);
        level.playSound(null, player.blockPosition(), SoundEvents.FIREWORK_ROCKET_TWINKLE, SoundSource.PLAYERS, 1f, random.nextFloat() * 0.35f + 0.75f);
        if (level.isClientSide) {
            float x = player.getXRot();
            float y = player.getYRot();
            float z = 0f;
            float velocity;
            float inaccuracy = 25f;
            for (int i = 0; i < 80; i++) {
                velocity = 1f + random.nextFloat() * 0.3f;

                float f = -Mth.sin(y * ((float)Math.PI / 180F)) * Mth.cos(x * ((float)Math.PI / 180F));
                float f1 = -Mth.sin((x + z) * ((float)Math.PI / 180F));
                float f2 = Mth.cos(y * ((float)Math.PI / 180F)) * Mth.cos(x * ((float)Math.PI / 180F));

                Vec3 vec3 = player.getKnownMovement();
                Vec3 vec31 = (new Vec3(f, f1, f2)).normalize().add(
                        random.triangle(0.0F, 0.0172275 * inaccuracy),
                        random.triangle(0.0F, 0.0172275 * inaccuracy),
                        random.triangle(0.0F, 0.0172275 * inaccuracy)).scale(velocity);
                Vec3 vec32 = vec31.add(vec3.x, vec3.y, vec3.z);


                int[] colour = new int[]{
                        random.nextInt(80) + 150,
                        random.nextInt(80) + 150,
                        random.nextInt(80) + 150};
                if (random.nextBoolean()) {
                    int s = (int) (random.nextFloat() * 60) + 60;
                    Arrays.sort(colour);
                    colour[0] -= s;
                    colour[2] += s;
                }
                int index = random.nextInt(3);

                player.level().addAlwaysVisibleParticle(
                        ColorParticleOption.create(ParticleRegistry.CONFETTI_PARTICLE.get(), ARGB.color(
                                colour[index % 3],
                                colour[(index + 1) % 3],
                                colour[(index + 2) % 3])),
                        player.getX(),
                        player.getEyeY() - 0.25,
                        player.getZ(),
                        vec32.x,
                        vec32.y,
                        vec32.z);
            }

        }

        player.getItemInHand(hand).hurtAndBreak(1, player, hand.equals(InteractionHand.MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
        return InteractionResult.SUCCESS;
    }
}
