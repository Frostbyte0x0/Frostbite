package org.exodusstudio.frostbite.client.codex.formations;

import org.exodusstudio.frostbite.client.codex.entries.CodexWidget;

import java.util.LinkedHashMap;

public abstract class CodexFormation {
    protected final LinkedHashMap<CodexWidget, int[]> widgetPlacements = new LinkedHashMap<>();
    protected final int centerX;
    protected final int centerY;

    public CodexFormation(int centerX, int centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public void addWidget(CodexWidget widget) {
        widget.codexFormation = this;
        widgetPlacements.put(widget, null);
    }

    public abstract void computePlacements();

    public int[] getPlacement(CodexWidget widget) {
        return widgetPlacements.get(widget);
    }

    public int getXPlacement(CodexWidget widget) {
        return getPlacement(widget)[0];
    }

    public int getYPlacement(CodexWidget widget) {
        return getPlacement(widget)[1];
    }

    public LinkedHashMap<CodexWidget, int[]> getWidgetPlacements() {
        return widgetPlacements;
    }
}
