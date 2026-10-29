package org.exodusstudio.frostbite.common.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerPlayer;
import org.exodusstudio.frostbite.common.combat.ParryManager;
import org.exodusstudio.frostbite.common.item.weapons.elf.ModeWeapon;

import java.util.Arrays;

public class ServerPayloadHandler {
    public static void handleParry(final ParryPayload data, final NetworkManager.PacketContext context) {
        context.queue(() -> {
            if (context.getPlayer() instanceof ServerPlayer player) {
                if (data.start()) ParryManager.start(player);
            }
        });
    }
    public static void handleDataOnMain(final StaffPayload data, final NetworkManager.PacketContext context) {
        context.queue(() -> {
            LivingEntity owner = (LivingEntity) context.getPlayer().level().getEntity(data.staffInfo().uuid());
            if (owner != null && !ParryManager.isParrying(owner)
                    && owner.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ModeWeapon staff) {
                if (Arrays.asList(staff.getModes()).contains(data.staffInfo().mode())) {
                    staff.setMode(owner.getItemInHand(InteractionHand.MAIN_HAND),
                            Arrays.asList(staff.getModes()).indexOf(data.staffInfo().mode()));
                }
                if (data.staffInfo().cast()) staff.attack(owner.level(), owner);
            }
        });
    }
}
