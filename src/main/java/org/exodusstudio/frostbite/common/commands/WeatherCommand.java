package org.exodusstudio.frostbite.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permissions;
import org.exodusstudio.frostbite.Frostbite;

public class WeatherCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("fweather")
                        .requires(p_139171_ -> p_139171_.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
                        .then(
                                Commands.literal("snow")
                                        .executes(context -> setSnow(context.getSource(), -1))
                                        .then(
                                                Commands.argument("duration", TimeArgument.time(1))
                                                        .executes(context -> setSnow(context.getSource(), IntegerArgumentType.getInteger(context, "duration")))
                                        )
                        )
                        .then(
                                Commands.literal("blizzard")
                                        .executes(context -> setBlizzard(context.getSource(), -1))
                                        .then(
                                                Commands.argument("duration", TimeArgument.time(1))
                                                        .executes(context -> setBlizzard(context.getSource(), IntegerArgumentType.getInteger(context, "duration")))
                                        )
                        )
                        .then(
                                Commands.literal("whiteout")
                                        .executes(context -> setWhiteout(context.getSource(), -1))
                                        .then(
                                                Commands.argument("duration", TimeArgument.time(1))
                                                        .executes(context -> setWhiteout(context.getSource(), IntegerArgumentType.getInteger(context, "duration")))
                                        )
                        )
        );
    }

    private static int setSnow(CommandSourceStack source, int time) {
        Frostbite.weatherInfo.setSnowing(Minecraft.getInstance().level.getGameTime());
        source.sendSuccess(() -> Component.literal("Set frostbite weather to snow"), true);
        return time;
    }

    private static int setBlizzard(CommandSourceStack source, int time) {
        Frostbite.weatherInfo.setBlizzarding(Minecraft.getInstance().level.getGameTime());
        source.sendSuccess(() -> Component.literal("Set frostbite weather to blizzard"), true);
        return time;
    }

    private static int setWhiteout(CommandSourceStack source, int time) {
        Frostbite.weatherInfo.setWhiteouting(Minecraft.getInstance().level.getGameTime());
        source.sendSuccess(() -> Component.literal("Set frostbite weather to whiteout"), true);
        return time;
    }
}