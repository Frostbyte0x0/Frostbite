package org.exodusstudio.frostbite.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.exodusstudio.frostbite.common.registry.AttachmentRegistry;
import org.exodusstudio.frostbite.common.weather.WeatherInfo;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class DataHelper {
    public static void setWeatherInfo(Level level, WeatherInfo weatherInfo) {
        level.setData(AttachmentRegistry.WEATHER_INFO, weatherInfo);
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
