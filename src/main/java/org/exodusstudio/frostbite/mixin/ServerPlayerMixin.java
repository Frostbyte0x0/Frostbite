package org.exodusstudio.frostbite.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.GameRules;
import org.exodusstudio.frostbite.common.util.InventoryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Unique
    ServerPlayer frostbite$serverPlayer = (ServerPlayer) ((Object) this);

    @Inject(at = @At("RETURN"), method = "restoreFrom")
    private void restoreFrom(ServerPlayer that, boolean keepEverything, CallbackInfo ci) {
        if (!keepEverything && (frostbite$serverPlayer.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) || that.isSpectator())) {
            frostbite$serverPlayer.getInventory().replaceWith(that.getInventory());
            frostbite$serverPlayer.getInventory().setItem(43, ((InventoryWrapper) that.getInventory()).frostbite$getEquipment().get(EquipmentSlot.FEET));
            frostbite$serverPlayer.getInventory().setItem(44, ((InventoryWrapper) that.getInventory()).frostbite$getEquipment().get(EquipmentSlot.LEGS));
            frostbite$serverPlayer.getInventory().setItem(45, ((InventoryWrapper) that.getInventory()).frostbite$getEquipment().get(EquipmentSlot.CHEST));
            frostbite$serverPlayer.getInventory().setItem(46, ((InventoryWrapper) that.getInventory()).frostbite$getEquipment().get(EquipmentSlot.HEAD));
        }
    }
}
