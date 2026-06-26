package org.exodusstudio.frostbite.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.exodusstudio.frostbite.common.inventory.LiningSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {
    @Unique
    private AbstractContainerScreen<?> frostbite$screen = (AbstractContainerScreen<?>) ((Object) this);

//    @Inject(at = @At("HEAD"), method = "extractSlot", cancellable = true)
//    protected void renderSlot(GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
//        if (frostbite$screen.getMenu() instanceof InventoryMenu && Frostbite.shouldShowLining && slot instanceof LiningSlot) {
//            int i = slot.x;
//            int j = slot.y;
//            ItemStack itemstack = slot.getItem();
//            boolean flag = false;
//            boolean flag1 = slot == frostbite$screen.clickedSlot && !frostbite$screen.draggingItem.isEmpty() && !frostbite$screen.isSplittingStack;
//            ItemStack itemstack1 = frostbite$screen.getMenu().getCarried();
//            String s = null;
//            if (slot == frostbite$screen.clickedSlot && !frostbite$screen.draggingItem.isEmpty() && frostbite$screen.isSplittingStack && !itemstack.isEmpty()) {
//                itemstack = itemstack.copyWithCount(itemstack.getCount() / 2);
//            } else if (frostbite$screen.isQuickCrafting && frostbite$screen.quickCraftSlots.contains(slot) && !itemstack1.isEmpty()) {
//                if (frostbite$screen.quickCraftSlots.size() == 1) {
//                    return;
//                }
//
//                if (AbstractContainerMenu.canItemQuickReplace(slot, itemstack1, true) && frostbite$screen.getMenu().canDragTo(slot)) {
//                    flag = true;
//                    int k = Math.min(itemstack1.getMaxStackSize(), slot.getMaxStackSize(itemstack1));
//                    int l = slot.getItem().isEmpty() ? 0 : slot.getItem().getCount();
//                    int i1 = AbstractContainerMenu.getQuickCraftPlaceCount(frostbite$screen.quickCraftSlots, frostbite$screen.quickCraftingType, itemstack1) + l;
//                    if (i1 > k) {
//                        i1 = k;
//                        s = ChatFormatting.YELLOW.toString() + k;
//                    }
//
//                    itemstack = itemstack1.copyWithCount(i1);
//                } else {
//                    frostbite$screen.quickCraftSlots.remove(slot);
//                    frostbite$screen.recalculateQuickCraftRemaining();
//                }
//            }
//
//            graphics.pose().translate(0.0F, 100.0F, graphics.pose().pushMatrix());
//            if (itemstack.isEmpty() && slot.isActive()) {
//                Identifier Identifier = slot.getNoItemIcon();
//                if (Identifier != null) {
//                    graphics.blit(RenderPipelines.GUI_TEXTURED, Identifier, i, j - 100, 0, 0, 16, 16, 16, 16);
//                    flag1 = true;
//                }
//            }
//
//            if (!flag1) {
//                if (flag) {
//                    graphics.fill(i, j - 100, i + 16, j - 100 + 16, -2130706433);
//                }
//
//                frostbite$screen.renderSlotContents(graphics, itemstack, slot, s);
//            }
//
//            graphics.pose().popMatrix();
//            ci.cancel();
//        }
//    }

    @Inject(at = @At("HEAD"), method = "renderSlotContents", cancellable = true)
    public void renderSlotContents(GuiGraphicsExtractor graphics, ItemStack itemStack, Slot slot, String itemCount, CallbackInfo ci) {
        if (slot instanceof LiningSlot) {
            int i = slot.x;
            int j = slot.y - 100;
            int j1 = slot.x + slot.y * 176;
            graphics.item(itemStack, i, j, j1);

            Font font = IClientItemExtensions.of(itemStack).getFont(itemStack, IClientItemExtensions.FontContext.ITEM_COUNT);
            graphics.itemDecorations(font != null ? font : Minecraft.getInstance().font, itemStack, i, j, itemCount);
            ci.cancel();
        }
    }
}
