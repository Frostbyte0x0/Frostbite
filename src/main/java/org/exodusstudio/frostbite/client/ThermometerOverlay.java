package org.exodusstudio.frostbite.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import org.exodusstudio.frostbite.Frostbite;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.common.registry.AttachmentTypeRegistry;

public class ThermometerOverlay {
    private static final ResourceLocation THERMOMETER0 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer0.png");
    private static final ResourceLocation THERMOMETER1 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer1.png");
    private static final ResourceLocation THERMOMETER2 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer2.png");
    private static final ResourceLocation THERMOMETER3 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer3.png");
    private static final ResourceLocation THERMOMETER4 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer4.png");
    private static final ResourceLocation THERMOMETER5 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer5.png");
    private static final ResourceLocation THERMOMETER6 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer6.png");
    private static final ResourceLocation THERMOMETER7 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer7.png");

    public static final ResourceLocation[] THERMOMETERS = {THERMOMETER0, THERMOMETER1, THERMOMETER2, THERMOMETER3,
            THERMOMETER4, THERMOMETER5, THERMOMETER6, THERMOMETER7};


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

        float maxTemp = 20f;
        float minTemp = -60f;

        float outer_temp = player.getData(AttachmentTypeRegistry.OUTER_TEMPERATURE);

        int thermometerToShow = (int) Math.floor(Math.clamp(THERMOMETERS.length * (outer_temp - minTemp) / (maxTemp - minTemp), 0, THERMOMETERS.length - 1));

        int x = 370;
        int y = 470;

        int textureWidth = 24;
        int textureHeight = 24;

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexture(x, y, textureWidth, textureHeight, THERMOMETERS[thermometerToShow], false);

        Font font = Minecraft.getInstance().font;
        Component text = Component.literal("").append("§7" + outer_temp + "C").withStyle(ChatFormatting.BLUE);
        guiGraphics.drawString(font, text, (x + (textureWidth - font.width(text)) / 2), y - textureHeight + 11, 0xFFFFFF);
    }
}
