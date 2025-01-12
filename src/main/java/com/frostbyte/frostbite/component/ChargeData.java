package com.frostbyte.frostbite.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Objects;

public record ChargeData(int charge) {
    public static final Codec<ChargeData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(Codec.INT.fieldOf("charge").forGetter(ChargeData::charge)).apply(instance, ChargeData::new));

    @Override
    public boolean equals(Object obj) {
        return (obj == this) || (obj instanceof ChargeData(int charge) && Objects.equals(this.charge, charge));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.charge);
    }
}
