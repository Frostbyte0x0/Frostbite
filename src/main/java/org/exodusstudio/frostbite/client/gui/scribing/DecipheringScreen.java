package org.exodusstudio.frostbite.client.gui.scribing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.contracts.LivingContractInfo;
import org.exodusstudio.frostbite.common.contracts.Literacy;
import org.exodusstudio.frostbite.common.registry.AttachmentRegistry;

import java.util.List;

public class DecipheringScreen extends AbstractRecipeBookScreen<DecipheringMenu> {
    private static final Identifier ERROR_SPRITE = Identifier.withDefaultNamespace("container/anvil/error");
    private static final Identifier BURN_PROGRESS_SPRITE = Identifier.withDefaultNamespace("container/furnace/burn_progress");
    private static final Identifier SCRIBING_TABLE_LOCATION = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/gui/deciphering_table.png");
    private static final Component TITLE = Component.translatable("container.deciphering_table");
    private final Player player;

    public DecipheringScreen(DecipheringMenu menu, Inventory playerInventory, Component title) {
        super(menu, new BSRecipeBookComponent(menu), playerInventory, Component.empty());
        this.player = playerInventory.player;
        this.titleLabelX = (180 - Minecraft.getInstance().font.width(title)) / 2;
        this.titleLabelY = -Minecraft.getInstance().font.lineHeight - 2;
    }

    @Override
    protected ScreenPosition getRecipeBookButtonPosition() {
        return new ScreenPosition(-100, -100);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.key() == 256) {
            this.player.closeContainer();
        }
        return super.keyPressed(event);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int xo = leftPos;
        int yo = topPos;
        graphics.blit(RenderPipelines.GUI_TEXTURED, SCRIBING_TABLE_LOCATION, xo, yo, 0, 0, imageWidth, imageHeight, 256, 256);

        graphics.text(font, TITLE, xo + (180 - Minecraft.getInstance().font.width(TITLE)) / 2, yo - Minecraft.getInstance().font.lineHeight - 2, 0xFFFFFFFF);

        int burnProgressWidth = Mth.ceil(menu.getDecipheringProgress() * 24.0F);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BURN_PROGRESS_SPRITE, 24, 16, 0, 0,
                xo + 53, yo + 32, burnProgressWidth, 16);

        if (!menu.canCraft()) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ERROR_SPRITE, xo + 52, yo + 30, 28, 21);
        }

        LivingContractInfo info = player.getData(AttachmentRegistry.LIVING_CONTRACT_INFO.get());
        Literacy literacy = info.literacyRank();
        int discoveredTotal = info.getDiscoveredNb();
        float progress = 1;
        Literacy current = Literacy.PROFICIENT;
        Literacy next = Literacy.LITERATE;
        if (literacy.hasNext()) {
            progress = (float)discoveredTotal / (float)literacy.next().discoveredNb;
            current = literacy;
            next = literacy.next();
        }

        graphics.text(font, next.title, xo + 139 - font.width(next.title) / 2, yo + 5, 0xFFFFFFFF);
        graphics.text(font, current.title, xo + 139 - font.width(current.title) / 2, yo + 70, 0xFFFFFFFF);
        graphics.fill(xo + 139, yo + 66, xo + 141, (int) (yo + Mth.lerp(progress, 66, 16)), 0xFF00FF00);

        if (mouseX > xo + 139 - font.width(next.title) / 2 && mouseX < xo + 139 + font.width(next.title) / 2 &&
            mouseY > yo + 5 && mouseY < yo + 5 + font.lineHeight) {
            graphics.setComponentTooltipForNextFrame(font, List.of(next.description), mouseX, mouseY);
        }
        if (mouseX > xo + 139 - font.width(current.title) / 2 && mouseX < xo + 139 + font.width(current.title) / 2 &&
            mouseY > yo + 70 && mouseY < yo + 70 + font.lineHeight) {
            graphics.setComponentTooltipForNextFrame(font, List.of(current.description), mouseX, mouseY);
        }
        if (mouseX > xo + 132 && mouseX < xo + 148 && mouseY > yo + 16 && mouseY < yo + 66) {
            graphics.setComponentTooltipForNextFrame(font, List.of(Component.literal(discoveredTotal + "/" + next.discoveredNb)), mouseX, mouseY);
        }
    }
}
