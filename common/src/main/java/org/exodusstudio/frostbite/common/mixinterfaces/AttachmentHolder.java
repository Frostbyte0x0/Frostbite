package org.exodusstudio.frostbite.common.mixinterfaces;

import org.exodusstudio.frostbite.common.attachment.DataAttachment;
import org.exodusstudio.frostbite.platform.DataAttachmentsPlatform;

public interface AttachmentHolder {
    default <T> T getData(DataAttachment<T> attachment) {
        return DataAttachmentsPlatform.getData(this, attachment);
    }

    default <T> void setData(DataAttachment<T> attachment, T value) {
        DataAttachmentsPlatform.setData(this, attachment, value);
    }

    default boolean hasData(DataAttachment<?> attachment) {
        return DataAttachmentsPlatform.hasData(this, attachment);
    }
}
