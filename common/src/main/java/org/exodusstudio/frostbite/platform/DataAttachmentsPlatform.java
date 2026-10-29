package org.exodusstudio.frostbite.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import org.exodusstudio.frostbite.common.attachment.DataAttachment;
import org.exodusstudio.frostbite.common.mixinterfaces.AttachmentHolder;

public class DataAttachmentsPlatform {
    @ExpectPlatform
    public static <T> T getData(AttachmentHolder holder, DataAttachment<T> attachment) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T> void setData(AttachmentHolder holder, DataAttachment<T> attachment, T value) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean hasData(AttachmentHolder holder, DataAttachment<?> attachment) {
        throw new AssertionError();
    }
}
