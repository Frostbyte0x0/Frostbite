package org.exodusstudio.frostbite.client.overlays;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;

public class GunOverlay {
    private static float scopeScale;
    private static final Identifier SNIPER_SCOPE_LOCATION =
            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/overlays/sniper_overlay.png");

    public static void render(GuiGraphicsExtractor GuiGraphicsExtractor, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        assert player != null;

        float f = deltaTracker.getGameTimeDeltaTicks();
        scopeScale = Mth.lerp(0.5F * f, scopeScale, 1.125F);
        if (mc.options.getCameraType().isFirstPerson()) {
            if (player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistry.SNIPER) && player.isShiftKeyDown()) {
                renderSpyglassOverlay(GuiGraphicsExtractor, scopeScale);
            } else {
                scopeScale = 0.5F;

                for (EquipmentSlot equipmentslot : EquipmentSlot.values()) {
                    ItemStack itemstack = player.getItemBySlot(equipmentslot);
                    Equippable equippable = itemstack.get(DataComponents.EQUIPPABLE);
                    if (equippable != null && equippable.slot() == equipmentslot && equippable.cameraOverlay().isPresent()) {
                        renderTextureOverlay(GuiGraphicsExtractor, equippable.cameraOverlay().get().withPath(p_380782_ -> "textures/" + p_380782_ + ".png"), 1.0F);
                    }
                }
            }
        }
    }

    private static void renderSpyglassOverlay(GuiGraphicsExtractor GuiGraphicsExtractor, float scopeScale) {
        float f = (float)Math.min(GuiGraphicsExtractor.guiWidth(), GuiGraphicsExtractor.guiHeight());
        float f1 = Math.min((float)GuiGraphicsExtractor.guiWidth() / f, (float)GuiGraphicsExtractor.guiHeight() / f) * scopeScale;
        int i = Mth.floor(f * f1);
        int j = Mth.floor(f * f1);
        int k = (GuiGraphicsExtractor.guiWidth() - i) / 2;
        int l = (GuiGraphicsExtractor.guiHeight() - j) / 2;
        int i1 = k + i;
        int j1 = l + j;
        GuiGraphicsExtractor.blit(RenderPipelines.GUI_TEXTURED, SNIPER_SCOPE_LOCATION, k, l, 0.0F, 0.0F, i, j, i, j);
        GuiGraphicsExtractor.fill(RenderPipelines.GUI, 0, j1, GuiGraphicsExtractor.guiWidth(), GuiGraphicsExtractor.guiHeight(), -16777216);
        GuiGraphicsExtractor.fill(RenderPipelines.GUI, 0, 0, GuiGraphicsExtractor.guiWidth(), l, -16777216);
        GuiGraphicsExtractor.fill(RenderPipelines.GUI, 0, l, k, j1, -16777216);
        GuiGraphicsExtractor.fill(RenderPipelines.GUI, i1, l, GuiGraphicsExtractor.guiWidth(), j1, -16777216);
    }

    private static void renderTextureOverlay(GuiGraphicsExtractor GuiGraphicsExtractor, Identifier shaderLocation, float alpha) {
        int i = ARGB.white(alpha);
        GuiGraphicsExtractor.blit(
                RenderPipelines.GUI_TEXTURED,
                shaderLocation,
                0,
                0,
                0.0F,
                0.0F,
                GuiGraphicsExtractor.guiWidth(),
                GuiGraphicsExtractor.guiHeight(),
                GuiGraphicsExtractor.guiWidth(),
                GuiGraphicsExtractor.guiHeight(),
                i
        );
    }
}
