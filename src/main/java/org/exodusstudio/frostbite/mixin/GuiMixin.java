package org.exodusstudio.frostbite.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.Frostbite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Unique
    private static final ResourceLocation ARMOR_EMPTY_SPRITE = ResourceLocation.withDefaultNamespace("hud/armor_empty");
    @Unique
    private static final ResourceLocation ARMOR_HALF_SPRITE = ResourceLocation.withDefaultNamespace("hud/armor_half");
    @Unique
    private static final ResourceLocation ARMOR_FULL_SPRITE = ResourceLocation.withDefaultNamespace("hud/armor_full");

    @Inject(at = @At("HEAD"), method = "renderArmor", cancellable = true)
    private static void renderArmor(GuiGraphics guiGraphics, Player player, int y, int heartRows, int height, int x, CallbackInfo ci) {
        int i = player.getArmorValue();
        if (i > 0 || Frostbite.liningStorage.getLiningLevelForPlayer(player.getStringUUID()) > 0) {
            int j = y - (heartRows - 1) * height - 10;

            for (int k = 0; k < 10; ++k) {
                int l = x + k * 8;
                if (k * 2 + 1 < i) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_FULL_SPRITE, l, j, 9, 9);
                }

                if (k * 2 + 1 == i) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_HALF_SPRITE, l, j, 9, 9);
                }

                if (k * 2 + 1 > i) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_EMPTY_SPRITE, l, j, 9, 9);
                }
            }
        }
        ci.cancel();
    }
}
