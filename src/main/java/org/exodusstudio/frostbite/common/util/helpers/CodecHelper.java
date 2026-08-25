package org.exodusstudio.frostbite.common.util.helpers;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.Utf8String;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.AABB;
import org.exodusstudio.frostbite.common.entity.custom.helper.PseudoEntity;

import java.util.*;

public class CodecHelper {
    public static final Codec<AABB> AABB_CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<AABB, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getStringValue(input).flatMap(s -> {
                try {
                    String [] parts = s.split(",");
                    if (parts.length != 6) {
                        return DataResult.error(() -> "Invalid AABB string: " + s);
                    }
                    double minX = Double.parseDouble(parts[0]);
                    double minY = Double.parseDouble(parts[1]);
                    double minZ = Double.parseDouble(parts[2]);
                    double maxX = Double.parseDouble(parts[3]);
                    double maxY = Double.parseDouble(parts[4]);
                    double maxZ = Double.parseDouble(parts[5]);
                    return DataResult.success(Pair.of(new AABB(minX, minY, minZ, maxX, maxY, maxZ), input));
                } catch (IllegalArgumentException e) {
                    return DataResult.error(() -> "Invalid AABB string: " + s);
                }
            });
        }

        @Override
        public <T> DataResult<T> encode(AABB aabb, DynamicOps<T> ops, T prefix) {
            String result = aabb.minX + "," + aabb.minY + "," + aabb.minZ + "," + aabb.maxX + "," + aabb.maxY + "," + aabb.maxZ;
            return ops.mergeToPrimitive(prefix, ops.createString(result));
        }
    };

    public static final Codec<UUID> UUID_CODEC = new Codec<>() {
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

    public static final Codec<Pair<String, Long>> LONG_STRING_PAIR_CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<Pair<String, Long>, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getStringValue(input).flatMap(s -> {
                try {
                    String[] parts = s.split(":");
                    if (parts.length != 2) {
                        return DataResult.error(() -> "Invalid long-string pair: " + s);
                    }
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

    public static Codec<EntityType<?>> ENTITY_TYPE_CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<EntityType<?>, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getStringValue(input).flatMap(s -> {
                try {
                    String[] parts = s.split(":");
                    EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(Identifier.fromNamespaceAndPath(parts[0], parts[1])).orElseThrow().value();
                    return DataResult.success(Pair.of(type, input));
                } catch (IllegalArgumentException e) {
                    return DataResult.error(() -> "Invalid UUID string: " + s);
                }
            });
        }

        @Override
        public <T> DataResult<T> encode(EntityType<?> type, DynamicOps<T> ops, T prefix) {
            Identifier id = BuiltInRegistries.ENTITY_TYPE.getKey(type);
            return ops.mergeToPrimitive(prefix, ops.createString(id.getNamespace() + ":" + id.getPath()));
        }
    };



    public static final StreamCodec<RegistryFriendlyByteBuf, Map<BlockPos, EntityType<?>>> MAP_BLOCK_POS_ENTITY_TYPE_STREAM_CODEC = StreamCodec.of(
            (b, m) -> {
                b.writeInt(m.size());
                m.forEach((k, v) -> {
                    b.writeInt(k.getX());
                    b.writeInt(k.getY());
                    b.writeInt(k.getZ());
                    EntityType.STREAM_CODEC.encode(b, v);
                });
            },
            b -> {
                Map<BlockPos, EntityType<?>> m = new HashMap<>();
                int size = b.readInt();
                for (int i = 0; i < size; i++) {
                    m.put(new BlockPos(b.readInt(), b.readInt(), b.readInt()), EntityType.STREAM_CODEC.decode(b));
                }
                return m;
            });
    public static final StreamCodec<RegistryFriendlyByteBuf, Map<BlockPos, String>> MAP_BLOCK_POS_STRING_STREAM_CODEC = StreamCodec.of(
            (b, m) -> {
                b.writeInt(m.size());
                m.forEach((k, v) -> {
                    b.writeInt(k.getX());
                    b.writeInt(k.getY());
                    b.writeInt(k.getZ());
                    b.writeUtf(v);
                });
            },
            b -> {
                Map<BlockPos, String> m = new HashMap<>();
                int size = b.readInt();
                for (int i = 0; i < size; i++) {
                    m.put(new BlockPos(b.readInt(), b.readInt(), b.readInt()), b.readUtf());
                }
                return m;
            });
    public static final StreamCodec<RegistryFriendlyByteBuf, Map<String, Boolean>> MAP_STRING_BOOLEAN_STREAM_CODEC = StreamCodec.of(
            (b, m) -> {
                b.writeInt(m.size());
                m.forEach((k, v) -> {
                    b.writeUtf(k);
                    b.writeBoolean(v);
                });
            },
            b -> {
                Map<String, Boolean> m = new HashMap<>();
                int size = b.readInt();
                for (int i = 0; i < size; i++) {
                    m.put(b.readUtf(), b.readBoolean());
                }
                return m;
            });
    public static final StreamCodec<RegistryFriendlyByteBuf, Map<BlockPos, Map<String, Float>>> MAP_BLOCK_POS_MAP_STRING_FLOAT_STREAM_CODEC = StreamCodec.of(
            (b, m) -> {
                b.writeInt(m.size());
                m.forEach((k, v) -> {
                    b.writeInt(v.size());
                    v.forEach((sk, sv) -> {
                        b.writeUtf(sk);
                        b.writeFloat(sv);
                    });

                    b.writeInt(k.getX());
                    b.writeInt(k.getY());
                    b.writeInt(k.getZ());
                });
            },
            b -> {
                Map<BlockPos, Map<String, Float>> m = new HashMap<>();
                int size = b.readInt();
                for (int i = 0; i < size; i++) {
                    Map<String, Float> v = new HashMap<>();
                    int size1 = b.readInt();
                    for (int j = 0; j < size1; j++) {
                        v.put(b.readUtf(), b.readFloat());
                    }

                    m.put(new BlockPos(b.readInt(), b.readInt(), b.readInt()), v);
                }
                return m;
            });
    public static final StreamCodec<RegistryFriendlyByteBuf, Map<String, BlockPos>> MAP_STRING_BLOCK_POS_STREAM_CODEC = StreamCodec.of(
            (b, m) -> {
                b.writeInt(m.size());
                m.forEach((k, v) -> {
                    b.writeUtf(k);
                    b.writeInt(v.getX());
                    b.writeInt(v.getY());
                    b.writeInt(v.getZ());
                });
            },
            b -> {
                Map<String, BlockPos> m = new HashMap<>();
                int size = b.readInt();
                for (int i = 0; i < size; i++) {
                    m.put(b.readUtf(), new BlockPos(b.readInt(), b.readInt(), b.readInt()));
                }
                return m;
            });
    public static final StreamCodec<ByteBuf, Map<String, Integer>> MAP_STRING_INT_STREAM_CODEC = StreamCodec.of(
            (b, m) -> {
                b.writeInt(m.size());
                m.forEach((k, v) -> {
                    Utf8String.write(b, k, 32767);
                    b.writeInt(v);
                });
            },
            b -> {
                Map<String, Integer> m = new HashMap<>();
                int size = b.readInt();
                for (int i = 0; i < size; i++) {
                    m.put(Utf8String.read(b, 32767), b.readInt());
                }
                return m;
            });
    public static final StreamCodec<RegistryFriendlyByteBuf, Map<String, String>> MAP_STRING_STRING_STREAM_CODEC = StreamCodec.of(
            (b, m) -> {
                b.writeInt(m.size());
                m.forEach((k, v) -> {
                    b.writeUtf(k);
                    b.writeUtf(v);
                });
            },
            b -> {
                Map<String, String> m = new HashMap<>();
                int size = b.readInt();
                for (int i = 0; i < size; i++) {
                    m.put(b.readUtf(), b.readUtf());
                }
                return m;
            });
    public static final StreamCodec<RegistryFriendlyByteBuf, Map<String, Float>> MAP_STRING_FLOAT_STREAM_CODEC = StreamCodec.of(
            (b, m) -> {
                b.writeInt(m.size());
                m.forEach((k, v) -> {
                    b.writeUtf(k);
                    b.writeFloat(v);
                });
            },
            b -> {
                Map<String, Float> m = new HashMap<>();
                int size = b.readInt();
                for (int i = 0; i < size; i++) {
                    m.put(b.readUtf(), b.readFloat());
                }
                return m;
            });
    public static final StreamCodec<RegistryFriendlyByteBuf, Map<String, List<Float>>> MAP_STRING_LIST_FLOAT_STREAM_CODEC = StreamCodec.of(
            (b, m) -> {
                b.writeInt(m.size());
                m.forEach((k, v) -> {
                    b.writeUtf(k);
                    b.writeInt(v.size());
                    v.forEach(b::writeFloat);
                });
            },
            b -> {
                Map<String, List<Float>> m = new HashMap<>();
                int size = b.readInt();
                for (int i = 0; i < size; i++) {
                    String key = b.readUtf();
                    int listSize = b.readInt();
                    List<Float> list = new ArrayList<>();
                    for (int j = 0; j < listSize; j++) {
                        list.add(b.readFloat());
                    }
                    m.put(key, list);
                }
                return m;
            });
    public static final StreamCodec<RegistryFriendlyByteBuf, Map<String, List<PseudoEntity>>> MAP_STRING_LIST_PSEUDO_ENTITY_STREAM_CODEC = StreamCodec.of(
            (b, m) -> {
                b.writeInt(m.size());
                m.forEach((k, v) -> {
                    b.writeUtf(k);
                    b.writeInt(v.size());
                    v.forEach(pseudoEntity -> PseudoEntity.toBuffer(b, pseudoEntity));
                });
            },
            b -> {
                Map<String, List<PseudoEntity>> m = new HashMap<>();
                int size = b.readInt();
                for (int i = 0; i < size; i++) {
                    String key = b.readUtf();
                    int listSize = b.readInt();
                    List<PseudoEntity> list = new ArrayList<>();
                    for (int j = 0; j < listSize; j++) {
                        list.add(PseudoEntity.fromBuffer(b));
                    }
                    m.put(key, list);
                }
                return m;
            });
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
}
