package org.exodusstudio.frostbite.common.item.custom.alchemy;

import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.component.ChargeData;
import org.exodusstudio.frostbite.common.component.FillLevelData;
import org.exodusstudio.frostbite.common.component.JarContentsData;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;

public class JarItem extends Item {
    public JarItem(Properties properties) {
        super(properties);
    }

    public ItemStack getDefaultInstance() {
        ItemStack itemstack = super.getDefaultInstance();
        itemstack.set(DataComponentTypeRegistry.JAR_CONTENTS, new JarContentsData(Jars.WATER));
        return itemstack;
    }

    public Component getName(ItemStack stack) {
        JarContentsData jar_contents = stack.get(DataComponentTypeRegistry.JAR_CONTENTS);
        return jar_contents != null ? jar_contents.getName(this.descriptionId + ".effect.") : super.getName(stack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (remainingUseDuration == 1) {
            JarContentsData jarContentsData = stack.get(DataComponentTypeRegistry.JAR_CONTENTS.get());
            stack.set(DataComponentTypeRegistry.FILL_LEVEL.get(), new FillLevelData(stack.get(DataComponentTypeRegistry.FILL_LEVEL).level() - 1));

            InteractionHand hand = livingEntity.getItemInHand(InteractionHand.MAIN_HAND).is(this) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;

            if (stack.get(DataComponentTypeRegistry.FILL_LEVEL).level() <= 0) {
                livingEntity.setItemInHand(hand, new ItemStack(ItemRegistry.EMPTY_JAR.asItem()));
                return;
            }

            if (jarContentsData.jar().get().is(Jars.CURING)) {
                livingEntity.removeAllEffects();
            } else {
                livingEntity.addEffect(jarContentsData.customEffects().getFirst());
            }
        }
    }
}
