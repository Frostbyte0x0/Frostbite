package org.exodusstudio.frostbite.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;

public record ParryPayload(boolean start) implements CustomPacketPayload {
    public static final Type<ParryPayload> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "parry"));
    public static final StreamCodec<FriendlyByteBuf, ParryPayload> STREAM_CODEC =
            StreamCodec.of((buf, payload) -> buf.writeBoolean(payload.start),
                    buf -> new ParryPayload(buf.readBoolean()));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
