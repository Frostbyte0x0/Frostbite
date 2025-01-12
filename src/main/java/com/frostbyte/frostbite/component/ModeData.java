package com.frostbyte.frostbite.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Objects;

public record ModeData(String mode) {
    public static final Codec<ModeData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(Codec.STRING.fieldOf("mode").forGetter(ModeData::mode)).apply(instance, ModeData::new));

    @Override
    public boolean equals(Object obj) {
        return (obj == this) || (obj instanceof ModeData(String mode1) && Objects.equals(this.mode, mode1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.mode);
    }
}
