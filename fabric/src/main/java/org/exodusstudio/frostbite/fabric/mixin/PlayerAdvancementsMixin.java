package org.exodusstudio.frostbite.fabric.mixin;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.exodusstudio.frostbite.common.event.custom.AdvancementProgressEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementsMixin {
    @Shadow private ServerPlayer player;

    @Inject(method = "award", at = @At("RETURN"))
    private void frostbite$advancementProgress(AdvancementHolder holder, String criterion, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) {
            AdvancementProgressEvent.EVENT.invoker().accept(new AdvancementProgressEvent(this.player, holder, criterion));
        }
    }
}
