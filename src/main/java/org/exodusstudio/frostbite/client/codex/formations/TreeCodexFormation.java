package org.exodusstudio.frostbite.client.codex.formations;

import org.exodusstudio.frostbite.client.codex.entries.CodexWidget;
import org.exodusstudio.frostbite.client.codex.entries.TargetCodexEntry;

public class TreeCodexFormation extends CodexFormation {
    public TreeCodexFormation(int centerX, int centerY) {
        super(centerX, centerY);
    }

    @Override
    protected void computePlacements() {
        if (widgetPlacements.isEmpty()) return;

        int levelWidth = 30;
        int levelHeight = 30;

        int index = -widgetPlacements.size() / 2;
        for (CodexWidget widget : widgetPlacements.keySet()) {
            int xOffset = index * levelWidth;
            int level = 0;
            TargetCodexEntry parent = widget.codexEntry.parent;
            while (parent != null) {
                level++;
                parent = parent.parent;
            }
            int yOffset = level * levelHeight;

            int x = centerX + xOffset;
            int y = centerY + yOffset;

            widgetPlacements.put(widget, new int[]{x, y});
            index++;
        }
    }
}
