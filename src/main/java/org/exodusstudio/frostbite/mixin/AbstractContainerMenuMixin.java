package org.exodusstudio.frostbite.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.InventoryMenu;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.inventory.LiningSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {
    @Unique
    AbstractContainerMenu frostbite$container = (AbstractContainerMenu) ((Object) this);

    @Inject(method = "doClick", at = @At("HEAD"), cancellable = true)
    private void doClick(int slotId, int button, ClickType clickType, Player player, CallbackInfo ci) {
        assert Minecraft.getInstance().player != null;
        if (frostbite$container instanceof InventoryMenu && slotId >= 0 && slotId <= 50 &&
                frostbite$container.slots.get(slotId) instanceof LiningSlot liningSlot &&
                player instanceof ServerPlayer) {
            frostbite$container.setCarried(Minecraft.getInstance().player.containerMenu.getCarried());
            liningSlot.set(Frostbite.savedLinings.getSpecificLiningForPlayer(player.getStringUUID(), liningSlot.getSlot()));

            ci.cancel();
        }

//        assert Minecraft.getInstance().player != null;
//        if (frostbite$container instanceof InventoryMenu && slotId >= 0 && slotId <= 50 &&
//                frostbite$container.slots.get(slotId) instanceof LiningSlot liningSlot) {
//            if (player instanceof ServerPlayer) {
//                frostbite$container.setCarried(Minecraft.getInstance().player.containerMenu.getCarried());
//                liningSlot.set(Frostbite.savedLinings.getSpecificLiningForPlayer(player.getStringUUID(), liningSlot.getSlot()));
//            } else if (!frostbite$container.getCarried().equals(Minecraft.getInstance().player.containerMenu.getCarried())) {
//                frostbite$container.setCarried(Minecraft.getInstance().player.containerMenu.getCarried());
//            }
//
//            ci.cancel();
//        }
    }
}
