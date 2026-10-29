package org.exodusstudio.frostbite.fabric.mixin;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import org.exodusstudio.frostbite.common.event.custom.FinalizeSpawnEvent;
import org.exodusstudio.frostbite.common.event.custom.LivingChangeTargetEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin {
    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void frostbite$changeTarget(@Nullable LivingEntity target, CallbackInfo ci) {
        LivingChangeTargetEvent event = new LivingChangeTargetEvent((Mob) (Object) this, target);
        LivingChangeTargetEvent.EVENT.invoker().accept(event);
        if (event.isCanceled()) ci.cancel();
    }

    @Inject(method = "finalizeSpawn", at = @At("HEAD"))
    private void frostbite$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason,
                                         @Nullable SpawnGroupData groupData, CallbackInfoReturnable<SpawnGroupData> cir) {
        FinalizeSpawnEvent.EVENT.invoker().accept(new FinalizeSpawnEvent((Mob) (Object) this));
    }
}
