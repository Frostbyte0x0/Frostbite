package org.exodusstudio.frostbite.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.common.registry.EffectRegistry;
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
    Player frostbite$player = (Player) ((Object) this);

    @Inject(at = @At("HEAD"), method = "tick()V")
    private void tick(CallbackInfo ci) {
//        if (frostbite$random.nextFloat() < frostbite$frequency) {
//            frostbite$livingEntity.setPose(Pose.CROUCHING);
//            frostbite$livingEntity.setShiftKeyDown(true);
//        }
        if (frostbite$player != null && frostbite$player.hasEffect(EffectRegistry.TWITCHING)) {
            if (frostbite$random.nextFloat() < frostbite$frequency) {
                frostbite$player.setSprinting(!frostbite$player.isSprinting());
            }
        }
    }
}
