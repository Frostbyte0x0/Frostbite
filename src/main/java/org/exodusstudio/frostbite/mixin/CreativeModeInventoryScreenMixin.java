package org.exodusstudio.frostbite.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import org.exodusstudio.frostbite.Frostbite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin {
    @Unique
    CreativeModeInventoryScreen frostbite$screen = (CreativeModeInventoryScreen) ((Object) this);
    @Unique
    CycleButton<Boolean> frostbite$cycleButton;

    @Inject(at = @At("HEAD"), method = "selectTab")
    private void selectTab(CreativeModeTab tab, CallbackInfo ci) {
        if (frostbite$setScreen()) return;
        if (tab.getType() == CreativeModeTab.Type.INVENTORY) {
            frostbite$refreshButton();
            frostbite$screen.addRenderableWidget(frostbite$cycleButton);
        } else {
            if (frostbite$screen.renderables.contains(frostbite$cycleButton))
                frostbite$screen.removeWidget(frostbite$cycleButton);
        }
    }

    @Inject(at = @At("HEAD"), method = "resize")
    private void resize(Minecraft minecraft, int width, int height, CallbackInfo ci) {
        frostbite$screen.removeWidget(frostbite$cycleButton);
        frostbite$refreshButton();
        frostbite$screen.addRenderableWidget(frostbite$cycleButton);
    }

    @Unique
    private boolean frostbite$setScreen() {
        frostbite$screen = (CreativeModeInventoryScreen) ((Object) this);
        return frostbite$screen == null;
    }

    @Unique
    private void frostbite$refreshButton() {
        frostbite$cycleButton = CycleButton
                .booleanBuilder(Component.literal("A"), Component.literal("L"))
                .displayOnlyValue()
                .withInitialValue(!Frostbite.shouldShowLining)
                .create(frostbite$screen.getGuiLeft() + 126, frostbite$screen.getGuiTop() + 33, 16, 16, Component.literal("A"),
                        (button, bool) -> Frostbite.shouldShowLining = !bool);
    }
}
