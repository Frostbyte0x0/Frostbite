package org.exodusstudio.frostbite.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.RemoteSlot;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.inventory.LiningSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {
    @Unique
    AbstractContainerMenu frostbite$container = (AbstractContainerMenu) ((Object) this);
    @Unique
    boolean frostbite$shouldSync = false;
    @Unique
    int frostbite$syncCount = 2;

    @Inject(method = "doClick", at = @At("HEAD"), cancellable = true)
    private void doClick(int slotId, int button, ClickType clickType, Player player, CallbackInfo ci) {
        assert Minecraft.getInstance().player != null;
        if (frostbite$container instanceof InventoryMenu && slotId >= 0 && slotId <= 50 &&
                frostbite$container.slots.get(slotId) instanceof LiningSlot liningSlot &&
                player instanceof ServerPlayer) {
            frostbite$container.setCarried(Minecraft.getInstance().player.containerMenu.getCarried());
            liningSlot.set(Frostbite.liningStorage.getSpecificLiningForPlayer(player.getStringUUID(), liningSlot.getSlot()));
            frostbite$shouldSync = true;
            frostbite$syncCount = 2;

            ci.cancel();
        }
    }

    @Inject(method = "synchronizeSlotToRemote", at = @At("HEAD"), cancellable = true)
    private void synchronizeSlotToRemote(int slotIndex, ItemStack stack, Supplier<ItemStack> supplier, CallbackInfo ci) {
        if (!frostbite$container.suppressRemoteUpdates) {
            if (Minecraft.getInstance().player != null) {
                ItemStack liningStack = Minecraft.getInstance().player.containerMenu.getSlot(slotIndex).getItem();
                if (frostbite$shouldSync) {
                    RemoteSlot remoteslot = frostbite$container.remoteSlots.get(slotIndex);
                    if (!remoteslot.matches(stack)) {
                        remoteslot.force(stack);
                        if (frostbite$container.synchronizer != null) {
                            frostbite$container.synchronizer.sendSlotChange(frostbite$container, slotIndex, liningStack);
                            frostbite$container.setItem(slotIndex, frostbite$container.incrementStateId(), liningStack);
                            frostbite$syncing();
                        }
                    }
                    ci.cancel();
                }
            }
        }
    }

    @Inject(method = "synchronizeCarriedToRemote", at = @At("HEAD"), cancellable = true)
    private void synchronizeCarriedToRemote(CallbackInfo ci) {
        if (!frostbite$container.suppressRemoteUpdates) {
            if (Minecraft.getInstance().player != null) {
                ItemStack liningStack = Minecraft.getInstance().player.containerMenu.getCarried();

                if (frostbite$shouldSync) {
                    ItemStack itemstack = frostbite$container.getCarried();
                    if (!frostbite$container.remoteCarried.matches(itemstack)) {
                        frostbite$container.remoteCarried.force(itemstack);
                        if (frostbite$container.synchronizer != null) {
                            frostbite$container.synchronizer.sendCarriedChange(frostbite$container, liningStack);
                            frostbite$container.setCarried(liningStack);
                            frostbite$syncing();
                        }
                    }
                    ci.cancel();
                }
            }
        }
    }

    @Unique
    private void frostbite$syncing() {
        frostbite$syncCount--;
        if (frostbite$syncCount <= 0) {
            frostbite$shouldSync = false;
        }
    }
}
