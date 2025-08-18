package org.exodusstudio.frostbite.common.item;

import org.exodusstudio.frostbite.common.entity.custom.PackedHardenedSnowballProjectileEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PackedHardenedSnowballItem extends Item {
    public PackedHardenedSnowballItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        if (level instanceof ServerLevel serverlevel) {
            Projectile.spawnProjectileFromRotation(PackedHardenedSnowballProjectileEntity::new, serverlevel, itemstack, player, 0.0f, 1.5f, 1.0f);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        itemstack.consume(1, player);
        return InteractionResult.SUCCESS;
    }
}
