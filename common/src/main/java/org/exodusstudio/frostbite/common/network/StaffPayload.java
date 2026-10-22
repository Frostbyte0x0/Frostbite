package org.exodusstudio.frostbite.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.DebugEntityNameGenerator;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;

import java.util.UUID;

public record StaffPayload(StaffInfo staffInfo) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<StaffPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "staff_data"));

    public static final StreamCodec<FriendlyByteBuf, StaffPayload> STREAM_CODEC = CustomPacketPayload.codec(
            StaffPayload::write, StaffPayload::new
    );

    private StaffPayload(FriendlyByteBuf buf) {
        this(new StaffInfo(buf));
    }

    private void write(FriendlyByteBuf buffer) {
        this.staffInfo.write(buffer);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public record StaffInfo(String mode, UUID uuid) {
        public StaffInfo(FriendlyByteBuf buf) {
            this(buf.readUtf(), buf.readUUID());
        }

        public void write(FriendlyByteBuf buffer) {
            buffer.writeUtf(this.mode);
            buffer.writeUUID(this.uuid);
        }

        public String generateName() {
            return DebugEntityNameGenerator.getEntityName(this.uuid);
        }

        @Override
        public String toString() {
            return this.generateName();
        }
    }
}