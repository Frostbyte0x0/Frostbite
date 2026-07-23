package org.exodusstudio.frostbite.client.codex;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.client.codex.entries.CodexEntry;
import org.exodusstudio.frostbite.common.registry.KeyMappingRegistry;

public class CodexEntryToast implements Toast {
    private static final WidgetSprites BACKGROUND_SPRITE =
            new WidgetSprites(Identifier.withDefaultNamespace("friends/toast_background"));

    private final CodexEntry entry;
    private Toast.Visibility visibility = Toast.Visibility.SHOW;


    public CodexEntryToast(CodexEntry entry) {
        this.entry = entry;
    }

    @Override
    public Visibility getWantedVisibility() {
        return visibility;
    }

    @Override
    public void update(ToastManager manager, long l) {
        if (l >= 10000 * manager.getNotificationDisplayTimeMultiplier()) {
            this.visibility = Toast.Visibility.HIDE;
        }
    }

    @Override
    public int width() {
        return Toast.super.width() + 50;
    }

    @Override
    public int height() {
        return Toast.super.height() + 11;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, Font font, long l) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE.get(true, false), 0, 0, this.width(), this.height());
        int textLeft = 42;

        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, entry.image, 6, 6, 32, 32);

        int textTop = 7;

        graphics.text(font, Component.translatable("codex.new_entry").withStyle(ChatFormatting.GREEN), textLeft, textTop, -1, false);
        graphics.text(font, entry.title, textLeft, textTop + 11, -1, false);
        graphics.text(font, Component.translatable("codex.tooltip", KeyMappingRegistry.CODEX.getKey().getDisplayName()).withStyle(ChatFormatting.DARK_GREEN), textLeft, textTop + 22, -1, false);
    }
}
