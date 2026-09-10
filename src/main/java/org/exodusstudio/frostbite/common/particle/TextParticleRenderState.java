package org.exodusstudio.frostbite.common.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

public class TextParticleRenderState implements ParticleGroupRenderState {
    private final List<Entry> entries = new ArrayList<>();

    public void add(PoseStack poseStack, float x, float y, FormattedCharSequence text, int color, int lightCoords) {
        this.entries.add(new Entry(poseStack, x, y, text, color, lightCoords));
    }

    @Override
    public void submit(SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        for (Entry entry : this.entries) {
            submitNodeCollector.submitText(
                    entry.poseStack(),
                    entry.x(),
                    entry.y(),
                    entry.text(),
                    true,
                    Font.DisplayMode.NORMAL,
                    entry.lightCoords(),
                    entry.color(),
                    0,
                    0);
        }
    }

    @Override
    public void clear() {
        this.entries.clear();
    }

    private record Entry(PoseStack poseStack, float x, float y, FormattedCharSequence text, int color, int lightCoords) {}
}
