package org.exodusstudio.frostbite.client.gui.scribing;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.common.contracts.ContractAttribute;
import org.exodusstudio.frostbite.common.contracts.LivingContractInfo;

public class DecipheringResultSlot extends Slot {
    Player player;

    public DecipheringResultSlot(Player player, Container container, int slot, int x, int y) {
        super(container, slot, x, y);
        this.player = player;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return false;
    }

    @Override
    public void onTake(Player player, ItemStack carried) {
        ContractAttribute a = ContractAttribute.getAttribute(carried);
        if (a != null) LivingContractInfo.addDiscoveredAttribute(player, a);
        player.level().playLocalSound(
                player.getX(), player.getY(), player.getZ(), SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS,
                1.0F, 0.8F + 0.4F * player.getRandom().nextFloat(), false
        );
        super.onTake(player, carried);
    }
}
