package org.exodusstudio.frostbite.fabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.common.contracts.ContractAttributes;
import org.exodusstudio.frostbite.common.contracts.LivingContractInfo;
import org.exodusstudio.frostbite.common.event.custom.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow @Final private Map<Holder<MobEffect>, MobEffectInstance> activeEffects;

    @Shadow protected abstract void onEffectsRemoved(Collection<MobEffectInstance> effects);

    @Unique
    private float frostbite$originalDamage;

    @Unique
    private LivingEntity frostbite$self() {
        return (LivingEntity) (Object) this;
    }

    @Inject(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isSleeping()Z"), cancellable = true)
    private void frostbite$incomingDamage(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        LivingIncomingDamageEvent event = new LivingIncomingDamageEvent(frostbite$self(), source, damage);
        LivingIncomingDamageEvent.EVENT.invoker().accept(event);
        if (event.isCanceled()) cir.setReturnValue(false);
    }

    @Inject(method = "actuallyHurt", at = @At("HEAD"))
    private void frostbite$captureOriginalDamage(ServerLevel level, DamageSource source, float damage, CallbackInfo ci) {
        frostbite$originalDamage = damage;
    }

    @ModifyVariable(method = "actuallyHurt", at = @At(value = "LOAD", ordinal = 6), argsOnly = true)
    private float frostbite$damagePre(float damage, @Local(argsOnly = true) DamageSource source) {
        LivingDamageEvent.Pre event = new LivingDamageEvent.Pre(frostbite$self(), source, frostbite$originalDamage, damage);
        LivingDamageEvent.Pre.EVENT.invoker().accept(event);
        return event.getNewDamage();
    }

    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;gameEvent(Lnet/minecraft/core/Holder;)V"))
    private void frostbite$damagePost(ServerLevel level, DamageSource source, float damage, CallbackInfo ci) {
        LivingDamageEvent.Post.EVENT.invoker().accept(new LivingDamageEvent.Post(frostbite$self(), source, frostbite$originalDamage, damage));
    }

    @ModifyVariable(method = "knockback(DDDLnet/minecraft/world/damagesource/DamageSource;FZ)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private double frostbite$knockBack(double power) {
        LivingKnockBackEvent event = new LivingKnockBackEvent(frostbite$self(), (float) power);
        LivingKnockBackEvent.EVENT.invoker().accept(event);
        return event.getStrength();
    }

    @Inject(method = "startUsingItem", at = @At("HEAD"), cancellable = true)
    private void frostbite$useItemStart(InteractionHand hand, CallbackInfo ci) {
        LivingEntity self = frostbite$self();
        ItemStack stack = self.getItemInHand(hand);
        if (stack.isEmpty() || self.isUsingItem()) return;

        LivingEntityUseItemEvent.Start event = new LivingEntityUseItemEvent.Start(self, stack, stack.getUseDuration(self));
        LivingEntityUseItemEvent.Start.EVENT.invoker().accept(event);
        if (event.isCanceled()) ci.cancel();
    }

    @Inject(method = "updateUsingItem", at = @At("HEAD"))
    private void frostbite$useItemTick(ItemStack useItem, CallbackInfo ci) {
        LivingEntity self = frostbite$self();
        LivingEntityUseItemEvent.Tick.EVENT.invoker().accept(
                new LivingEntityUseItemEvent.Tick(self, useItem, self.getUseItemRemainingTicks()));
    }

    @Inject(method = "removeEffect", at = @At("HEAD"), cancellable = true)
    private void frostbite$removeEffect(Holder<MobEffect> effect, CallbackInfoReturnable<Boolean> cir) {
        if (frostbite$isRemovalCanceled(effect, this.activeEffects.get(effect))) cir.setReturnValue(false);
    }

    @Inject(method = "removeAllEffects", at = @At("HEAD"), cancellable = true)
    private void frostbite$removeAllEffects(CallbackInfoReturnable<Boolean> cir) {
        if (frostbite$self().level().isClientSide() || this.activeEffects.isEmpty()) return;

        List<MobEffectInstance> kept = new ArrayList<>();
        for (MobEffectInstance instance : List.copyOf(this.activeEffects.values())) {
            if (frostbite$isRemovalCanceled(instance.getEffect(), instance)) kept.add(instance);
        }
        if (kept.isEmpty()) return;

        List<MobEffectInstance> removed = new ArrayList<>();
        for (MobEffectInstance instance : List.copyOf(this.activeEffects.values())) {
            if (kept.contains(instance)) continue;
            this.activeEffects.remove(instance.getEffect());
            removed.add(instance);
        }
        if (!removed.isEmpty()) this.onEffectsRemoved(removed);
        cir.setReturnValue(!removed.isEmpty());
    }

    @Unique
    private boolean frostbite$isRemovalCanceled(Holder<MobEffect> effect, MobEffectInstance instance) {
        MobEffectEvent.Remove event = new MobEffectEvent.Remove(frostbite$self(), effect, instance);
        MobEffectEvent.Remove.EVENT.invoker().accept(event);
        return event.isCanceled();
    }

    @ModifyExpressionValue(method = "checkTotemDeathProtection", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack frostbite$useTotem(ItemStack totem, @Local(argsOnly = true) DamageSource source) {
        LivingUseTotemEvent.EVENT.invoker().accept(new LivingUseTotemEvent(frostbite$self(), source, totem));
        return totem;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void frostbite$tick(CallbackInfo ci) {
        LivingTickEvent.EVENT.invoker().accept(new LivingTickEvent(frostbite$self()));
    }

    @ModifyExpressionValue(method = "travelInAir", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getFriction()F"))
    private float frostbite$slidy(float friction) {
        if (LivingContractInfo.getContract(frostbite$self()).hasAttribute(ContractAttributes.SLIDY)) {
            return 0.98f;
        }
        return friction;
    }
}
