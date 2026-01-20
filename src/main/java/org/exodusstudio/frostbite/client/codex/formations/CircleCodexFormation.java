package org.exodusstudio.frostbite.client.codex.formations;

import org.exodusstudio.frostbite.client.codex.entries.CodexWidget;

public class CircleCodexFormation extends CodexFormation {
    private int radius;

    public CircleCodexFormation(int centerX, int centerY, int radius) {
        super(centerX, centerY);
    }

    @Override
    protected void computePlacements() {
        int widgetCount = widgetPlacements.size();
        if (widgetCount == 0) return;

        double angleIncrement = 2 * Math.PI / widgetCount;

        int index = 0;
        for (CodexWidget widget : widgetPlacements.keySet()) {
            double angle = index * angleIncrement;
            int x = centerX + (int) (radius * Math.cos(angle));
            int y = centerY + (int) (radius * Math.sin(angle));
            widgetPlacements.put(widget, new int[]{x, y});
            index++;
        }
    }
}
