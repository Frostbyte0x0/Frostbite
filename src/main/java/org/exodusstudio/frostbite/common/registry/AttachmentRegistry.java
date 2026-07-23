package org.exodusstudio.frostbite.common.registry;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.contracts.PlayerContractInfo;
import org.exodusstudio.frostbite.common.entity.custom.helper.PseudoEntity;
import org.exodusstudio.frostbite.common.weather.WeatherInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import static org.exodusstudio.frostbite.common.util.CodecHelper.*;

public class AttachmentRegistry {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Frostbite.MOD_ID);

    public static final Supplier<AttachmentType<Map<String, Integer>>> MAP_STRING_INT = ATTACHMENT_TYPES.register(
            "map_string_int", () -> AttachmentType.builder(() -> (Map<String, Integer>) new HashMap<String, Integer>())
                    .sync(MAP_STRING_INT_STREAM_CODEC)
                    .serialize(Codec.unboundedMap(Codec.STRING, Codec.INT).fieldOf("map_string_int")).build());

    public static final Supplier<AttachmentType<Map<String, Float>>> MAP_STRING_FLOAT = ATTACHMENT_TYPES.register(
            "map_string_float", () -> AttachmentType.builder(() -> (Map<String, Float>) new HashMap<String, Float>())
                    .sync(MAP_STRING_FLOAT_STREAM_CODEC)
                    .serialize(Codec.unboundedMap(Codec.STRING, Codec.FLOAT).fieldOf("map_string_float")).build());

    public static final Supplier<AttachmentType<Map<String, String>>> MAP_STRING_STRING = ATTACHMENT_TYPES.register(
            "map_string_string", () -> AttachmentType.builder(() -> (Map<String, String>) new HashMap<String, String>())
                    .sync(MAP_STRING_STRING_STREAM_CODEC)
                    .serialize(Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("map_string_string")).build());

    public static final Supplier<AttachmentType<Map<String, BlockPos>>> MAP_STRING_BLOCK_POS = ATTACHMENT_TYPES.register(
            "map_string_block_pos", () -> AttachmentType.builder(() -> (Map<String, BlockPos>) new HashMap<String, BlockPos>())
                    .sync(MAP_STRING_BLOCK_POS_STREAM_CODEC)
                    .serialize(Codec.unboundedMap(Codec.STRING, BlockPos.CODEC).fieldOf("map_string_block_pos"))
                    .build());

    public static final Supplier<AttachmentType<Map<String, Boolean>>> MAP_STRING_BOOLEAN = ATTACHMENT_TYPES.register(
            "map_string_boolean", () -> AttachmentType.builder(() -> (Map<String, Boolean>) new HashMap<String, Boolean>())
                    .sync(MAP_STRING_BOOLEAN_STREAM_CODEC)
                    .serialize(Codec.unboundedMap(Codec.STRING, Codec.BOOL).fieldOf("map_string_boolean"))
                    .build());

    public static final Supplier<AttachmentType<Map<String, List<PseudoEntity>>>> PSEUDO_ENTITIES = ATTACHMENT_TYPES.register(
            "pseudo_entities", () -> AttachmentType.builder(() -> (Map<String, List<PseudoEntity>>) new HashMap<String, List<PseudoEntity>>())
                    .sync(MAP_STRING_LIST_PSEUDO_ENTITY_STREAM_CODEC)
                    .serialize(Codec.unboundedMap(Codec.STRING, PseudoEntity.CODEC.listOf()).fieldOf("pseudo_entities"))
                    .build());

    public static final Supplier<AttachmentType<Map<BlockPos, String>>> ADDED_BOSSES = ATTACHMENT_TYPES.register(
            "added_bosses", () -> AttachmentType.builder(() -> (Map<BlockPos, String>) new HashMap<BlockPos, String>())
                    .sync(MAP_BLOCK_POS_STRING_STREAM_CODEC)
                    .serialize(Codec.unboundedMap(BlockPos.CODEC, Codec.STRING).fieldOf("added_bosses"))
                    .build());
    public static final Supplier<AttachmentType<Map<BlockPos, String>>> BOSSES_TO_ADD = ATTACHMENT_TYPES.register(
            "bosses_to_add", () -> AttachmentType.builder(() -> (Map<BlockPos, String>) new HashMap<BlockPos, String>())
                    .sync(MAP_BLOCK_POS_STRING_STREAM_CODEC)
                    .serialize(Codec.unboundedMap(BlockPos.CODEC, Codec.STRING).fieldOf("bosses_to_add"))
                    .build());

    public static final Supplier<AttachmentType<PlayerContractInfo>> PLAYER_CONTRACT_INFO = ATTACHMENT_TYPES.register(
            "player_contract_info", () -> AttachmentType.builder(() -> PlayerContractInfo.EMPTY)
                    .sync(StreamCodec.of(PlayerContractInfo::toBuffer, PlayerContractInfo::fromBuffer))
                    .serialize(PlayerContractInfo.CODEC.fieldOf("player_contract_info"))
                    .copyOnDeath().build());

    public static final Supplier<AttachmentType<WeatherInfo>> WEATHER_INFO = ATTACHMENT_TYPES.register(
            "weather_info", () -> AttachmentType.builder(WeatherInfo::new)
                    .sync(WeatherInfo.STREAM_CODEC)
                    .serialize(WeatherInfo.CODEC.fieldOf("weather_info"))
                    .build());

    public static final Supplier<AttachmentType<Map<UUID, List<Pair<String, Long>>>>> CURRENT_RENDERING_ATTACKS = ATTACHMENT_TYPES.register(
            "current_rendering_attacks",
            () -> AttachmentType.builder(() -> (Map<UUID, List<Pair<String, Long>>>) new HashMap<UUID, List<Pair<String, Long>>>())
                    .sync(MAP_UUID_LIST_STRING_LONG_PAIR_STREAM_CODEC)
                    .serialize(Codec.unboundedMap(UUID_CODEC, LONG_STRING_PAIR_CODEC.listOf()).fieldOf("current_rendering_attacks")).build());

    public static final Supplier<AttachmentType<Boolean>> SHOW_LINING = ATTACHMENT_TYPES.register(
            "show_lining", () -> AttachmentType.builder(() -> false)
                    .serialize(Codec.BOOL.fieldOf("show_lining")).build());
}
