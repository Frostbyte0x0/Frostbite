package org.exodusstudio.frostbite.common.weather;

import net.minecraft.client.Minecraft;
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
    public static final float normalNearFog = 0;
    public static final float normalRed = 130 / 255f;
    public static final float normalGreen = 128 / 255f;
    public static final float normalBlue = 214 / 255f;
    public static final float BLIZZARD_NEAR_FOG = -50f;
    public static final float BLIZZARD_FAR_FOG = 100f;
    public static final float WHITEOUT_NEAR_FOG = -50f;
    public static final float WHITEOUT_FAR_FOG = 50f;
    public static final float BLIZZARD_COLOUR = 150 / 255f;
    public static final float WHITEOUT_COLOUR = 200 / 255f;

    public float normalFarFog;
    public int snowTime;
    public int blizzardTime;
    public int whiteoutTime;
    public boolean isBlizzarding;
    public boolean isWhiteouting;
    public float oBlizzardLevel = 0;
    public float blizzardLevel;
    public float oWhiteoutLevel = 0;
    public float whiteoutLevel;
    public float oNearFog;
    public float oFarFog;
    public float nearFog;
    public float farFog;
    public float oRed;
    public float oGreen;
    public float oBlue;
    public float red;
    public float green;
    public float blue;
    public double timeSinceLastUpdate = 0;

    public WeatherInfo(
            int snowTime,
            int blizzardTime,
            int whiteoutTime,
            boolean isBlizzarding,
            boolean isWhiteouting,
            float blizzardLevel,
            float whiteoutLevel
    ) {
        normalFarFog = Minecraft.getInstance().options.getEffectiveRenderDistance() * 16;
        this.snowTime = snowTime;
        this.blizzardTime = blizzardTime;
        this.whiteoutTime = whiteoutTime;
        this.isBlizzarding = isBlizzarding;
        this.isWhiteouting = isWhiteouting;
        this.blizzardLevel = blizzardLevel;
        this.whiteoutLevel = whiteoutLevel;
        this.oNearFog = normalNearFog;
        this.oFarFog = normalFarFog;
        this.nearFog = normalNearFog;
        this.farFog = normalFarFog;
        this.oRed = normalRed;
        this.oGreen = normalGreen;
        this.oBlue = normalBlue;
        this.red = normalRed;
        this.green = normalGreen;
        this.blue = normalBlue;
    }

    public WeatherInfo() {
        normalFarFog = 0;
    }

    public float getBlizzardLevel(double partialTick) {
        return (float) Mth.lerp(partialTick, oBlizzardLevel, blizzardLevel);
    }

    public float getLerp() {
        if (Minecraft.getInstance().level == null) return 0;
        return (float) Mth.clamp((Minecraft.getInstance().level.getGameTime() - timeSinceLastUpdate) / 100f, 0, 1);
    }

    public void setSnowing(float currentTime) {
        snowTime = BLIZZARD_DELAY.sample(source);
        blizzardTime = 0;
        whiteoutTime = 0;
        isBlizzarding = false;
        isWhiteouting = false;
        oNearFog = nearFog;
        oFarFog = farFog;
        oRed = red;
        oGreen = green;
        oBlue = blue;
        nearFog = normalNearFog;
        farFog = normalFarFog;
        red = normalRed;
        green = normalGreen;
        blue = normalBlue;

        timeSinceLastUpdate = currentTime;
    }

    public void setBlizzarding(float currentTime) {
        snowTime = 0;
        int t = BLIZZARD_DELAY.sample(source);
        blizzardTime = t;
        whiteoutTime = t;
        isBlizzarding = true;
        isWhiteouting = false;
        oNearFog = nearFog;
        oFarFog = farFog;
        nearFog = BLIZZARD_NEAR_FOG;
        farFog = BLIZZARD_FAR_FOG;
        oRed = red;
        oGreen = green;
        oBlue = blue;
        red = BLIZZARD_COLOUR;
        green = BLIZZARD_COLOUR;
        blue = BLIZZARD_COLOUR;
        timeSinceLastUpdate = currentTime;
    }

    public void setWhiteouting(float currentTime) {
        snowTime = 0;
        int t = WHITEOUT_DELAY.sample(source);
        blizzardTime = t;
        whiteoutTime = t;
        isBlizzarding = true;
        isWhiteouting = true;
        oNearFog = nearFog;
        oFarFog = farFog;
        nearFog = WHITEOUT_NEAR_FOG;
        farFog = WHITEOUT_FAR_FOG;
        oRed = red;
        oGreen = green;
        oBlue = blue;
        red = WHITEOUT_COLOUR;
        green = WHITEOUT_COLOUR;
        blue = WHITEOUT_COLOUR;
        timeSinceLastUpdate = currentTime;
    }
}
