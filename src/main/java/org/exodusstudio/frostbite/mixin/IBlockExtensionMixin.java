package org.exodusstudio.frostbite.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import org.exodusstudio.frostbite.common.contracts.ContractAttributes;
import org.exodusstudio.frostbite.common.contracts.LivingContractInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IBlockExtension.class)
public interface IBlockExtensionMixin {
    @Inject(at = @At("HEAD"), method = "getFriction", cancellable = true)
    private static void renderArmor(BlockState state, LevelReader level, BlockPos pos, Entity entity, CallbackInfoReturnable<Float> cir) {
        if (entity instanceof LivingEntity livingEntity) {
            if (LivingContractInfo.getContract(livingEntity).hasAttribute(ContractAttributes.SLIDY)) {
                cir.setReturnValue(0.98f);
            }
        }
    }
}
