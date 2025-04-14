package org.exodusstudio.frostbite.common.item.custom.alchemy;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.exodusstudio.frostbite.common.component.JarContentsData;
import org.exodusstudio.frostbite.common.entity.custom.SporeCloudEntity;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EmptyJarItem extends Item {
    public EmptyJarItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, @NotNull InteractionHand hand) {
        List<SporeCloudEntity> list = level.getEntitiesOfClass(SporeCloudEntity.class,
                player.getBoundingBox().inflate(2.0));
        ItemStack itemstack = player.getItemInHand(hand);
        if (!list.isEmpty()) {
            SporeCloudEntity sporeCloudEntity = list.getFirst();
            sporeCloudEntity.setRadius(sporeCloudEntity.getRadius() - 0.5F);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.NEUTRAL, 1.0F, 1.0F);
            level.gameEvent(player, GameEvent.FLUID_PICKUP, player.position());
            if (player instanceof ServerPlayer serverplayer) {
                CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY.trigger(serverplayer, itemstack, sporeCloudEntity);
            }

            ItemStack stack = ItemStack.EMPTY;
            if (!sporeCloudEntity.getPotionContents().customEffects().isEmpty()) {
                stack = new ItemStack(ItemRegistry.JAR.get());
                stack.set(DataComponentTypeRegistry.JAR_CONTENTS, new JarContentsData(sporeCloudEntity.getPotionContents()));
            }

            return InteractionResult.SUCCESS.heldItemTransformedTo(this.turnJarIntoItem(itemstack, player, stack));
        }

        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (blockhitresult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = blockhitresult.getBlockPos();
            if (!level.mayInteract(player, blockpos)) {
                return InteractionResult.PASS;
            }

            if (level.getFluidState(blockpos).is(FluidTags.WATER)) {
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                level.gameEvent(player, GameEvent.FLUID_PICKUP, blockpos);
                return InteractionResult.SUCCESS.heldItemTransformedTo(this.turnJarIntoItem(player.getItemInHand(hand), player, JarContentsData.createItemStack(ItemRegistry.JAR.asItem(), Jars.WATER)));
            }
        }

        return InteractionResult.FAIL;
    }

    protected ItemStack turnJarIntoItem(ItemStack jarStack, Player player, ItemStack filledJarStack) {
        player.awardStat(Stats.ITEM_USED.get(this));
        return ItemUtils.createFilledResult(jarStack, player, filledJarStack);
    }
}
