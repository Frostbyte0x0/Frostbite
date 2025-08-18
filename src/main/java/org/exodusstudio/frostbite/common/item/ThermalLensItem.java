package org.exodusstudio.frostbite.common.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;

public class ThermalLensItem extends Item {
    public ThermalLensItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        player.playSound(SoundEvents.SPYGLASS_USE, 1.0F, 1.0F);
        player.awardStat(Stats.ITEM_USED.get(this));
        return ItemUtils.startUsingInstantly(level, player, usedHand);
    }

    @Override
    public int getUseDuration(ItemStack p_151222_, LivingEntity p_345399_) {
        return 1200;
    }
}