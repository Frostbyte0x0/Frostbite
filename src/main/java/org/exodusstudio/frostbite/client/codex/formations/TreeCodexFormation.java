package org.exodusstudio.frostbite.client.codex.formations;

import org.exodusstudio.frostbite.client.codex.entries.CodexWidget;
import org.exodusstudio.frostbite.client.codex.entries.TargetCodexEntry;

import java.util.ArrayList;
import java.util.HashMap;

public class TreeCodexFormation extends CodexFormation {
    public HashMap<Integer, ArrayList<CodexWidget>> widgetsPerDepth = new HashMap<>();
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

//        widgetsPerDepth.get(1).sort((w1, w2) -> CodexWidget.getNearestSharedParentDepth(w1, w2));
////        widgetsPerDepth.get(1).getFirst().setX(centerX);
////        widgetsPerDepth.get(1).getFirst().setY(centerY);
//        for (int i = 1; i < widgetsPerDepth.get(1).size(); i++) {
//            CodexWidget prevWidget = widgetsPerDepth.get(1).get(i - 1);
//            CodexWidget widget = widgetsPerDepth.get(1).get(i);
//        }
//
//        for (int depth : widgetsPerDepth.keySet()) {
//            if (depth == 1) break;
//            for (CodexWidget widget : widgetsPerDepth.get(depth)) {
//
//            }
//        }
    }
}
