package org.exodusstudio.frostbite.platform.fabric;

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import org.exodusstudio.frostbite.common.attachment.DataAttachment;
import org.exodusstudio.frostbite.common.mixinterfaces.AttachmentHolder;
import org.exodusstudio.frostbite.fabric.common.registries.DataAttachmentRegistry;

public class DataAttachmentsPlatformImpl {
    public static <T> T getData(AttachmentHolder holder, DataAttachment<T> attachment) {
        return ((AttachmentTarget) holder).getAttachedOrCreate(DataAttachmentRegistry.get(attachment));
    }

    public static <T> void setData(AttachmentHolder holder, DataAttachment<T> attachment, T value) {
        ((AttachmentTarget) holder).setAttached(DataAttachmentRegistry.get(attachment), value);
    }

    public static boolean hasData(AttachmentHolder holder, DataAttachment<?> attachment) {
        return ((AttachmentTarget) holder).hasAttached(DataAttachmentRegistry.get(attachment));
    }
}
