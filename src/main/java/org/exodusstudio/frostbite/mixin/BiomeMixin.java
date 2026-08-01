package org.exodusstudio.frostbite.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public class BiomeMixin {
    @Inject(at = @At("HEAD"), method = "shouldSnow", cancellable = true)
    private void removed(LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (level.dimensionType().toString().contains("frostbite"))
            cir.setReturnValue(true);
    }
}
