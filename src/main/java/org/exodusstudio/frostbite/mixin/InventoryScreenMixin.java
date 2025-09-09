package org.exodusstudio.frostbite.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import org.exodusstudio.frostbite.Frostbite;
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

    @Inject(at = @At("HEAD"), method = "render")
    private void render(GuiGraphics p_283246_, int p_98876_, int p_98877_, float p_98878_, CallbackInfo ci) {
        frostbite$cycleButton.setX(frostbite$screen.getGuiLeft() + 76);
        frostbite$cycleButton.setY(frostbite$screen.getGuiTop() + 44);
    }

    @Unique
    private boolean frostbite$setScreen() {
        frostbite$screen = (InventoryScreen) ((Object) this);
        return frostbite$screen == null;
    }

    @Unique
    private void frostbite$refreshButton() {
        frostbite$cycleButton = CycleButton
                .booleanBuilder(Component.literal("A"), Component.literal("L"))
                .displayOnlyValue()
                .withInitialValue(!Frostbite.shouldShowLining)
                .create(frostbite$screen.getGuiLeft() + 468, frostbite$screen.getGuiTop() + 208, 16, 16,
                        Component.literal("A"), (button, bool) -> Frostbite.shouldShowLining = !bool);
    }
}
