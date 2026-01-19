package org.exodusstudio.frostbite.common.registry;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.exodusstudio.frostbite.Frostbite;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class AttachementRegistry {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Frostbite.MOD_ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, Float> FLOAT_STREAM_CODEC = StreamCodec.of(
            FriendlyByteBuf::writeFloat,
            FriendlyByteBuf::readFloat);
    public static final StreamCodec<RegistryFriendlyByteBuf, String> STRING_STREAM_CODEC = StreamCodec.of(
            FriendlyByteBuf::writeUtf,
            FriendlyByteBuf::readUtf);

    public static final Supplier<AttachmentType<Float>> INNER_TEMPERATURE = ATTACHMENT_TYPES.register(
            "inner_temperature", () -> AttachmentType.builder(() -> 0f)
                    .sync(FLOAT_STREAM_CODEC)
                    .serialize(Codec.FLOAT.fieldOf("inner_temperature")).build());

    public static final Supplier<AttachmentType<Float>> OUTER_TEMPERATURE = ATTACHMENT_TYPES.register(
            "outer_temperature", () -> AttachmentType.builder(() -> 0f)
                    .sync(FLOAT_STREAM_CODEC)
                    .serialize(Codec.FLOAT.fieldOf("outer_temperature")).build());

    public static final Supplier<AttachmentType<String>> UNLOCKED_ENTRIES = ATTACHMENT_TYPES.register(
            "unlocked_entries", () -> AttachmentType.builder(() -> "")
                    .sync(STRING_STREAM_CODEC)
                    .serialize(Codec.STRING.fieldOf("unlocked_entries")).build());
}
