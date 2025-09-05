package org.exodusstudio.frostbite.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.custom.FrozenRemnantsEntity;
import org.exodusstudio.frostbite.common.entity.custom.LastStandEntity;
import org.exodusstudio.frostbite.common.item.last_stand.LastStand;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static org.exodusstudio.frostbite.common.util.Util.plusOrMinus;

@Mixin(Player.class)
public class PlayerMixin implements LastStand {
    @Unique
    Player frostbite$player = (Player) ((Object) this);

    @Unique
    RandomSource frostbite$random = RandomSource.create();

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
        if (frostbite$isAccumulatingDamage) {
            if (frostbite$maxDurationTicks < frostbite$durationTicks++) {
                frostbite$isAccumulatingDamage = false;
                frostbite$lastStandEntity.setReleasing(true);
                frostbite$player.makeSound(SoundEvents.PLAYER_DEATH);
                frostbite$player.setHealth(0f);
                frostbite$player.die(frostbite$player.damageSources().generic());

                for (int i = 0; i < frostbite$accumulatedDamage / 5; i++) {
                    frostbite$player.level().explode(frostbite$lastStandEntity,
                            frostbite$player.getX() + frostbite$random.nextFloat() * plusOrMinus() * frostbite$accumulatedDamage / 10,
                            frostbite$player.getY() + frostbite$random.nextFloat() * plusOrMinus() * frostbite$accumulatedDamage / 10,
                            frostbite$player.getZ() + frostbite$random.nextFloat() * plusOrMinus() * frostbite$accumulatedDamage / 10,
                            frostbite$accumulatedDamage / 15,
                            true,
                            Level.ExplosionInteraction.MOB);
                }
                frostbite$lastStandEntity.setReleaseTicks((int) (frostbite$accumulatedDamage * 2));
            }

            frostbite$lastStandEntity.move(MoverType.PLAYER, frostbite$player.position());
        }
    }

    @Inject(at = @At("HEAD"), method = "hurtServer", cancellable = true)
    private void hurtServer(ServerLevel serverLevel, DamageSource damageSource, float damage, CallbackInfoReturnable<Boolean> cir) {
        if (frostbite$isAccumulatingDamage && !frostbite$player.isInvulnerableTo(serverLevel, damageSource)
                && !frostbite$player.getAbilities().invulnerable
                && !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            frostbite$accumulatedDamage += damage;
            frostbite$accumulatedDamage = Math.min(frostbite$accumulatedDamage, 100);
            cir.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "dropEquipment", cancellable = true)
    private void dropEquipment(ServerLevel serverLevel, CallbackInfo ci) {
        if (FrozenRemnantsEntity.shouldSpawnFrozenRemnants(serverLevel)) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "addAdditionalSaveData")
    private void addAdditionalSaveData(ValueOutput output, CallbackInfo ci) {
        Frostbite.savedLinings.save(output.list("linings", ItemStackWithSlot.CODEC), frostbite$player.getStringUUID());
    }

    @Inject(at = @At("HEAD"), method = "readAdditionalSaveData")
    private void readAdditionalSaveData(ValueInput input, CallbackInfo ci) {
        Frostbite.savedLinings.load(input.listOrEmpty("linings", ItemStackWithSlot.CODEC), frostbite$player.getStringUUID());
    }

    @Unique
    public void frostbite$startAccumulatingDamage(ServerLevel serverLevel) {
        frostbite$lastStandEntity = new LastStandEntity(EntityRegistry.LAST_STAND.get(), serverLevel);
        frostbite$lastStandEntity.move(MoverType.PLAYER, frostbite$player.position());
        frostbite$lastStandEntity.setReleasing(false);
        serverLevel.addFreshEntityWithPassengers(frostbite$lastStandEntity);
        serverLevel.gameEvent(GameEvent.ENTITY_PLACE, frostbite$player.position(), GameEvent.Context.of(frostbite$player));
        frostbite$isAccumulatingDamage = true;
    }

    @Unique
    public void frostbite$addDamage(float amount) {
        frostbite$accumulatedDamage += amount;
    }
}
