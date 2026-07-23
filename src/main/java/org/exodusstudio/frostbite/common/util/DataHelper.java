package org.exodusstudio.frostbite.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.exodusstudio.frostbite.common.registry.AttachmentRegistry;
import org.exodusstudio.frostbite.common.weather.WeatherInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class DataHelper {
    public static void setWeatherInfo(Level level, WeatherInfo weatherInfo) {
        level.setData(AttachmentRegistry.WEATHER_INFO, weatherInfo);
    }

    public static WeatherInfo getWeatherInfo(Level level) {
        if (level.hasData(AttachmentRegistry.WEATHER_INFO)) {
            return level.getData(AttachmentRegistry.WEATHER_INFO);
        } else {
            WeatherInfo weatherInfo = new WeatherInfo();
            level.setData(AttachmentRegistry.WEATHER_INFO, weatherInfo);
            return weatherInfo;
        }
    }

    public static void setData(IAttachmentHolder holder, String key, int value) {
        Map<String, Integer> map = holder.getData(AttachmentRegistry.MAP_STRING_INT);
        map = addValueToMap(map, key, value);
        holder.setData(AttachmentRegistry.MAP_STRING_INT, map);
    }

    public static int getInt(IAttachmentHolder holder, String key) {
        Map<String, Integer> map = holder.getData(AttachmentRegistry.MAP_STRING_INT);
        if (!map.containsKey(key)) map = addValueToMap(map, key, 0);
        return map.get(key);
    }

    public static void setData(IAttachmentHolder holder, String key, float value) {
        Map<String, Float> map = holder.getData(AttachmentRegistry.MAP_STRING_FLOAT);
        map = addValueToMap(map, key, value);
        holder.setData(AttachmentRegistry.MAP_STRING_FLOAT, map);
    }

    public static float getFloat(IAttachmentHolder holder, String key) {
        Map<String, Float> map = holder.getData(AttachmentRegistry.MAP_STRING_FLOAT);
        if (!map.containsKey(key)) map = addValueToMap(map, key, 0.0f);
        return map.get(key);
    }

    public static void setData(IAttachmentHolder holder, String key, String value) {
        Map<String, String> map = holder.getData(AttachmentRegistry.MAP_STRING_STRING);
        map = addValueToMap(map, key, value);
        holder.setData(AttachmentRegistry.MAP_STRING_STRING, map);
    }

    public static String getString(IAttachmentHolder holder, String key) {
        Map<String, String> map = holder.getData(AttachmentRegistry.MAP_STRING_STRING);
        if (!map.containsKey(key)) map = addValueToMap(map, key, "");
        return map.get(key);
    }

    public static void setData(IAttachmentHolder holder, String key, BlockPos value) {
        Map<String, BlockPos> map = holder.getData(AttachmentRegistry.MAP_STRING_BLOCK_POS);
        map = addValueToMap(map, key, value);
        holder.setData(AttachmentRegistry.MAP_STRING_BLOCK_POS, map);
    }

    public static BlockPos getBlockPos(IAttachmentHolder holder, String key) {
        Map<String, BlockPos> map = holder.getData(AttachmentRegistry.MAP_STRING_BLOCK_POS);
        if (!map.containsKey(key)) map = addValueToMap(map, key, BlockPos.ZERO);
        return map.get(key);
    }

    public static void setData(IAttachmentHolder holder, String key, boolean value) {
        Map<String, Boolean> map = holder.getData(AttachmentRegistry.MAP_STRING_BOOLEAN);
        map = addValueToMap(map, key, value);
        holder.setData(AttachmentRegistry.MAP_STRING_BOOLEAN, map);
    }

    public static boolean getBoolean(IAttachmentHolder holder, String key) {
        Map<String, Boolean> map = holder.getData(AttachmentRegistry.MAP_STRING_BOOLEAN);
        if (!map.containsKey(key)) map = addValueToMap(map, key, true);
        return map.get(key);
    }

    public static <E extends Entity> void addBossToAdd(IAttachmentHolder holder, BlockPos key, EntityType<E> value) {
        Map<BlockPos, EntityType<?>> map = getBossesToAdd(holder);
        map = addValueToMap(map, key, value);
        Map<BlockPos, String> stringMap = map.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> BuiltInRegistries.ENTITY_TYPE.getKey(e.getValue()).toString()));
        holder.setData(AttachmentRegistry.BOSSES_TO_ADD, stringMap);
    }

    public static Map<BlockPos, EntityType<?>> getBossesToAdd(IAttachmentHolder holder) {
        return new HashMap<>(holder.getData(AttachmentRegistry.BOSSES_TO_ADD)
                .entrySet().stream()
                .map(e -> Map.entry(e.getKey(), BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.parse(e.getValue()))))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
                ));
    }

    public static Map<BlockPos, EntityType<?>> getAddedBosses(IAttachmentHolder holder) {
        return new HashMap<>(holder.getData(AttachmentRegistry.ADDED_BOSSES)
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.parse(e.getValue())))
        ));
    }

    public static void addAddedBosses(IAttachmentHolder holder, Map<BlockPos, EntityType<?>> map) {
        Map<BlockPos, EntityType<?>> existingMap = getAddedBosses(holder);
        existingMap.putAll(map);
        Map<BlockPos, String> stringMap = map.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> BuiltInRegistries.ENTITY_TYPE.getKey(e.getValue()).toString()));
        holder.setData(AttachmentRegistry.ADDED_BOSSES, stringMap);
    }

    public static void clearBossesToAdd(IAttachmentHolder holder) {
        holder.setData(AttachmentRegistry.BOSSES_TO_ADD, new HashMap<>());
    }

    public static <K, V> Map<K, V> addValueToMap(Map<K, V> map, K key, V value) {
        try {
            map.put(key, value);
        } catch (UnsupportedOperationException e) {
            map = new HashMap<>(map);
            map.put(key, value);
        }
        return map;
    }
}
