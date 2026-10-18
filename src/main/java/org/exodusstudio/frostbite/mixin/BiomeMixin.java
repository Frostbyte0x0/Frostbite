package org.exodusstudio.frostbite.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public class BiomeMixin {
    private static final ResourceKey<Biome> CHARM_GROVE = ResourceKey.create(
            Registries.BIOME,
            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "charm_grove")
    );

    @Inject(at = @At("HEAD"), method = "shouldSnow", cancellable = true)
    private void removed(LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (level instanceof Level actualLevel && Util.isFrostbite(actualLevel)) {
            // Keep the custom Frostbite snow feature available for the cold biomes,
            // while keeping Charm Grove permanently clear of ground snow.
            cir.setReturnValue(!level.getBiome(pos).is(CHARM_GROVE));
        }
    }
}
