package org.exodusstudio.frostbite.mixin;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.component.ChargeData;
import org.exodusstudio.frostbite.common.entity.custom.IceBlockEntity;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
    @Shadow public BlockPos destroyPos;
    @Unique
    ServerPlayerGameMode frostbite$this = (ServerPlayerGameMode) ((Object) this);
    @Unique
    private static final Logger frostbite$LOGGER = LogUtils.getLogger();


    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    private void tick(CallbackInfo ci) {
        frostbite$this.gameTicks++;

        if (frostbite$this.hasDelayedDestroy) {
            Frostbite.LOGGER.debug("delayed");
            BlockState blockstate = frostbite$this.level.getBlockState(frostbite$this.delayedDestroyPos);
            if (blockstate.isAir()) {
                frostbite$this.hasDelayedDestroy = false;
            } else {
                float f = frostbite$this.incrementDestroyProgress(blockstate, frostbite$this.delayedDestroyPos, frostbite$this.delayedTickStart);
                if (f >= 1.0F) {
                    frostbite$this.hasDelayedDestroy = false;
                    frostbite$this.destroyBlock(frostbite$this.delayedDestroyPos);
                }
            }
        } else if (frostbite$this.isDestroyingBlock) {
            BlockState blockstate1 = frostbite$this.level.getBlockState(frostbite$this.destroyPos);
            if (frostbite$this.player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistry.ICE_HAMMER)) {
                ItemStack itemStack = frostbite$this.player.getItemInHand(InteractionHand.MAIN_HAND);
                if (itemStack.get(DataComponentTypeRegistry.CHARGE).charge() <= 0) {
                    IceBlockEntity iceBlock = new IceBlockEntity(EntityRegistry.ICE_BLOCK.get(), frostbite$this.level);
                    iceBlock.moveTo(Vec3.atCenterOf(destroyPos));
                    iceBlock.disableDrop();
                    iceBlock.addDeltaMovement(new Vec3(0, 0.5, 0));
                    frostbite$this.level.addFreshEntity(iceBlock);

                    itemStack.set(DataComponentTypeRegistry.CHARGE, new ChargeData(60));
                }
            } else if (frostbite$this.player.getItemInHand(InteractionHand.OFF_HAND).is(ItemRegistry.ICE_HAMMER)) {
                ItemStack itemStack = frostbite$this.player.getItemInHand(InteractionHand.OFF_HAND);
                if (itemStack.get(DataComponentTypeRegistry.CHARGE).charge() <= 0) {
                    IceBlockEntity iceBlock = new IceBlockEntity(EntityRegistry.ICE_BLOCK.get(), frostbite$this.level);
                    iceBlock.disableDrop();
                    iceBlock.moveTo(Vec3.atCenterOf(destroyPos));
                    iceBlock.addDeltaMovement(new Vec3(0, 0.5, 0));
                    frostbite$this.level.addFreshEntity(iceBlock);

                    itemStack.set(DataComponentTypeRegistry.CHARGE, new ChargeData(60));
                }
            }

            if (blockstate1.isAir()) {
                frostbite$this.level.destroyBlockProgress(frostbite$this.player.getId(), frostbite$this.destroyPos, -1);
                frostbite$this.lastSentState = -1;
                frostbite$this.isDestroyingBlock = false;
            } else {
                frostbite$this.incrementDestroyProgress(blockstate1, frostbite$this.destroyPos, frostbite$this.destroyProgressStart);
            }
        }
        ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "handleBlockBreakAction", cancellable = true)
    public void handleBlockBreakAction(BlockPos pos, ServerboundPlayerActionPacket.Action action, Direction face, int maxBuildHeight, int sequence, CallbackInfo ci) {
        PlayerInteractEvent.LeftClickBlock event = CommonHooks.onLeftClickBlock(frostbite$this.player, pos, face, action);
        if (!event.isCanceled()) {
            if (!frostbite$this.player.canInteractWithBlock(pos, 1.0F)) {
                frostbite$this.debugLogging(pos, false, sequence, "too far");
            } else if (pos.getY() > maxBuildHeight) {
                frostbite$this.player.connection.send(new ClientboundBlockUpdatePacket(pos, frostbite$this.level.getBlockState(pos)));
                frostbite$this.debugLogging(pos, false, sequence, "too high");
            } else if (action == net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK) {
                if (!frostbite$this.level.mayInteract(frostbite$this.player, pos)) {
                    frostbite$this.player.connection.send(new ClientboundBlockUpdatePacket(pos, frostbite$this.level.getBlockState(pos)));
                    frostbite$this.debugLogging(pos, false, sequence, "may not interact");
                    return;
                }

                if (frostbite$this.isCreative()) {
                    if (frostbite$this.player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistry.ICE_HAMMER)) {
                        ItemStack itemStack = frostbite$this.player.getItemInHand(InteractionHand.MAIN_HAND);
                        if (itemStack.get(DataComponentTypeRegistry.CHARGE).charge() <= 0) {
                            IceBlockEntity iceBlock = new IceBlockEntity(EntityRegistry.ICE_BLOCK.get(), frostbite$this.level);
                            iceBlock.disableDrop();
                            iceBlock.moveTo(Vec3.atCenterOf(pos));
                            iceBlock.addDeltaMovement(new Vec3(0, 0.5, 0));
                            frostbite$this.level.addFreshEntity(iceBlock);

                            itemStack.set(DataComponentTypeRegistry.CHARGE, new ChargeData(60));
                        }
                    } else if (frostbite$this.player.getItemInHand(InteractionHand.OFF_HAND).is(ItemRegistry.ICE_HAMMER)) {
                        ItemStack itemStack = frostbite$this.player.getItemInHand(InteractionHand.OFF_HAND);
                        if (itemStack.get(DataComponentTypeRegistry.CHARGE).charge() <= 0) {
                            IceBlockEntity iceBlock = new IceBlockEntity(EntityRegistry.ICE_BLOCK.get(), frostbite$this.level);
                            iceBlock.disableDrop();
                            iceBlock.moveTo(Vec3.atCenterOf(pos));
                            iceBlock.addDeltaMovement(new Vec3(0, 0.5, 0));
                            frostbite$this.level.addFreshEntity(iceBlock);

                            itemStack.set(DataComponentTypeRegistry.CHARGE, new ChargeData(60));
                        }
                    } else {
                        frostbite$this.destroyAndAck(pos, sequence, "creative destroy");
                    }
                    return;
                }

                if (frostbite$this.player.blockActionRestricted(frostbite$this.level, pos, frostbite$this.getGameModeForPlayer())) {
                    frostbite$this.player.connection.send(new ClientboundBlockUpdatePacket(pos, frostbite$this.level.getBlockState(pos)));
                    frostbite$this.debugLogging(pos, false, sequence, "block action restricted");
                    return;
                }

                frostbite$this.destroyProgressStart = frostbite$this.gameTicks;
                float f = 1.0F;
                BlockState blockstate = frostbite$this.level.getBlockState(pos);
                if (!blockstate.isAir()) {
                    EnchantmentHelper.onHitBlock(frostbite$this.level, frostbite$this.player.getMainHandItem(), frostbite$this.player, frostbite$this.player, EquipmentSlot.MAINHAND, Vec3.atCenterOf(pos), blockstate, (p_348149_) -> frostbite$this.player.onEquippedItemBroken(p_348149_, EquipmentSlot.MAINHAND));
                    if (event.getUseBlock() != TriState.FALSE) {
                        blockstate.attack(frostbite$this.level, pos, frostbite$this.player);
                    }

                    f = blockstate.getDestroyProgress(frostbite$this.player, frostbite$this.player.level(), pos);
                }

                if (!blockstate.isAir() && f >= 1.0F) {
                    frostbite$this.destroyAndAck(pos, sequence, "insta mine");
                } else {
                    if (frostbite$this.isDestroyingBlock) {
                        frostbite$this.player.connection.send(new ClientboundBlockUpdatePacket(frostbite$this.destroyPos, frostbite$this.level.getBlockState(frostbite$this.destroyPos)));
                        frostbite$this.debugLogging(pos, false, sequence, "abort destroying since another started (client insta mine, server disagreed)");
                    }

                    frostbite$this.isDestroyingBlock = true;
                    frostbite$this.destroyPos = pos.immutable();
                    int i = (int)(f * 10.0F);
                    frostbite$this.level.destroyBlockProgress(frostbite$this.player.getId(), pos, i);
                    frostbite$this.debugLogging(pos, true, sequence, "actual start of destroying");
                    frostbite$this.lastSentState = i;
                }
            } else if (action == net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK) {
                if (pos.equals(frostbite$this.destroyPos)) {
                    int j = frostbite$this.gameTicks - frostbite$this.destroyProgressStart;
                    BlockState blockstate1 = frostbite$this.level.getBlockState(pos);
                    if (!blockstate1.isAir()) {
                        float f1 = blockstate1.getDestroyProgress(frostbite$this.player, frostbite$this.player.level(), pos) * (float)(j + 1);
                        if (f1 >= 0.7F) {
                            frostbite$this.isDestroyingBlock = false;
                            frostbite$this.level.destroyBlockProgress(frostbite$this.player.getId(), pos, -1);
                            frostbite$this.destroyAndAck(pos, sequence, "destroyed");
                            return;
                        }

                        if (!frostbite$this.hasDelayedDestroy) {
                            frostbite$this.isDestroyingBlock = false;
                            frostbite$this.hasDelayedDestroy = true;
                            frostbite$this.delayedDestroyPos = pos;
                            frostbite$this.delayedTickStart = frostbite$this.destroyProgressStart;
                        }
                    }
                }

                frostbite$this.debugLogging(pos, true, sequence, "stopped destroying");
            } else if (action == net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK) {
                frostbite$this.isDestroyingBlock = false;
                if (!Objects.equals(frostbite$this.destroyPos, pos)) {
                    frostbite$LOGGER.warn("Mismatch in destroy block pos: {} {}", frostbite$this.destroyPos, pos);
                    frostbite$this.level.destroyBlockProgress(frostbite$this.player.getId(), frostbite$this.destroyPos, -1);
                    frostbite$this.debugLogging(pos, true, sequence, "aborted mismatched destroying");
                }

                frostbite$this.level.destroyBlockProgress(frostbite$this.player.getId(), pos, -1);
                frostbite$this.debugLogging(pos, true, sequence, "aborted destroying");
            }

        }
        ci.cancel();
    }
}
