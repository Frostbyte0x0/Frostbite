package org.exodusstudio.frostbite.common.entity.custom.goals;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.common.entity.custom.TanukiEntity;

import java.util.EnumSet;

public class TanukiSitGoal extends Goal {
    private final TanukiEntity tanuki;

    public TanukiSitGoal(TanukiEntity tanuki) {
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.tanuki = tanuki;
    }

    public boolean canUse() {
        return !tanuki.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
    }

    public boolean canContinueToUse() {
        return !tanuki.isInWater() && (tanuki.getRandom().nextInt(reducedTickDelay(600)) != 1) && tanuki.getRandom().nextInt(reducedTickDelay(2000)) != 1;
    }

    public void tick() {
        if (!tanuki.isSitting() && !tanuki.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
            tanuki.tryToSit();
        }
    }

    public void start() {
        tanuki.tryToSit();
    }

    public void stop() {
        ItemStack itemstack = tanuki.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!itemstack.isEmpty()) {
            tanuki.spawnAtLocation(getServerLevel(tanuki.level()), itemstack);
            tanuki.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }

        tanuki.sit(false);
    }
}
