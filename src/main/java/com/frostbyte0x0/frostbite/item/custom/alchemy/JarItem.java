//package com.frostbyte.frostbite.item.custom.alchemy;
//
//import com.frostbyte.frostbite.component.JarContentsData;
//import com.frostbyte.frostbite.component.ModDataComponentTypes;
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//
//public class JarItem extends Item {
//    public JarItem(Properties properties) {
//        super(properties);
//    }
//
//    public ItemStack getDefaultInstance() {
//        ItemStack itemstack = super.getDefaultInstance();
//        itemstack.set(ModDataComponentTypes.JAR_CONTENTS, new JarContentsData(Jars.WATER));
//        return itemstack;
//    }
//
//    public Component getName(ItemStack stack) {
//        JarContentsData jar_contents = (JarContentsData) stack.get(ModDataComponentTypes.JAR_CONTENTS);
//        return jar_contents != null ? jar_contents.getName(this.descriptionId + ".effect.") : super.getName(stack);
//    }
//}
