package org.exodusstudio.frostbite.client.codex.formations;

import org.exodusstudio.frostbite.client.codex.entries.CodexWidget;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class TreeCodexFormation extends CodexFormation {
    public LinkedHashMap<Integer, ArrayList<CodexWidget>> widgetsPerDepth = new LinkedHashMap<>();
    public int height = 0;

    public TreeCodexFormation(int centerX, int centerY) {
        super(centerX, centerY);
    }

    @Override
    public void addWidget(CodexWidget widget) {
        if (widget.getDepth() > height) height = widget.getDepth();
        widgetsPerDepth.computeIfAbsent(widget.getDepth(), k -> new ArrayList<>()).add(widget);
        super.addWidget(widget);
    }

    @Override
    public void computePlacements() {
        if (widgetsPerDepth.isEmpty()) return;

        int levelWidth = 30;
        int levelHeight = 30;

        LinkedHashMap<CodexWidget, List<CodexWidget>> widgetsPerParent = new LinkedHashMap<>();
        for (CodexWidget w : widgetsPerDepth.get(height)) {
            widgetsPerParent.computeIfAbsent(w.parent, _ -> new ArrayList<>()).add(w);
        }

        int currentX = 0;
        for (int i = 0; i < widgetsPerParent.size(); i++) {
            CodexWidget parent = widgetsPerParent.sequencedKeySet().toArray(new CodexWidget[0])[i];

            for (CodexWidget child : widgetsPerParent.get(parent)) {
                int x = centerX + currentX;
                int y = centerY + levelHeight * height;
                widgetPlacements.put(child, new int[]{x, y});
                currentX += levelWidth;
            }

            if (i == widgetsPerParent.size() - 1) continue;
            currentX += levelWidth * (CodexWidget.getNearestSharedParentDepth(
                    widgetsPerParent.sequencedKeySet().toArray(new CodexWidget[0])[i],
                    widgetsPerParent.sequencedKeySet().toArray(new CodexWidget[0])[i+1]));
        }

        for (int i = height - 1; i >= 0; i--) {
            int depth = widgetsPerDepth.sequencedKeySet().toArray(new Integer[0])[i];

            for (CodexWidget widget : widgetsPerDepth.get(depth)) {
                int maxX = 0;
                int minX = (int) 10E9;

                for (CodexWidget child : widgetPlacements.keySet()) {
                    if (child.parent == widget) {
                        int[] placement = widgetPlacements.get(child);
                        maxX = Math.max(maxX, placement[0]);
                        minX = Math.min(minX, placement[0]);
                    }
                }

                int x = (maxX + minX) / 2;
                int y = centerY + levelHeight * depth;
                widgetPlacements.put(widget, new int[]{x, y});
            }
        }
    }
}
