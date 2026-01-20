package org.exodusstudio.frostbite.client.codex.formations;

import org.exodusstudio.frostbite.client.codex.entries.CodexWidget;

import java.util.HashMap;
import java.util.Map;

public abstract class CodexFormation {
    protected final Map<CodexWidget, int[]> widgetPlacements = new HashMap<>();
    protected int centerX;
    protected int centerY;

    public CodexFormation(int centerX, int centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public void addWidget(CodexWidget widget) {
        widget.codexFormation = this;
        widgetPlacements.put(widget, null);
        computePlacements();
    }

    protected void computePlacements() {

    }

    public int[] getPlacement(CodexWidget widget) {
        return widgetPlacements.get(widget);
    }

    public int getXPlacement(CodexWidget widget) {
        return getPlacement(widget)[0];
    }

    public int getYPlacement(CodexWidget widget) {
        return getPlacement(widget)[1];
    }
}
