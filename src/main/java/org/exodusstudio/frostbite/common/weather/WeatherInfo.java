package org.exodusstudio.frostbite.common.weather;

import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;

public class WeatherInfo {
    public static final IntProvider BLIZZARD_DELAY = UniformInt.of(12000, 180000);
    public static final IntProvider BLIZZARD_DURATION = UniformInt.of(12000, 24000);
    public static final IntProvider WHITEOUT_DELAY = UniformInt.of(12000, 180000);
    public static final IntProvider WHITEOUT_DURATION = UniformInt.of(3600, 15600);

    public int snowTime;
    public int blizzardTime;
    public int whiteoutTime;
    public boolean isBlizzarding;
    public boolean isWhiteouting;
    public float oBlizzardLevel = 0;
    public float blizzardLevel;
    public float oWhiteoutLevel = 0;
    public float whiteoutLevel;

    public WeatherInfo(
            int snowTime,
            int blizzardTime,
            int whiteoutTime,
            boolean isBlizzarding,
            boolean isWhiteouting,
            float blizzardLevel,
            float whiteoutLevel
    ) {
        this.snowTime = snowTime;
        this.blizzardTime = blizzardTime;
        this.whiteoutTime = whiteoutTime;
        this.isBlizzarding = isBlizzarding;
        this.isWhiteouting = isWhiteouting;
        this.blizzardLevel = blizzardLevel;
        this.whiteoutLevel = whiteoutLevel;
    }

    public WeatherInfo() {
        this.snowTime = 0;
        this.blizzardTime = 0;
        this.whiteoutTime = 0;
        this.isBlizzarding = false;
        this.isWhiteouting = false;
        this.blizzardLevel = 0;
        this.whiteoutLevel = 0;
    }

    public float getBlizzardLevel(float partialTick) {
        return Mth.lerp(partialTick, oBlizzardLevel, blizzardLevel);
    }

    public float getWhiteoutLevel(float partialTick) {
        return Mth.lerp(partialTick, oWhiteoutLevel, whiteoutLevel);
    }
}
