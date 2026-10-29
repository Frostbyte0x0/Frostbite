package org.exodusstudio.frostbite.neoforge.common.events;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.event.custom.AdvancementProgressEvent;
import org.exodusstudio.frostbite.common.event.custom.LivingTickEvent;

@EventBusSubscriber(modid = Frostbite.MOD_ID)
public class FrostbiteNeoForgeEvents {
    @SubscribeEvent
    public static void livingDamagePre(LivingDamageEvent.Pre event) {
        var frostbiteEvent = new org.exodusstudio.frostbite.common.event.custom.LivingDamageEvent.Pre(
                event.getEntity(), event.getSource(), event.getOriginalDamage(), event.getNewDamage());
        org.exodusstudio.frostbite.common.event.custom.LivingDamageEvent.Pre.EVENT.invoker().accept(frostbiteEvent);
        event.setNewDamage(frostbiteEvent.getNewDamage());
    }

    @SubscribeEvent
    public static void livingDamagePost(LivingDamageEvent.Post event) {
        org.exodusstudio.frostbite.common.event.custom.LivingDamageEvent.Post.EVENT.invoker().accept(
                new org.exodusstudio.frostbite.common.event.custom.LivingDamageEvent.Post(
                        event.getEntity(), event.getSource(), event.getOriginalDamage(), event.getInflictedDamage()));
    }

    @SubscribeEvent
    public static void livingIncomingDamage(LivingIncomingDamageEvent event) {
        var frostbiteEvent = new org.exodusstudio.frostbite.common.event.custom.LivingIncomingDamageEvent(
                event.getEntity(), event.getSource(), event.getAmount());
        org.exodusstudio.frostbite.common.event.custom.LivingIncomingDamageEvent.EVENT.invoker().accept(frostbiteEvent);
        if (frostbiteEvent.isCanceled()) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void livingKnockBack(LivingKnockBackEvent event) {
        var frostbiteEvent = new org.exodusstudio.frostbite.common.event.custom.LivingKnockBackEvent(
                event.getEntity(), event.getStrength());
        org.exodusstudio.frostbite.common.event.custom.LivingKnockBackEvent.EVENT.invoker().accept(frostbiteEvent);
        event.setStrength(frostbiteEvent.getStrength());
    }

    @SubscribeEvent
    public static void livingChangeTarget(LivingChangeTargetEvent event) {
        var frostbiteEvent = new org.exodusstudio.frostbite.common.event.custom.LivingChangeTargetEvent(
                event.getEntity(), event.getNewAboutToBeSetTarget());
        org.exodusstudio.frostbite.common.event.custom.LivingChangeTargetEvent.EVENT.invoker().accept(frostbiteEvent);
        if (frostbiteEvent.isCanceled()) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void useItemStart(LivingEntityUseItemEvent.Start event) {
        var frostbiteEvent = new org.exodusstudio.frostbite.common.event.custom.LivingEntityUseItemEvent.Start(
                event.getEntity(), event.getItem(), event.getDuration());
        org.exodusstudio.frostbite.common.event.custom.LivingEntityUseItemEvent.Start.EVENT.invoker().accept(frostbiteEvent);
        if (frostbiteEvent.isCanceled()) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void useItemTick(LivingEntityUseItemEvent.Tick event) {
        org.exodusstudio.frostbite.common.event.custom.LivingEntityUseItemEvent.Tick.EVENT.invoker().accept(
                new org.exodusstudio.frostbite.common.event.custom.LivingEntityUseItemEvent.Tick(
                        event.getEntity(), event.getItem(), event.getDuration()));
    }

    @SubscribeEvent
    public static void effectRemove(MobEffectEvent.Remove event) {
        var frostbiteEvent = new org.exodusstudio.frostbite.common.event.custom.MobEffectEvent.Remove(
                event.getEntity(), event.getEffect(), event.getEffectInstance());
        org.exodusstudio.frostbite.common.event.custom.MobEffectEvent.Remove.EVENT.invoker().accept(frostbiteEvent);
        if (frostbiteEvent.isCanceled()) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void useTotem(LivingUseTotemEvent event) {
        org.exodusstudio.frostbite.common.event.custom.LivingUseTotemEvent.EVENT.invoker().accept(
                new org.exodusstudio.frostbite.common.event.custom.LivingUseTotemEvent(
                        event.getEntity(), event.getSource(), event.getTotem()));
    }

    @SubscribeEvent
    public static void finalizeSpawn(FinalizeSpawnEvent event) {
        org.exodusstudio.frostbite.common.event.custom.FinalizeSpawnEvent.EVENT.invoker().accept(
                new org.exodusstudio.frostbite.common.event.custom.FinalizeSpawnEvent(event.getEntity()));
    }

    @SubscribeEvent
    public static void criticalHit(CriticalHitEvent event) {
        var frostbiteEvent = new org.exodusstudio.frostbite.common.event.custom.CriticalHitEvent(
                event.getEntity(), event.getTarget(), event.getDamageMultiplier());
        org.exodusstudio.frostbite.common.event.custom.CriticalHitEvent.EVENT.invoker().accept(frostbiteEvent);
        event.setDamageMultiplier(frostbiteEvent.getDamageMultiplier());
    }

    @SubscribeEvent
    public static void advancementProgress(AdvancementEvent.AdvancementProgressEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            AdvancementProgressEvent.EVENT.invoker().accept(
                    new AdvancementProgressEvent(player, event.getAdvancement(), event.getCriterionName()));
        }
    }

    @SubscribeEvent
    public static void entityTick(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity living) {
            LivingTickEvent.EVENT.invoker().accept(new LivingTickEvent(living));
        }
    }
}
