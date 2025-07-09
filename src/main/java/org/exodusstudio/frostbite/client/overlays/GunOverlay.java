package org.exodusstudio.frostbite.client.overlays;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
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
    private static final ResourceLocation SNIPER_SCOPE_LOCATION =
            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/overlays/sniper_overlay.png");
    private static boolean isAiming = true;

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        assert player != null;

        float f = deltaTracker.getGameTimeDeltaTicks();
        scopeScale = Mth.lerp(0.5F * f, scopeScale, 1.125F);
        if (mc.options.getCameraType().isFirstPerson()) {
            if (player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistry.SNIPER) && player.isShiftKeyDown()) {
                renderSpyglassOverlay(guiGraphics, scopeScale);
            } else {
                scopeScale = 0.5F;

                for (EquipmentSlot equipmentslot : EquipmentSlot.values()) {
                    ItemStack itemstack = player.getItemBySlot(equipmentslot);
                    Equippable equippable = itemstack.get(DataComponents.EQUIPPABLE);
                    if (equippable != null && equippable.slot() == equipmentslot && equippable.cameraOverlay().isPresent()) {
                        renderTextureOverlay(guiGraphics, equippable.cameraOverlay().get().withPath(p_380782_ -> "textures/" + p_380782_ + ".png"), 1.0F);
                    }
                }
            }
        }
    }

    private static void renderSpyglassOverlay(GuiGraphics guiGraphics, float scopeScale) {
        float f = (float)Math.min(guiGraphics.guiWidth(), guiGraphics.guiHeight());
        float f1 = Math.min((float)guiGraphics.guiWidth() / f, (float)guiGraphics.guiHeight() / f) * scopeScale;
        int i = Mth.floor(f * f1);
        int j = Mth.floor(f * f1);
        int k = (guiGraphics.guiWidth() - i) / 2;
        int l = (guiGraphics.guiHeight() - j) / 2;
        int i1 = k + i;
        int j1 = l + j;
        guiGraphics.blit(RenderType::guiTextured, SNIPER_SCOPE_LOCATION, k, l, 0.0F, 0.0F, i, j, i, j);
        guiGraphics.fill(RenderType.guiOverlay(), 0, j1, guiGraphics.guiWidth(), guiGraphics.guiHeight(), -90, -16777216);
        guiGraphics.fill(RenderType.guiOverlay(), 0, 0, guiGraphics.guiWidth(), l, -90, -16777216);
        guiGraphics.fill(RenderType.guiOverlay(), 0, l, k, j1, -90, -16777216);
        guiGraphics.fill(RenderType.guiOverlay(), i1, l, guiGraphics.guiWidth(), j1, -90, -16777216);
    }

    private static void renderTextureOverlay(GuiGraphics guiGraphics, ResourceLocation shaderLocation, float alpha) {
        int i = ARGB.white(alpha);
        guiGraphics.blit(
                RenderType::guiTexturedOverlay,
                shaderLocation,
                0,
                0,
                0.0F,
                0.0F,
                guiGraphics.guiWidth(),
                guiGraphics.guiHeight(),
                guiGraphics.guiWidth(),
                guiGraphics.guiHeight(),
                i
        );
    }

    public void setAiming(boolean aiming) {
        isAiming = aiming;
    }
}
