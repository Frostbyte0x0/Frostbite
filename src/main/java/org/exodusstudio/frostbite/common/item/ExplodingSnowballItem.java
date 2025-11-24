package org.exodusstudio.frostbite.common.item;

import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.entity.custom.projectiles.ExplodingSnowballProjectileEntity;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Random;

public class ExplodingSnowballItem extends Item {
    public ExplodingSnowballItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        ExplodingSnowballProjectileEntity entityArrow = new ExplodingSnowballProjectileEntity(EntityRegistry.EXPLODING_SNOWBALL_PROJECTILE_ENTITY.get(), player, player.level(), new ItemStack(ItemRegistry.EXPLODING_SNOWBALL.get()));

        double d0 = player.getLookAngle().y + (double) player.getEyeHeight() - 1.1;
        double d1 = player.getLookAngle().x - player.getX();
        double d3 = player.getLookAngle().z - player.getZ();

        entityArrow.shoot(d1, d0 - entityArrow.getY() + Math.sqrt(d1 * d1 + d3 * d3) * 0.2F, d3, 0.7f * 2, 12.0F);
        entityArrow.setSilent(true);

        player.level().addFreshEntity(entityArrow);

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GLASS_BREAK, SoundSource.HOSTILE, 1f, 1f / (new Random().nextFloat() * 0.5f + 1));
        return InteractionResult.CONSUME;
    }
}
