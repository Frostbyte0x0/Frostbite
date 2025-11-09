package org.exodusstudio.frostbite.common.network;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.exodusstudio.frostbite.common.item.weapons.elf.AbstractStaff;

public class ServerPayloadHandler {
    public static void handleDataOnMain(final StaffPayload data, final IPayloadContext context) {
        LivingEntity owner = (LivingEntity) context.player().level().getEntity(data.staffInfo().uuid());
        if (owner != null) {
            AbstractStaff staff = ((AbstractStaff) owner.getItemInHand(InteractionHand.MAIN_HAND).getItem());
            staff.mode = data.staffInfo().mode();
            staff.attack(owner.level(), owner);
        }
    }
}
