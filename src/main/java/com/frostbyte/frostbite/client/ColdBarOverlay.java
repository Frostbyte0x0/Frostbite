package com.frostbyte.frostbite.client;

import com.frostbyte.frostbite.Frostbite;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class ColdBarOverlay {
    private static final ResourceLocation THERMOMETER0 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/cold_bar/thermometer0.png");
    private static final ResourceLocation THERMOMETER1 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/cold_bar/thermometer1.png");
    private static final ResourceLocation THERMOMETER2 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/cold_bar/thermometer2.png");
    private static final ResourceLocation THERMOMETER3 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/cold_bar/thermometer3.png");
    private static final ResourceLocation THERMOMETER4 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/cold_bar/thermometer4.png");
    private static final ResourceLocation THERMOMETER5 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/cold_bar/thermometer5.png");
    private static final ResourceLocation THERMOMETER6 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/cold_bar/thermometer6.png");
    private static final ResourceLocation THERMOMETER7 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/cold_bar/thermometer7.png");

    public static final ResourceLocation[] THERMOMETERS = {THERMOMETER0, THERMOMETER1, THERMOMETER2, THERMOMETER3,
            THERMOMETER4, THERMOMETER5, THERMOMETER6, THERMOMETER7};


    public static void render(GuiGraphics graphics, float partialTick) {
        Player player = Minecraft.getInstance().player;

        int x = 5;
        int y = 5;

        int textureWidth = 16;
        int textureHeight = 16;

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexture(x, y, textureWidth, textureHeight, THERMOMETER0, false);
        //drawVertical(graphics, x, y, textureWidth, textureHeight, oxygen, maxOxygen, OXYGEN_TANK_FULL, false);

        //Font font = mc.font;
        //Component text = Component.translatable("general." + Frostbite.MOD_ID + ".oxygen").append(": ").withStyle(ChatFormatting.BLUE).append("§7" + oxygen / (maxOxygen / 100) + "%");
        //graphics.drawString(font, text, (x + (textureWidth - font.width(text)) / 2), y + textureHeight + 3, 0xFFFFFF);
    }

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

//    public static void drawVertical(GuiGraphics graphics, int leftPos, int topPos, int width, int height, double min, double max, ResourceLocation resourceLocation, boolean blend) {
//        double ratio = min / max;
//        int ratioHeight = (int) Math.ceil(height * ratio);
//        int remainHeight = height - ratioHeight;
//
//        if (blend) {
//            RenderSystem.enableBlend();
//            RenderSystem.defaultBlendFunc();
//        }
//
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderTexture(0, resourceLocation);
//        renderWithFloat.blit(graphics.pose(), leftPos, topPos + remainHeight, 0, remainHeight, width, ratioHeight, width, height);
//
//        if (blend) {
//            RenderSystem.disableBlend();
//        }
//    }

}
