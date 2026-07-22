package org.exodusstudio.frostbite.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import org.exodusstudio.frostbite.common.registry.AttachmentRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {
    @Unique
    InventoryScreen frostbite$screen = (InventoryScreen) ((Object) this);
    @Unique
    CycleButton<Boolean> frostbite$cycleButton;

    @Inject(at = @At("HEAD"), method = "init")
    private void init(CallbackInfo ci) {
        if (frostbite$setScreen()) return;
        frostbite$refreshButton();
        frostbite$screen.addRenderableWidget(frostbite$cycleButton);
    }

    @Inject(at = @At("HEAD"), method = "extractBackground")
    private void render(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        frostbite$cycleButton.setX(frostbite$screen.getLeftPos() + 76);
        frostbite$cycleButton.setY(frostbite$screen.getTopPos() + 44);
    }

    @Unique
    private boolean frostbite$setScreen() {
        frostbite$screen = (InventoryScreen) ((Object) this);
        return frostbite$screen == null;
    }

    @Unique
    private void frostbite$refreshButton() {
        frostbite$cycleButton = CycleButton
                .booleanBuilder(Component.literal("A"), Component.literal("L"), !frostbite$screen.getMinecraft().player.getData(AttachmentRegistry.SHOW_LINING))
                .displayOnlyValue()
                .create(frostbite$screen.getLeftPos() + 468, frostbite$screen.getTopPos() + 208, 16, 16,
                        Component.literal("A"), (button, bool) -> Minecraft.getInstance().player.setData(AttachmentRegistry.SHOW_LINING, !bool));
    }
}
