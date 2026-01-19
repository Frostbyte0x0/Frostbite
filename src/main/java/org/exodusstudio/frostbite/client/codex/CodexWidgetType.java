package org.exodusstudio.frostbite.client.codex;

import net.minecraft.advancements.AdvancementType;
import net.minecraft.resources.Identifier;

public enum CodexWidgetType {
    OBTAINED(
            Identifier.withDefaultNamespace("advancements/box_obtained"),
            Identifier.withDefaultNamespace("advancements/task_frame_obtained"),
            Identifier.withDefaultNamespace("advancements/challenge_frame_obtained"),
            Identifier.withDefaultNamespace("advancements/goal_frame_obtained")),
    UNOBTAINED(
            Identifier.withDefaultNamespace("advancements/box_unobtained"),
            Identifier.withDefaultNamespace("advancements/task_frame_unobtained"),
            Identifier.withDefaultNamespace("advancements/challenge_frame_unobtained"),
            Identifier.withDefaultNamespace("advancements/goal_frame_unobtained"));

    private final Identifier boxSprite;
    private final Identifier taskFrameSprite;
    private final Identifier challengeFrameSprite;
    private final Identifier goalFrameSprite;

    CodexWidgetType(Identifier p_468364_, Identifier p_467964_, Identifier p_467929_, Identifier p_469255_) {
        this.boxSprite = p_468364_;
        this.taskFrameSprite = p_467964_;
        this.challengeFrameSprite = p_467929_;
        this.goalFrameSprite = p_469255_;
    }

    public Identifier boxSprite() {
        return this.boxSprite;
    }

    public Identifier frameSprite(AdvancementType p_312712_) {
        Identifier var10000;
        switch (p_312712_) {
            case TASK -> var10000 = this.taskFrameSprite;
            case CHALLENGE -> var10000 = this.challengeFrameSprite;
            case GOAL -> var10000 = this.goalFrameSprite;
            default -> throw new MatchException(null, null);
        }

        return var10000;
    }
}
