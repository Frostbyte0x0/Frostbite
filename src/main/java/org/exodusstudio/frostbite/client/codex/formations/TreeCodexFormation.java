package org.exodusstudio.frostbite.client.codex.formations;

import org.exodusstudio.frostbite.client.codex.entries.CodexWidget;

public class TreeCodexFormation extends CodexFormation {
    public TreeCodexFormation(int centerX, int centerY) {
        super(centerX, centerY);
    }

    @Override
    protected void computePlacements() {
        int widgetCount = widgetPlacements.size();
        if (widgetCount == 0) return;

        int levelHeight = 25;
        int maxPerLevel = 2;
        int currentLevel = 0;
        int currentIndexInLevel = 0;

        for (CodexWidget widget : widgetPlacements.keySet()) {
            int xOffset = (currentIndexInLevel - (maxPerLevel - 1) / 2) * 100;
            int yOffset = currentLevel * levelHeight;

            int x = centerX + xOffset;
            int y = centerY + yOffset;

            widgetPlacements.put(widget, new int[]{x, y});

            currentIndexInLevel++;
            if (currentIndexInLevel >= maxPerLevel) {
                currentIndexInLevel = 0;
                currentLevel++;
                maxPerLevel *= 2;
            }
        }
    }
}
