package org.exodusstudio.frostbite.common.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.client.PlayerHeartData;
import org.jetbrains.annotations.NotNull;

public record PlayerHeartDataHandler(boolean showCustomHeart) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PlayerHeartDataHandler> TYPE = new CustomPacketPayload.Type<>(NetworkManager.PLAYER_HEART_DATA_PACKET_ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerHeartDataHandler> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull PlayerHeartDataHandler decode(RegistryFriendlyByteBuf buf) {
            return new PlayerHeartDataHandler(buf.readBoolean());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, PlayerHeartDataHandler packet) {
            buf.writeBoolean(packet.showCustomHeart);
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PlayerHeartDataHandler payload, Player player) {
        if (player != null && player.isLocalPlayer()) {
            PlayerHeartData.shouldShow = payload.showCustomHeart();
        }
    }
}