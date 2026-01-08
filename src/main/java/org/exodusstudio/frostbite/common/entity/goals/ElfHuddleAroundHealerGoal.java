package org.exodusstudio.frostbite.common.entity.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;
import org.exodusstudio.frostbite.common.entity.custom.elves.ElfEntity;

import java.util.EnumSet;

public class ElfHuddleAroundHealerGoal extends Goal {
    final ElfEntity elf;
    private final double speedModifier;
    private Path path;
    private long lastCanUseCheck;

    public ElfHuddleAroundHealerGoal(ElfEntity elf, float speedModifier) {
        this.elf = elf;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        long i = elf.level().getGameTime();
        if (i - this.lastCanUseCheck < 20) {
            return false;
        }
        if (elf.getHealerPosition().isPresent()) {
            this.lastCanUseCheck = i;
            BlockPos healerPos = elf.getHealerPosition().get();
            this.path = elf.getNavigation().createPath(healerPos, 0);
            return this.path != null;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return elf.getHealerPosition().isPresent() && elf.distanceToSqr(
                elf.getHealerPosition().get().getX(),
                elf.getHealerPosition().get().getY(),
                elf.getHealerPosition().get().getZ()) > 2;
    }

    public void start() {
        elf.getNavigation().moveTo(this.path, this.speedModifier);
    }

    public void stop() {
        elf.setHealerPosition(null);

        elf.setAggressive(false);
        elf.getNavigation().stop();
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        if (elf.getHealerPosition().isPresent()) {
           this.path = elf.getNavigation().createPath(elf.getHealerPosition().get(), 10);
           elf.getNavigation().moveTo(this.path, this.speedModifier);
        }
    }
}
