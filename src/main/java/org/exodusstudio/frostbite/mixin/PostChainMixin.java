package org.exodusstudio.frostbite.mixin;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PostChain.class)
public class PostChainMixin {
    @Unique
    PostChain frostbite$chain = (PostChain) ((Object) this);
    @Unique
    Minecraft frostbite$mc = Minecraft.getInstance();

    @Inject(at = @At("HEAD"), method = "addToFrame", cancellable = true)
    private void addToFrame(FrameGraphBuilder frameGraphBuilder, int width, int height, PostChain.TargetBundle targetBundle, CallbackInfo ci) {
        if (frostbite$setPostChain() && Sets.difference(frostbite$chain.externalTargets, LevelTargetBundle.OUTLINE_TARGETS).isEmpty() && frostbite$shouldShowEntityOutlines()) {
            ci.cancel();
        }
    }

    @Unique
    public boolean frostbite$shouldShowEntityOutlines() {
        if (frostbite$mc.player != null) {
            return (frostbite$mc.player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistry.THERMAL_LENS) ||
                    frostbite$mc.player.getItemInHand(InteractionHand.OFF_HAND).is(ItemRegistry.THERMAL_LENS))
                    && frostbite$mc.player.isUsingItem() && frostbite$mc.options.getCameraType().isFirstPerson();
        } else {
            return false;
        }
    }

    @Unique
    public boolean frostbite$setPostChain() {
        frostbite$chain = (PostChain) ((Object) this);
        return frostbite$chain != null;
    }
}
