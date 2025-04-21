package org.exodusstudio.frostbite.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class LivingEntityMixin {
    @Unique
    RandomSource frostbite$random = RandomSource.create();

    @Unique
    float frostbite$frequency = (float) 1 / 300;

    @Unique
    Player frostbite$livingEntity = (Player) ((Object) this);

    @Inject(at = @At("HEAD"), method = "tick()V")
    private void tick(CallbackInfo ci) {
//        if (frostbite$random.nextFloat() < frostbite$frequency) {
//            frostbite$livingEntity.setPose(Pose.CROUCHING);
//            frostbite$livingEntity.setShiftKeyDown(true);
//        }
        if (frostbite$random.nextFloat() < frostbite$frequency) {
            frostbite$livingEntity.setSprinting(!frostbite$livingEntity.isSprinting());
        }
    }
}
