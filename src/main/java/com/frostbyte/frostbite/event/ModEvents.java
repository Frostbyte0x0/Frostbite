package com.frostbyte.frostbite.event;

import com.frostbyte.frostbite.Frostbite;
import com.frostbyte.frostbite.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.List;
import java.util.Objects;

@EventBusSubscriber(modid = Frostbite.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {
    @SubscribeEvent
    public static void inputEvent(InputEvent.MouseButton.Post event) {
        Frostbite.LOGGER.debug("Button pressed: {}", event.getButton());
    }

    @SubscribeEvent
    public static void entityTickEvent(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof Player player) {
            Frostbite.LOGGER.debug(String.valueOf(player.isUsingItem() && player.getUseItem().is(Items.SPYGLASS)));
            Frostbite.LOGGER.debug("With get: {}", player.isUsingItem() && player.getUseItem().is(ModItems.DRAINING_STAFF.get()));
            Frostbite.LOGGER.debug("Without get: {}", player.isUsingItem() && player.getUseItem().is(ModItems.DRAINING_STAFF));
            if (Objects.equals(String.valueOf(player.getItemInHand(InteractionHand.MAIN_HAND)),
                    String.valueOf(ModItems.DRAINING_STAFF.toStack()))
                    && player.isUsingItem()) {
                player.displayClientMessage(Component.literal("Using item!"), false);
            }
        }
    }

//    @SubscribeEvent
//    public static void explosionEvent(ExplosionEvent.Detonate event) {
//        BlockPos[] blocks = (BlockPos[]) event.getAffectedBlocks().toArray();
//        for (int i = 0; i < blocks.length; i++) {
//            FallingBlockEntity fallingBlock = new FallingBlockEntity(event.getLevel(),
//                    blocks[i].getX(), blocks[i].getY(), blocks[i].getZ(), Blocks.SAND.defaultBlockState());
//        }
//    }
}
