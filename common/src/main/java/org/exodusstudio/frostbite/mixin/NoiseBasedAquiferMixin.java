package org.exodusstudio.frostbite.mixin;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Aquifer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Aquifer.NoiseBasedAquifer.class)
public class NoiseBasedAquiferMixin {
    @Inject(method = "computeSubstance", at = @At("RETURN"), cancellable = true)
    private void frostbite$replaceAquiferLava(
            int x,
            int y,
            int z,
            double density,
            CallbackInfoReturnable<BlockState> cir
    ) {
        BlockState state = cir.getReturnValue();
        if (state != null && state.is(Blocks.LAVA)) {
            cir.setReturnValue(Blocks.OBSIDIAN.defaultBlockState());
        }
    }
}
