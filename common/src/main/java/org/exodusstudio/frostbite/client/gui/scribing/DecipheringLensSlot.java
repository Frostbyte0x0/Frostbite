package org.exodusstudio.frostbite.client.gui.scribing;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.common.item.ThermalLensItem;

public class DecipheringLensSlot extends Slot {
    public DecipheringLensSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return itemStack.getItem() instanceof ThermalLensItem;
    }
}
