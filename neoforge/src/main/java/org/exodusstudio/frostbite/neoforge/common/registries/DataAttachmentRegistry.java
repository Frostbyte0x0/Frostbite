package org.exodusstudio.frostbite.neoforge.common.registries;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.attachment.DataAttachment;
import org.exodusstudio.frostbite.common.registry.AttachmentRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DataAttachmentRegistry {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Frostbite.MOD_ID);
    public static final Map<DataAttachment<?>, Supplier<AttachmentType<?>>> ATTACHMENTS = new HashMap<>();

    public static void register(IEventBus bus) {
        for (DataAttachment<?> attachment : AttachmentRegistry.ATTACHMENT_TYPES.getEntries()) {
            ATTACHMENTS.put(attachment, ATTACHMENT_TYPES.register(attachment.id().getPath(), () -> create(attachment)));
        }

        ATTACHMENT_TYPES.register(bus);
    }

    private static <T> AttachmentType<T> create(DataAttachment<T> attachment) {
        AttachmentType.Builder<T> builder = AttachmentType.builder(attachment.defaultValue())
                .serialize(attachment.serializer());
        if (attachment.syncCodec() != null) builder.sync(attachment.syncCodec());
        if (attachment.copyOnDeath()) builder.copyOnDeath();
        return builder.build();
    }

    @SuppressWarnings("unchecked")
    public static <T> AttachmentType<T> get(DataAttachment<T> attachment) {
        return (AttachmentType<T>) ATTACHMENTS.get(attachment).get();
    }
}
