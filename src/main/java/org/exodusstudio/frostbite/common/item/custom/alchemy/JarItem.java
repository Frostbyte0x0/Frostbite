package org.exodusstudio.frostbite.common.item.custom.alchemy;

import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.common.component.JarContentsData;

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
}
