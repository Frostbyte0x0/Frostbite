package org.exodusstudio.frostbite.client.overlays;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.EffectRegistry;

public class RageOverlay {
    private static final Identifier RAGE = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/rage.png");
    private static final Identifier VIGNETTE_LOCATION = Identifier.withDefaultNamespace("textures/misc/vignette.png");

    public static void render(GuiGraphicsExtractor GuiGraphicsExtractor, DeltaTracker deltaTracker) {
        Player player = Minecraft.getInstance().player;

        assert player != null;
        if (player.hasEffect(EffectRegistry.RAGE)) {
            GuiGraphicsExtractor.blit(
                    RenderPipelines.GUI_TEXTURED,
                    RAGE,
                    0,
                    0,
                    0.0F,
                    0.0F,
                    GuiGraphicsExtractor.guiWidth(),
                    GuiGraphicsExtractor.guiHeight(),
                    GuiGraphicsExtractor.guiWidth(),
                    GuiGraphicsExtractor.guiHeight(),
                    ARGB.color(1000, 256, 256));

            GuiGraphicsExtractor.blit(
                    RenderPipelines.VIGNETTE,
                    VIGNETTE_LOCATION,
                    0,
                    0,
                    0.0F,
                    0.0F,
                    GuiGraphicsExtractor.guiWidth(),
                    GuiGraphicsExtractor.guiHeight(),
                    GuiGraphicsExtractor.guiWidth(),
                    GuiGraphicsExtractor.guiHeight(),
                    ARGB.colorFromFloat(1, 0, 10, 10));
        }
    }
}
