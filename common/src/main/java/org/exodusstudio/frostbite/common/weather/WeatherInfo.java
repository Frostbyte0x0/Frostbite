package org.exodusstudio.frostbite.common.weather;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;

public class WeatherInfo {
    public static final Codec<WeatherInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("snow_time").forGetter(w -> w.snowTime),
            Codec.INT.fieldOf("blizzard_time").forGetter(w -> w.blizzardTime),
            Codec.INT.fieldOf("whiteout_time").forGetter(w -> w.whiteoutTime),
            Codec.BOOL.fieldOf("is_blizzarding").forGetter(w -> w.isBlizzarding),
            Codec.BOOL.fieldOf("is_whiteouting").forGetter(w -> w.isWhiteouting),
            Codec.FLOAT.fieldOf("blizzard_level").forGetter(w -> w.blizzardLevel),
            Codec.FLOAT.fieldOf("whiteout_level").forGetter(w -> w.whiteoutLevel)
    ).apply(instance, WeatherInfo::new));

    public static final StreamCodec<ByteBuf, WeatherInfo> STREAM_CODEC = StreamCodec.of(
            WeatherInfo::toBuffer,
            WeatherInfo::fromBuffer
    );

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

    public static void toBuffer(final ByteBuf buffer, WeatherInfo info) {
        buffer.writeInt(info.snowTime);
        buffer.writeInt(info.blizzardTime);
        buffer.writeInt(info.whiteoutTime);
        buffer.writeBoolean(info.isBlizzarding);
        buffer.writeBoolean(info.isWhiteouting);
        buffer.writeFloat(info.blizzardLevel);
        buffer.writeFloat(info.whiteoutLevel);
    }

    public static WeatherInfo fromBuffer(ByteBuf buffer) {
        return new WeatherInfo(
                buffer.readInt(),
                buffer.readInt(),
                buffer.readInt(),
                buffer.readBoolean(),
                buffer.readBoolean(),
                buffer.readFloat(),
                buffer.readFloat()
        );
    }
}
