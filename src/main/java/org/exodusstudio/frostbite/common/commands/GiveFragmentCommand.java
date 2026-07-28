package org.exodusstudio.frostbite.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import org.exodusstudio.frostbite.common.component.ContractAttributeData;
import org.exodusstudio.frostbite.common.contracts.ContractAttribute;
import org.exodusstudio.frostbite.common.contracts.ContractAttributes;
import org.exodusstudio.frostbite.common.contracts.ContractRank;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.exodusstudio.frostbite.common.util.helpers.DataHelper;

import java.util.Collection;
import java.util.List;

public class GiveFragmentCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("give_fragment")
                        .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(
                                Commands.argument("targets", EntityArgument.players())
                                        .then(
                                                Commands.argument("fragment", FragmentArgument.create())
                                                        .executes(c ->
                                                                giveFragment(c.getSource(), c.getArgument("fragment", String.class), EntityArgument.getPlayers(c, "targets"), 1))
                                                        .then(
                                                                Commands.argument("level", IntegerArgumentType.integer(1, 4))
                                                                        .executes(
                                                                                c -> giveFragment(
                                                                                        c.getSource(),
                                                                                        c.getArgument("fragment", String.class),
                                                                                        EntityArgument.getPlayers(c, "targets"),
                                                                                        IntegerArgumentType.getInteger(c, "level")
                                                                                )
                                                                        )
                                                        )
                                        )
                        )
        );
    }

    private static int giveFragment(CommandSourceStack source, String fragment, Collection<ServerPlayer> players, int level) {
        if (!ContractAttributes.ATTRIBUTES.containsKey(fragment)) {
            source.sendFailure(Component.translatable("commands.give_fragment_unknown", fragment));
            return 0;
        }
        if (level < 1 || level > 3) {
            source.sendFailure(Component.translatable("commands.give_fragment_invalid_level"));
            return 0;
        }
        ItemStack stack = new ItemStack(ItemRegistry.CONTRACT_FRAGMENTS.get(fragment).asItem());
        ContractAttribute a = ContractAttributes.ATTRIBUTES.get(fragment);
        stack.set(DataComponentTypeRegistry.CONTRACT_ATTRIBUTE, new ContractAttributeData(a));
        if (a.isScalable()) {
            DataHelper.setData(stack, "level", level);
            stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), List.of(ContractRank.fromNum(level).name()), List.of()));
        } else {
            stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), List.of(a.getRank().name()), List.of()));
        }

        for (ServerPlayer player : players) {
            boolean added = player.getInventory().add(stack);
            if (added) {
                ItemEntity drop = player.drop(stack.copy(), false);
                if (drop != null) {
                    drop.makeFakeItem();
                }

                player.level()
                        .playSound(
                                null,
                                player.getX(),
                                player.getY(),
                                player.getZ(),
                                SoundEvents.ITEM_PICKUP,
                                SoundSource.PLAYERS,
                                0.2F,
                                ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F
                        );
                player.containerMenu.broadcastChanges();
            } else {
                ItemEntity drop = player.drop(stack, false);
                if (drop != null) {
                    drop.setNoPickUpDelay();
                    drop.setTarget(player.getUUID());
                }
            }
        }

        if (players.size() == 1) {
            source.sendSuccess(
                    () -> Component.translatable(
                            "commands.give.success.single", 1, stack.getDisplayName(), players.iterator().next().getDisplayName()
                    ),
                    true
            );
        } else {
            source.sendSuccess(() -> Component.translatable("commands.give.success.single", 1, stack.getDisplayName(), players.size()), true);
        }

        return players.size();
    }
}