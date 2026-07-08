package org.exodusstudio.frostbite.common.registry;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.util.TemperatureManager;

import java.util.function.Supplier;

public class AttachementRegistry {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Frostbite.MOD_ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, Integer> INT_STREAM_CODEC = StreamCodec.of(
            FriendlyByteBuf::writeInt,
            FriendlyByteBuf::readInt);
    public static final StreamCodec<RegistryFriendlyByteBuf, Long> LONG_STREAM_CODEC = StreamCodec.of(
            FriendlyByteBuf::writeLong,
            FriendlyByteBuf::readLong);
    public static final StreamCodec<RegistryFriendlyByteBuf, Float> FLOAT_STREAM_CODEC = StreamCodec.of(
            FriendlyByteBuf::writeFloat,
            FriendlyByteBuf::readFloat);
    public static final StreamCodec<RegistryFriendlyByteBuf, String> STRING_STREAM_CODEC = StreamCodec.of(
            FriendlyByteBuf::writeUtf,
            FriendlyByteBuf::readUtf);

    public static final Supplier<AttachmentType<Integer>> COMBO_INDEX = ATTACHMENT_TYPES.register(
            "combo_index", () -> AttachmentType.builder(() -> 0)
                    .sync(INT_STREAM_CODEC)
                    .serialize(Codec.INT.fieldOf("combo_index")).build());

    public static final Supplier<AttachmentType<Long>> LAST_HIT = ATTACHMENT_TYPES.register(
            "last_hit", () -> AttachmentType.builder(() -> 0L)
                    .sync(LONG_STREAM_CODEC)
                    .serialize(Codec.LONG.fieldOf("last_hit")).build());

    public static final Supplier<AttachmentType<Float>> INNER_TEMPERATURE = ATTACHMENT_TYPES.register(
            "inner_temperature", () -> AttachmentType.builder(() -> TemperatureManager.MAX_TEMP)
                    .sync(FLOAT_STREAM_CODEC)
                    .serialize(Codec.FLOAT.fieldOf("inner_temperature")).build());

    public static final Supplier<AttachmentType<Float>> OUTER_TEMPERATURE = ATTACHMENT_TYPES.register(
            "outer_temperature", () -> AttachmentType.builder(() -> TemperatureManager.MAX_TEMP)
                    .sync(FLOAT_STREAM_CODEC)
                    .serialize(Codec.FLOAT.fieldOf("outer_temperature")).build());

    public static final Supplier<AttachmentType<String>> UNLOCKED_ENTRIES = ATTACHMENT_TYPES.register(
            "unlocked_entries", () -> AttachmentType.builder(() -> "")
                    .sync(STRING_STREAM_CODEC)
                    .serialize(Codec.STRING.fieldOf("unlocked_entries")).build());
}
