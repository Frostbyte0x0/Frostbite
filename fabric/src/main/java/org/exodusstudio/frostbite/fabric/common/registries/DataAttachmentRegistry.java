package org.exodusstudio.frostbite.fabric.common.registries;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import org.exodusstudio.frostbite.common.attachment.DataAttachment;

import java.util.HashMap;
import java.util.Map;

public class DataAttachmentRegistry {
    public static final Map<DataAttachment<?>, AttachmentType<?>> ATTACHMENTS = new HashMap<>();

    public static void register() {
        for (DataAttachment<?> attachment : org.exodusstudio.frostbite.common.registry.AttachmentRegistry.ATTACHMENT_TYPES.getEntries()) {
            ATTACHMENTS.put(attachment, create(attachment));
        }
    }

    private static <T> AttachmentType<T> create(DataAttachment<T> attachment) {
        return AttachmentRegistry.create(attachment.id(), builder -> {
            builder.initializer(attachment.defaultValue())
                    .persistent(attachment.serializer().codec());
            if (attachment.syncCodec() != null) builder.syncWith(attachment.syncCodec(), AttachmentSyncPredicate.all());
            if (attachment.copyOnDeath()) builder.copyOnDeath();
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> AttachmentType<T> get(DataAttachment<T> attachment) {
        return (AttachmentType<T>) ATTACHMENTS.get(attachment);
    }
}
