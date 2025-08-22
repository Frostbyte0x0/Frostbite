package org.exodusstudio.frostbite.client.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.Frostbite;

public class FireOverlay {
    private static final ResourceLocation FIRE0 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/fire/fire0.png");
    private static final ResourceLocation FIRE1 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/fire/fire1.png");
    private static final ResourceLocation FIRE2 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/fire/fire2.png");
    private static final ResourceLocation FIRE3 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/fire/fire3.png");
    private static final ResourceLocation FIRE4 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/fire/fire4.png");
    private static final ResourceLocation FIRE5 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/fire/fire5.png");
    private static final ResourceLocation FIRE6 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/fire/fire6.png");
    private static final ResourceLocation FIRE7 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/fire/fire7.png");

    public static final ResourceLocation[] FIRES = {FIRE0, FIRE1, FIRE2, FIRE3,
            FIRE4, FIRE5, FIRE6, FIRE7};


    public static void drawTexture(int leftPos, int topPos, int width, int height, ResourceLocation texture, boolean blend) {
        if (blend) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
        }

        //RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        new GuiGraphics(Minecraft.getInstance(), Minecraft.getInstance().renderBuffers().bufferSource()).blit(RenderType::guiTextured, texture, leftPos, topPos, 0f, 0f, width, height, width, height);

        if (blend) {
            RenderSystem.disableBlend();
        }
    }

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Player player = Minecraft.getInstance().player;
        assert player != null;


        float maxTemp = 20f;
        float minTemp = -60f;

        float innerTemp = (float) Math.round(Frostbite.savedTemperatures.getTemperature(player, true) * 10f) / 10f;

        if (!player.level().dimension().toString().equals("ResourceKey[minecraft:dimension / frostbite:frostbite]") && innerTemp == maxTemp) {
            return;
        }

        int fireToShow = (int) Math.floor(Math.clamp(FIRES.length * (innerTemp - minTemp) / (maxTemp - minTemp), 0, FIRES.length - 1));

        int x = 580; // 580
        int y = 470; // 470

        int textureWidth = 24;
        int textureHeight = 24;

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexture(x, y, textureWidth, textureHeight, FIRES[fireToShow], false);

        Font font = Minecraft.getInstance().font;
        Component text = Component.literal("§7" + innerTemp + "C").withStyle(ChatFormatting.RED);
        guiGraphics.drawString(font, text, (x + (textureWidth - font.width(text)) / 2), y - textureHeight + 11, 0xFFFFFF);
    }
}
