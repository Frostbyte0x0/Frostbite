package org.exodusstudio.frostbite.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import org.exodusstudio.frostbite.common.block.ChilliPepperPlantBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PathfindingContext.class)
public class PathfindingContextMixin {
    @Unique
    PathfindingContext frostbite$context = (PathfindingContext) ((Object) this);

    @Inject(at = @At("TAIL"), method = "getPathTypeFromState", cancellable = true)
    private void getPathTypeFromState(int x, int y, int z, CallbackInfoReturnable<PathType> cir) {
        if (frostbite$context.level().getBlockState(new BlockPos(x, y , z)).getBlock() instanceof ChilliPepperPlantBlock) {
            cir.setReturnValue(PathType.DAMAGING);
        }
    }
}
