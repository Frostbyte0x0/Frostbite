package org.exodusstudio.frostbite.client.overlays;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.EffectRegistry;

public class RageOverlay {
    private static final ResourceLocation RAGE = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/rage.png");
    private static final ResourceLocation VIGNETTE_LOCATION = ResourceLocation.withDefaultNamespace("textures/misc/vignette.png");

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Player player = Minecraft.getInstance().player;

        assert player != null;
        if (player.hasEffect(EffectRegistry.RAGE)) {
            guiGraphics.blit(
                    RenderType::guiTexturedOverlay,
                    RAGE,
                    0,
                    0,
                    0.0F,
                    0.0F,
                    guiGraphics.guiWidth(),
                    guiGraphics.guiHeight(),
                    guiGraphics.guiWidth(),
                    guiGraphics.guiHeight(),
                    ARGB.color(1000, 256, 256));

            guiGraphics.blit(
                    RenderType::vignette,
                    VIGNETTE_LOCATION,
                    0,
                    0,
                    0.0F,
                    0.0F,
                    guiGraphics.guiWidth(),
                    guiGraphics.guiHeight(),
                    guiGraphics.guiWidth(),
                    guiGraphics.guiHeight(),
                    ARGB.colorFromFloat(1, 0, 10, 10));
        }
    }
}
