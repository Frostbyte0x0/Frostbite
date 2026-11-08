package org.exodusstudio.frostbite.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ProtoChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProtoChunk.class)
public class ProtoChunkMixin {
    @Inject(method = "setBlockState", at = @At("HEAD"), cancellable = true)
    private void frostbite$replaceGeneratedLava(
            BlockPos pos,
            BlockState state,
            int flags,
            CallbackInfoReturnable<BlockState> cir
    ) {
        if (state.is(Blocks.LAVA)) {
            ProtoChunk chunk = (ProtoChunk) (Object) this;
            cir.setReturnValue(chunk.setBlockState(pos, Blocks.OBSIDIAN.defaultBlockState(), flags));
        }
    }
}
