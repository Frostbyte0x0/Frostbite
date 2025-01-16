package com.frostbyte.frostbite.item.alchemy;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;

public class Jar {
    public static final Codec<Holder<Jar>> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Jar>> STREAM_CODEC;
    private final String name;
    private final List<MobEffectInstance> effects;

    public Jar(String name, MobEffectInstance... effects) {
        this.name = name;
        this.effects = List.of(effects);
    }

    public List<MobEffectInstance> getEffects() {
        return this.effects;
    }

    public String name() {
        return this.name;
    }

    static {
        CODEC = BuiltInRegistries.POTION.holderByNameCodec();
        STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.POTION);
    }
}
