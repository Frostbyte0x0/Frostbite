package org.exodusstudio.frostbite.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.exodusstudio.frostbite.Frostbite;

public record StaffData(String mode) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<StaffData> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "staff_data"));

    public static final StreamCodec<ByteBuf, StaffData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            StaffData::mode,
            StaffData::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}