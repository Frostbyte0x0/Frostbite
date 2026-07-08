package org.exodusstudio.frostbite.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import org.exodusstudio.frostbite.common.item.weapons.ComboWeapon;
import org.exodusstudio.frostbite.common.util.PlayerWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class HudMixin {
    @Unique
    Hud frostbite$hud = (Hud) ((Object) this);

    @Unique
    private static final Identifier ARMOR_EMPTY_SPRITE = Identifier.withDefaultNamespace("hud/armor_empty");
    @Unique
    private static final Identifier ARMOR_HALF_SPRITE = Identifier.withDefaultNamespace("hud/armor_half");
    @Unique
    private static final Identifier ARMOR_FULL_SPRITE = Identifier.withDefaultNamespace("hud/armor_full");
    @Unique
    private static final Identifier CROSSHAIR_SPRITE = Identifier.withDefaultNamespace("hud/crosshair");


    @Inject(at = @At("HEAD"), method = "extractArmor", cancellable = true)
    private static void renderArmor(GuiGraphicsExtractor graphics, Player player, int yLineBase, int numHealthRows, int healthRowHeight, int xLeft, CallbackInfo ci) {
        int i = player.getArmorValue();
        if (i > 0 || ((PlayerWrapper) player).frostbite$getLiningLevel() > 0) {
            int j = yLineBase - (numHealthRows - 1) * healthRowHeight - 10;

            for (int k = 0; k < 10; ++k) {
                int l = xLeft + k * 8;
                if (k * 2 + 1 < i) {
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_FULL_SPRITE, l, j, 9, 9);
                }

                if (k * 2 + 1 == i) {
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_HALF_SPRITE, l, j, 9, 9);
                }

                if (k * 2 + 1 > i) {
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_EMPTY_SPRITE, l, j, 9, 9);
                }
            }
        }
        ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "extractCrosshair", cancellable = true)
    private void renderCrosshair(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();

        if (ComboWeapon.shouldShowComboOverlay(minecraft.player)) {
            Options options = minecraft.options;
            if (options.getCameraType().isFirstPerson()) {
                if (minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR || frostbite$hud.canRenderCrosshairForSpectator(minecraft.hitResult)) {
                    if (!minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.THREE_DIMENSIONAL_CROSSHAIR)) {
                        graphics.nextStratum();
                        graphics.blitSprite(RenderPipelines.CROSSHAIR, CROSSHAIR_SPRITE, (graphics.guiWidth() - 15) / 2, (graphics.guiHeight() - 15) / 2, 15, 15);
                    }
                }
            }
            ci.cancel();
        }
    }
}
