package org.exodusstudio.frostbite.common.registry;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import org.exodusstudio.frostbite.common.attachment.DataAttachment;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.contracts.LivingContractInfo;
import org.exodusstudio.frostbite.common.entity.custom.helper.PseudoEntity;
import org.exodusstudio.frostbite.common.weather.WeatherInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.exodusstudio.frostbite.common.util.helpers.CodecHelper.*;

public class AttachmentRegistry {
    public static final DataAttachment.Register ATTACHMENT_TYPES =
            new DataAttachment.Register(Frostbite.MOD_ID);

    public static final DataAttachment<Map<String, Integer>> MAP_STRING_INT = ATTACHMENT_TYPES.register(
            "map_string_int", () -> DataAttachment.builder(() -> (Map<String, Integer>) new HashMap<String, Integer>())
                    .sync(MAP_STRING_INT_STREAM_CODEC)
                    .serialize(Codec.unboundedMap(Codec.STRING, Codec.INT).fieldOf("map_string_int")).build());

    public static final DataAttachment<Map<String, Float>> MAP_STRING_FLOAT = ATTACHMENT_TYPES.register(
            "map_string_float", () -> DataAttachment.builder(() -> (Map<String, Float>) new HashMap<String, Float>())
                    .sync(MAP_STRING_FLOAT_STREAM_CODEC)
                    .serialize(Codec.unboundedMap(Codec.STRING, Codec.FLOAT).fieldOf("map_string_float")).build());

    public static final DataAttachment<Map<String, List<Float>>> MAP_STRING_LIST_FLOAT = ATTACHMENT_TYPES.register(
            "map_string_list_float", () -> DataAttachment.builder(() -> (Map<String, List<Float>>) new HashMap<String, List<Float>>())
                    .sync(MAP_STRING_LIST_FLOAT_STREAM_CODEC)
                    .serialize(Codec.unboundedMap(Codec.STRING, Codec.FLOAT.listOf()).fieldOf("map_string_list_float")).build());

    public static final DataAttachment<Map<String, String>> MAP_STRING_STRING = ATTACHMENT_TYPES.register(
            "map_string_string", () -> DataAttachment.builder(() -> (Map<String, String>) new HashMap<String, String>())
                    .sync(MAP_STRING_STRING_STREAM_CODEC)
                    .serialize(Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("map_string_string")).build());

    public static final DataAttachment<Map<String, BlockPos>> MAP_STRING_BLOCK_POS = ATTACHMENT_TYPES.register(
            "map_string_block_pos", () -> DataAttachment.builder(() -> (Map<String, BlockPos>) new HashMap<String, BlockPos>())
                    .sync(MAP_STRING_BLOCK_POS_STREAM_CODEC)
                    .serialize(Codec.unboundedMap(Codec.STRING, BLOCK_POS_CODEC).fieldOf("map_string_block_pos"))
                    .build());

    public static final DataAttachment<Map<String, Boolean>> MAP_STRING_BOOLEAN = ATTACHMENT_TYPES.register(
            "map_string_boolean", () -> DataAttachment.builder(() -> (Map<String, Boolean>) new HashMap<String, Boolean>())
                    .sync(MAP_STRING_BOOLEAN_STREAM_CODEC)
                    .serialize(Codec.unboundedMap(Codec.STRING, Codec.BOOL).fieldOf("map_string_boolean"))
                    .build());

    public static final DataAttachment<Map<BlockPos, Map<String, Float>>> MAP_BLOCK_POS_MAP_STRING_FLOAT = ATTACHMENT_TYPES.register(
            "map_block_pos_map_string_string", () -> DataAttachment.builder(() -> (Map<BlockPos, Map<String, Float>>) new HashMap<BlockPos, Map<String, Float>>())
                    .sync(MAP_BLOCK_POS_MAP_STRING_FLOAT_STREAM_CODEC)
                    .serialize(Codec.unboundedMap(BLOCK_POS_CODEC, Codec.unboundedMap(Codec.STRING, Codec.FLOAT)).fieldOf("map_block_pos_map_string_string"))
                    .build());

    public static final DataAttachment<Map<String, List<PseudoEntity>>> PSEUDO_ENTITIES = ATTACHMENT_TYPES.register(
            "pseudo_entities", () -> DataAttachment.builder(() -> (Map<String, List<PseudoEntity>>) new HashMap<String, List<PseudoEntity>>())
                    .sync(MAP_STRING_LIST_PSEUDO_ENTITY_STREAM_CODEC)
                    .serialize(Codec.unboundedMap(Codec.STRING, PseudoEntity.CODEC.listOf()).fieldOf("pseudo_entities"))
                    .build());

    public static final DataAttachment<Map<BlockPos, String>> ADDED_BOSSES = ATTACHMENT_TYPES.register(
            "added_bosses", () -> DataAttachment.builder(() -> (Map<BlockPos, String>) new HashMap<BlockPos, String>())
                    .sync(MAP_BLOCK_POS_STRING_STREAM_CODEC)
                    .serialize(Codec.unboundedMap(BLOCK_POS_CODEC, Codec.STRING).fieldOf("added_bosses"))
                    .build());
    public static final DataAttachment<Map<BlockPos, String>> BOSSES_TO_ADD = ATTACHMENT_TYPES.register(
            "bosses_to_add", () -> DataAttachment.builder(() -> (Map<BlockPos, String>) new HashMap<BlockPos, String>())
                    .sync(MAP_BLOCK_POS_STRING_STREAM_CODEC)
                    .serialize(Codec.unboundedMap(BLOCK_POS_CODEC, Codec.STRING).fieldOf("bosses_to_add"))
                    .build());

    public static final DataAttachment<LivingContractInfo> LIVING_CONTRACT_INFO = ATTACHMENT_TYPES.register(
            "living_contract_info", () -> DataAttachment.builder(() -> LivingContractInfo.EMPTY)
                    .sync(StreamCodec.of(LivingContractInfo::toBuffer, LivingContractInfo::fromBuffer))
                    .serialize(LivingContractInfo.CODEC.fieldOf("living_contract_info"))
                    .copyOnDeath().build());

    public static final DataAttachment<WeatherInfo> WEATHER_INFO = ATTACHMENT_TYPES.register(
            "weather_info", () -> DataAttachment.builder(WeatherInfo::new)
                    .sync(WeatherInfo.STREAM_CODEC)
                    .serialize(WeatherInfo.CODEC.fieldOf("weather_info"))
                    .build());

    public static final DataAttachment<Map<UUID, List<Pair<String, Long>>>> CURRENT_RENDERING_ATTACKS = ATTACHMENT_TYPES.register(
            "current_rendering_attacks",
            () -> DataAttachment.builder(() -> (Map<UUID, List<Pair<String, Long>>>) new HashMap<UUID, List<Pair<String, Long>>>())
                    .sync(MAP_UUID_LIST_STRING_LONG_PAIR_STREAM_CODEC)
                    .serialize(Codec.unboundedMap(UUID_CODEC, LONG_STRING_PAIR_CODEC.listOf()).fieldOf("current_rendering_attacks")).build());

    public static final DataAttachment<Boolean> SHOW_LINING = ATTACHMENT_TYPES.register(
            "show_lining", () -> DataAttachment.builder(() -> false)
                    .serialize(Codec.BOOL.fieldOf("show_lining")).build());
}
