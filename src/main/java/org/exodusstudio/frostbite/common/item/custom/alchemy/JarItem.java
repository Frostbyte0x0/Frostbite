package org.exodusstudio.frostbite.common.item.custom.alchemy;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.component.JarContentsData;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;

public class JarItem extends Item {
    boolean used = false;

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
        if (used) {
            super.onUseTick(level, livingEntity, stack, remainingUseDuration);
            if (remainingUseDuration == 1) {
                livingEntity.addEffect(stack.get(DataComponentTypeRegistry.JAR_CONTENTS.get()).customEffects().getFirst());
            }
        }
        used = !used;
    }
}
