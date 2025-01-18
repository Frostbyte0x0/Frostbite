package com.frostbyte.frostbite.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record GunData(boolean bulletInChamber,
                      int bulletsInMag,
                      boolean isReloading,
                      int reloadCooldown,
                      boolean isChambering,
                      int chamberCooldown) {
    public static final GunData EMPTY = new GunData(false, 0, false, 0, false, 0);

    public static final Codec<GunData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.BOOL.fieldOf("bulletInChamber").forGetter(GunData::bulletInChamber),
                    Codec.INT.fieldOf("bulletsInMag").forGetter(GunData::bulletsInMag),
                    Codec.BOOL.fieldOf("isReloading").forGetter(GunData::isReloading),
                    Codec.INT.fieldOf("reloadCooldown").forGetter(GunData::reloadCooldown),
                    Codec.BOOL.fieldOf("isChambering").forGetter(GunData::isChambering),
                    Codec.INT.fieldOf("chamberCooldown").forGetter(GunData::chamberCooldown))
                    .apply(instance, GunData::new));

    public static final StreamCodec<ByteBuf, GunData> STREAM_CODEC;

    static {
        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL, GunData::bulletInChamber,
                ByteBufCodecs.INT, GunData::bulletsInMag,
                ByteBufCodecs.BOOL, GunData::isReloading,
                ByteBufCodecs.INT, GunData::reloadCooldown,
                ByteBufCodecs.BOOL, GunData::isChambering,
                ByteBufCodecs.INT, GunData::chamberCooldown,
                GunData::new);
    }
}
