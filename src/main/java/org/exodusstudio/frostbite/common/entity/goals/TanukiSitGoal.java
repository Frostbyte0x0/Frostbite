package org.exodusstudio.frostbite.common.entity.goals;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.Goal;
import org.exodusstudio.frostbite.common.entity.custom.animals.TanukiEntity;

import java.util.EnumSet;

public class TanukiSitGoal extends Goal {
    private final TanukiEntity tanuki;

    public TanukiSitGoal(TanukiEntity tanuki) {
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.tanuki = tanuki;
    }

    public boolean canUse() {
        return tanuki.getItemBySlot(EquipmentSlot.MAINHAND).is(ItemTags.FOX_FOOD);
    }

    public boolean canContinueToUse() {
        return !tanuki.isInWater() && (tanuki.getRandom().nextInt(reducedTickDelay(600)) != 1) && tanuki.getRandom().nextInt(reducedTickDelay(2000)) != 1;
    }

    public void tick() {
        if (!tanuki.isSitting() && tanuki.getItemBySlot(EquipmentSlot.MAINHAND).is(ItemTags.FOX_FOOD)) {
            tanuki.tryToSit();
        }
    }

    public void start() {
        tanuki.tryToSit();
    }

    public void stop() {
        tanuki.sit(false);
    }
}
