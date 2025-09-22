package org.exodusstudio.frostbite.common.network;

import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.exodusstudio.frostbite.common.item.weapons.elf_weapons.AbstractStaff;

public class ServerPayloadHandler {
    public static void handleDataOnMain(final StaffData data, final IPayloadContext context) {
        AbstractStaff staff = ((AbstractStaff) context.player().getItemInHand(InteractionHand.MAIN_HAND).getItem());
        staff.mode = data.mode();
        staff.attack(context.player().level(), context.player());
    }
}
