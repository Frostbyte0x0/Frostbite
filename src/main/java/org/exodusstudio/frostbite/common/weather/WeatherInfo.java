package org.exodusstudio.frostbite.common.weather;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;

public class WeatherInfo {
    private static final RandomSource source = RandomSource.create();
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

    public WeatherInfo() {}

    public float getBlizzardLevel(float partialTick) {
        return Mth.lerp(partialTick, oBlizzardLevel, blizzardLevel);
    }

    public void setSnowing() {
        snowTime = BLIZZARD_DELAY.sample(source);
        blizzardTime = 0;
        whiteoutTime = 0;
        isBlizzarding = false;
        isWhiteouting = false;
    }

    public void setBlizzarding() {
        snowTime = 0;
        int t = BLIZZARD_DELAY.sample(source);
        blizzardTime = t;
        whiteoutTime = t;
        isBlizzarding = true;
        isWhiteouting = false;
    }

    public void setWhiteouting() {
        snowTime = 0;
        int t = WHITEOUT_DELAY.sample(source);
        blizzardTime = t;
        whiteoutTime = t;
        isBlizzarding = true;
        isWhiteouting = true;
    }
}
