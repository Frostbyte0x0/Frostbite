package org.exodusstudio.frostbite.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.exodusstudio.frostbite.common.entity.custom.LastStandEntity;
import org.exodusstudio.frostbite.common.item.custom.last_stand.LastStand;
import org.exodusstudio.frostbite.common.registry.EffectRegistry;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin implements LastStand {
    @Unique
    Player frostbite$player = (Player) ((Object) this);

    @Unique
    RandomSource frostbite$random = RandomSource.create();

    @Unique
    float frostbite$frequency = (float) 1 / 300;


    @Unique
    boolean frostbite$isAccumulatingDamage = false;

    @Unique
    float frostbite$accumulatedDamage = 0f;

    @Unique
    int frostbite$maxDurationTicks = 200;

    @Unique
    int frostbite$durationTicks = 0;

    @Unique
    LastStandEntity frostbite$lastStandEntity;

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

        if (frostbite$isAccumulatingDamage) {
            if (frostbite$maxDurationTicks < frostbite$durationTicks++) {
                frostbite$isAccumulatingDamage = false;
                frostbite$lastStandEntity.setReleasing(true);
                frostbite$player.makeSound(SoundEvents.PLAYER_DEATH);
                frostbite$player.setHealth(0f);
                frostbite$player.die(frostbite$player.damageSources().generic());
                frostbite$player.level().explode(frostbite$lastStandEntity,
                        frostbite$player.getX(),
                        frostbite$player.getY(),
                        frostbite$player.getZ(),
                        frostbite$accumulatedDamage / 2,
                        Level.ExplosionInteraction.MOB);
            }
            frostbite$lastStandEntity.moveTo(frostbite$player.position());
            frostbite$lastStandEntity.setDamageAccumulated(frostbite$accumulatedDamage);
        }
    }

    @Inject(at = @At("HEAD"), method = "hurtServer", cancellable = true)
    private void hurtServer(ServerLevel serverLevel, DamageSource damageSource, float damage, CallbackInfoReturnable<Boolean> cir) {
        if (frostbite$isAccumulatingDamage && !frostbite$player.isInvulnerableTo(serverLevel, damageSource)
                && !frostbite$player.getAbilities().invulnerable
                && !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            frostbite$accumulatedDamage += damage;
            cir.cancel();
        }
    }

    @Unique
    public void frostbite$startAccumulatingDamage(ServerLevel serverLevel) {
        frostbite$lastStandEntity = new LastStandEntity(EntityRegistry.LAST_STAND.get(), serverLevel);
        frostbite$lastStandEntity.moveTo(frostbite$player.position());
        frostbite$lastStandEntity.setReleasing(false);
        serverLevel.addFreshEntityWithPassengers(frostbite$lastStandEntity);
        serverLevel.gameEvent(GameEvent.ENTITY_PLACE, frostbite$player.position(), GameEvent.Context.of(frostbite$player));
        frostbite$isAccumulatingDamage = true;
    }
}
