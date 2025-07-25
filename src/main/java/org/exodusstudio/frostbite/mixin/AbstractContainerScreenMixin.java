package org.exodusstudio.frostbite.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.inventory.LiningSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {
    @Unique
    private AbstractContainerScreen frostbite$screen = (AbstractContainerScreen) ((Object) this);

    @Inject(at = @At("HEAD"), method = "renderSlot", cancellable = true)
    protected void renderSlot(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        if (frostbite$screen.getMenu() instanceof InventoryMenu && Frostbite.shouldShowLining && slot instanceof LiningSlot) {
            int i = slot.x;
            int j = slot.y;
            ItemStack itemstack = slot.getItem();
            boolean flag = false;
            boolean flag1 = slot == frostbite$screen.clickedSlot && !frostbite$screen.draggingItem.isEmpty() && !frostbite$screen.isSplittingStack;
            ItemStack itemstack1 = frostbite$screen.getMenu().getCarried();
            String s = null;
            if (slot == frostbite$screen.clickedSlot && !frostbite$screen.draggingItem.isEmpty() && frostbite$screen.isSplittingStack && !itemstack.isEmpty()) {
                itemstack = itemstack.copyWithCount(itemstack.getCount() / 2);
            } else if (frostbite$screen.isQuickCrafting && frostbite$screen.quickCraftSlots.contains(slot) && !itemstack1.isEmpty()) {
                if (frostbite$screen.quickCraftSlots.size() == 1) {
                    return;
                }

                if (AbstractContainerMenu.canItemQuickReplace(slot, itemstack1, true) && frostbite$screen.getMenu().canDragTo(slot)) {
                    flag = true;
                    int k = Math.min(itemstack1.getMaxStackSize(), slot.getMaxStackSize(itemstack1));
                    int l = slot.getItem().isEmpty() ? 0 : slot.getItem().getCount();
                    int i1 = AbstractContainerMenu.getQuickCraftPlaceCount(frostbite$screen.quickCraftSlots, frostbite$screen.quickCraftingType, itemstack1) + l;
                    if (i1 > k) {
                        i1 = k;
                        s = ChatFormatting.YELLOW.toString() + k;
                    }

                    itemstack = itemstack1.copyWithCount(i1);
                } else {
                    frostbite$screen.quickCraftSlots.remove(slot);
                    frostbite$screen.recalculateQuickCraftRemaining();
                }
            }

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, 0.0F, 100.0F);
            if (itemstack.isEmpty() && slot.isActive()) {
                ResourceLocation resourcelocation = slot.getNoItemIcon();
                if (resourcelocation != null) {
                    guiGraphics.blit(RenderType::guiTextured, resourcelocation, i, j, 0, 0, 16, 16, 16, 16);
                    flag1 = true;
                }
            }

            if (!flag1) {
                if (flag) {
                    guiGraphics.fill(i, j, i + 16, j + 16, -2130706433);
                }

                frostbite$screen.renderSlotContents(guiGraphics, itemstack, slot, s);
            }

            guiGraphics.pose().popPose();
            ci.cancel();
        }
    }
}
