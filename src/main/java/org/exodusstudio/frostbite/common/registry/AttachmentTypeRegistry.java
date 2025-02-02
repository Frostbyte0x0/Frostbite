package org.exodusstudio.frostbite.common.registry;

import com.mojang.serialization.Codec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.exodusstudio.frostbite.Frostbite;

import java.util.function.Supplier;

public class AttachmentTypeRegistry {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Frostbite.MOD_ID);

    public static final Supplier<AttachmentType<Float>> OUTER_TEMPERATURE = ATTACHMENT_TYPES.register(
            "outer_temperature", () -> AttachmentType.builder(() -> 20f).serialize(Codec.FLOAT).build());

    public static final Supplier<AttachmentType<Float>> INNER_TEMPERATURE = ATTACHMENT_TYPES.register(
            "inner_temperature", () -> AttachmentType.builder(() -> 20f).serialize(Codec.FLOAT).build());
}
