package org.exodusstudio.frostbite.platform.neoforge;

import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.exodusstudio.frostbite.common.attachment.DataAttachment;
import org.exodusstudio.frostbite.common.mixinterfaces.AttachmentHolder;
import org.exodusstudio.frostbite.neoforge.common.registries.DataAttachmentRegistry;

public class DataAttachmentsPlatformImpl {
    public static <T> T getData(AttachmentHolder holder, DataAttachment<T> attachment) {
        return ((IAttachmentHolder) holder).getData(DataAttachmentRegistry.get(attachment));
    }

    public static <T> void setData(AttachmentHolder holder, DataAttachment<T> attachment, T value) {
        ((IAttachmentHolder) holder).setData(DataAttachmentRegistry.get(attachment), value);
    }

    public static boolean hasData(AttachmentHolder holder, DataAttachment<?> attachment) {
        return ((IAttachmentHolder) holder).hasData(DataAttachmentRegistry.get(attachment));
    }
}
