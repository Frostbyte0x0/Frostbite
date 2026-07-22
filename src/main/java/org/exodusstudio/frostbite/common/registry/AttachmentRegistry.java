package org.exodusstudio.frostbite.common.registry;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.contracts.PlayerContractInfo;
import org.exodusstudio.frostbite.common.util.TemperatureManager;

import java.util.*;
import java.util.function.Supplier;

public class AttachmentRegistry {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Frostbite.MOD_ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, Integer> INT_STREAM_CODEC = StreamCodec.of(
            FriendlyByteBuf::writeInt,
            FriendlyByteBuf::readInt);
    public static final StreamCodec<RegistryFriendlyByteBuf, Long> LONG_STREAM_CODEC = StreamCodec.of(
            FriendlyByteBuf::writeLong,
            FriendlyByteBuf::readLong);
    public static final StreamCodec<RegistryFriendlyByteBuf, Float> FLOAT_STREAM_CODEC = StreamCodec.of(
            FriendlyByteBuf::writeFloat,
            FriendlyByteBuf::readFloat);
    public static final StreamCodec<RegistryFriendlyByteBuf, String> STRING_STREAM_CODEC = StreamCodec.of(
            FriendlyByteBuf::writeUtf,
            FriendlyByteBuf::readUtf);
    public static final StreamCodec<RegistryFriendlyByteBuf, Map<UUID, List<Pair<String, Long>>>> MAP_UUID_LIST_STRING_LONG_PAIR_STREAM_CODEC = StreamCodec.of(
            (b, m) -> {
                b.writeInt(m.size());
                m.forEach((uuid, s) -> {
                    b.writeInt(s.size());
                    s.forEach(p -> {
                        b.writeUtf(p.getFirst());
                        b.writeLong(p.getSecond());
                    });
                    b.writeUUID(uuid);
                });
            },
            b -> {
                Map<UUID, List<Pair<String, Long>>> m = new HashMap<>();
                int size = b.readInt();
                for (int i = 0; i < size; i++) {
                    int listSize = b.readInt();
                    List<Pair<String, Long>> s = new ArrayList<>();
                    for (int j = 0; j < listSize; j++) {
                        s.add(Pair.of(b.readUtf(), b.readLong()));
                    }
                    UUID uuid = b.readUUID();
                    m.put(uuid, s);
                }
                return m;
            });

    public static Codec<UUID> UUID_CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<UUID, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getStringValue(input).flatMap(s -> {
                try {
                    return DataResult.success(Pair.of(UUID.fromString(s), input));
                } catch (IllegalArgumentException e) {
                    return DataResult.error(() -> "Invalid UUID string: " + s);
                }
            });
        }

        @Override
        public <T> DataResult<T> encode(UUID uuid, DynamicOps<T> ops, T prefix) {
            return ops.mergeToPrimitive(prefix, ops.createString(uuid.toString()));
        }
    };

    public static Codec<Pair<String, Long>> LONG_STRING_PAIR_CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<Pair<String, Long>, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getStringValue(input).flatMap(s -> {
                try {
                    String[] parts = s.split(":");
                    return DataResult.success(Pair.of(Pair.of(parts[0], Long.parseLong(parts[1])), input));
                } catch (IllegalArgumentException e) {
                    return DataResult.error(() -> "Invalid UUID string: " + s);
                }
            });
        }

        @Override
        public <T> DataResult<T> encode(Pair<String, Long> pair, DynamicOps<T> ops, T prefix) {
            return ops.mergeToPrimitive(prefix, ops.createString(pair.getFirst() + ":" + pair.getSecond()));
        }
    };


    public static final Supplier<AttachmentType<PlayerContractInfo>> PLAYER_CONTRACT_INFO = ATTACHMENT_TYPES.register(
            "player_contract_info", () -> AttachmentType.builder(() -> PlayerContractInfo.EMPTY)
                    .sync(StreamCodec.of(PlayerContractInfo::toBuffer, PlayerContractInfo::fromBuffer))
                    .serialize(PlayerContractInfo.CODEC.fieldOf("player_contract_info"))
                    .copyOnDeath().build());

    public static final Supplier<AttachmentType<Integer>> COMBO_INDEX = ATTACHMENT_TYPES.register(
            "combo_index", () -> AttachmentType.builder(() -> 0)
                    .sync(INT_STREAM_CODEC)
                    .serialize(Codec.INT.fieldOf("combo_index")).build());

    public static final Supplier<AttachmentType<Long>> LAST_HIT = ATTACHMENT_TYPES.register(
            "last_hit", () -> AttachmentType.builder(() -> 0L)
                    .sync(LONG_STREAM_CODEC)
                    .serialize(Codec.LONG.fieldOf("last_hit")).build());

    public static final Supplier<AttachmentType<Float>> COMBO_LENGTH = ATTACHMENT_TYPES.register(
            "combo_length", () -> AttachmentType.builder(() -> 0f)
                    .sync(FLOAT_STREAM_CODEC)
                    .serialize(Codec.FLOAT.fieldOf("combo_length")).build());

    public static final Supplier<AttachmentType<Map<UUID, List<Pair<String, Long>>>>> CURRENT_RENDERING_ATTACKS = ATTACHMENT_TYPES.register(
            "current_rendering_attacks",
            () -> AttachmentType.builder(() -> (Map<UUID, List<Pair<String, Long>>>) new HashMap<UUID, List<Pair<String, Long>>>())
                    .sync(MAP_UUID_LIST_STRING_LONG_PAIR_STREAM_CODEC)
                    .serialize(Codec.unboundedMap(UUID_CODEC, LONG_STRING_PAIR_CODEC.listOf()).fieldOf("current_rendering_attacks")).build());

    public static final Supplier<AttachmentType<Float>> INNER_TEMPERATURE = ATTACHMENT_TYPES.register(
            "inner_temperature", () -> AttachmentType.builder(() -> TemperatureManager.MAX_TEMP)
                    .sync(FLOAT_STREAM_CODEC)
                    .serialize(Codec.FLOAT.fieldOf("inner_temperature")).build());

    public static final Supplier<AttachmentType<Float>> OUTER_TEMPERATURE = ATTACHMENT_TYPES.register(
            "outer_temperature", () -> AttachmentType.builder(() -> TemperatureManager.MAX_TEMP)
                    .sync(FLOAT_STREAM_CODEC)
                    .serialize(Codec.FLOAT.fieldOf("outer_temperature")).build());

    public static final Supplier<AttachmentType<String>> UNLOCKED_ENTRIES = ATTACHMENT_TYPES.register(
            "unlocked_entries", () -> AttachmentType.builder(() -> "")
                    .sync(STRING_STREAM_CODEC)
                    .serialize(Codec.STRING.fieldOf("unlocked_entries")).build());
}
