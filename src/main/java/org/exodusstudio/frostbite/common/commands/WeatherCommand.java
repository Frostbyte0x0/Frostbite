package org.exodusstudio.frostbite.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.IntProvider;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.weather.WeatherInfo;

public class WeatherCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("fweather")
                        .requires(p_139171_ -> p_139171_.hasPermission(2))
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

    private static int getDuration(CommandSourceStack source, int time, IntProvider timeProvider) {
        return time == -1 ? timeProvider.sample(source.getServer().overworld().getRandom()) : time;
    }

    private static int setSnow(CommandSourceStack source, int time) {
        Frostbite.weatherInfo.snowTime = getDuration(source, time, WeatherInfo.BLIZZARD_DELAY);
        Frostbite.weatherInfo.blizzardTime = 0;
        Frostbite.weatherInfo.whiteoutTime = 0;
        Frostbite.weatherInfo.isBlizzarding = false;
        Frostbite.weatherInfo.isWhiteouting = false;
        source.sendSuccess(() -> Component.literal("Set frostbite weather to snow"), true);
        return time;
    }

    private static int setBlizzard(CommandSourceStack source, int time) {
        Frostbite.weatherInfo.snowTime = 0;
        int t = getDuration(source, time, WeatherInfo.BLIZZARD_DELAY);
        Frostbite.weatherInfo.blizzardTime = t;
        Frostbite.weatherInfo.whiteoutTime = t;
        Frostbite.weatherInfo.isBlizzarding = true;
        Frostbite.weatherInfo.isWhiteouting = false;
        source.sendSuccess(() -> Component.literal("Set frostbite weather to blizzard"), true);
        return time;
    }

    private static int setWhiteout(CommandSourceStack source, int time) {
        Frostbite.weatherInfo.snowTime = 0;
        int t = getDuration(source, time, WeatherInfo.WHITEOUT_DELAY);
        Frostbite.weatherInfo.blizzardTime = t;
        Frostbite.weatherInfo.whiteoutTime = t;
        Frostbite.weatherInfo.isBlizzarding = true;
        Frostbite.weatherInfo.isWhiteouting = true;
        source.sendSuccess(() -> Component.literal("Set frostbite weather to whiteout"), true);
        return time;
    }
}
