package org.exodusstudio.frostbite.common.util.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import org.exodusstudio.frostbite.common.component.MapStringIntData;
import org.exodusstudio.frostbite.common.entity.custom.helper.PseudoEntity;
import org.exodusstudio.frostbite.common.registry.AttachmentRegistry;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.registry.PseudoEntityTypes;
import org.exodusstudio.frostbite.common.weather.WeatherInfo;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "DataFlowIssue"})
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

    public static void setData(MutableDataComponentHolder holder, String key, int value) {
        if (!holder.has(DataComponentTypeRegistry.MAP_STRING_INT)) holder.set(DataComponentTypeRegistry.MAP_STRING_INT, MapStringIntData.EMPTY);
        Map<String, Integer> map = holder.get(DataComponentTypeRegistry.MAP_STRING_INT).map();
        map = safelyAddValueToMap(map, key, value);
        holder.set(DataComponentTypeRegistry.MAP_STRING_INT, new MapStringIntData(map));
    }

    public static int getInt(MutableDataComponentHolder holder, String key) {
        if (!holder.has(DataComponentTypeRegistry.MAP_STRING_INT)) holder.set(DataComponentTypeRegistry.MAP_STRING_INT, MapStringIntData.EMPTY);
        Map<String, Integer> map = holder.get(DataComponentTypeRegistry.MAP_STRING_INT).map();
        if (!map.containsKey(key)) map = safelyAddValueToMap(map, key, 0);
        return map.get(key);
    }

    public static void setData(IAttachmentHolder holder, String key, int value) {
        Map<String, Integer> map = holder.getData(AttachmentRegistry.MAP_STRING_INT);
        map = safelyAddValueToMap(map, key, value);
        holder.setData(AttachmentRegistry.MAP_STRING_INT, map);
    }

    public static int getInt(IAttachmentHolder holder, String key) {
        Map<String, Integer> map = holder.getData(AttachmentRegistry.MAP_STRING_INT);
        if (!map.containsKey(key)) map = safelyAddValueToMap(map, key, 0);
        return map.get(key);
    }

    public static void setData(IAttachmentHolder holder, String key, float value) {
        Map<String, Float> map = holder.getData(AttachmentRegistry.MAP_STRING_FLOAT);
        map = safelyAddValueToMap(map, key, value);
        holder.setData(AttachmentRegistry.MAP_STRING_FLOAT, map);
    }

    public static float getFloat(IAttachmentHolder holder, String key) {
        Map<String, Float> map = holder.getData(AttachmentRegistry.MAP_STRING_FLOAT);
        if (!map.containsKey(key)) map = safelyAddValueToMap(map, key, 0.0f);
        return map.get(key);
    }

    public static void setData(IAttachmentHolder holder, String key, String value) {
        Map<String, String> map = holder.getData(AttachmentRegistry.MAP_STRING_STRING);
        map = safelyAddValueToMap(map, key, value);
        holder.setData(AttachmentRegistry.MAP_STRING_STRING, map);
    }

    public static String getString(IAttachmentHolder holder, String key) {
        Map<String, String> map = holder.getData(AttachmentRegistry.MAP_STRING_STRING);
        if (!map.containsKey(key)) map = safelyAddValueToMap(map, key, "");
        return map.get(key);
    }

    public static void setData(IAttachmentHolder holder, String key, BlockPos value) {
        Map<String, BlockPos> map = holder.getData(AttachmentRegistry.MAP_STRING_BLOCK_POS);
        map = safelyAddValueToMap(map, key, value);
        holder.setData(AttachmentRegistry.MAP_STRING_BLOCK_POS, map);
    }

    public static BlockPos getBlockPos(IAttachmentHolder holder, String key) {
        Map<String, BlockPos> map = holder.getData(AttachmentRegistry.MAP_STRING_BLOCK_POS);
        if (!map.containsKey(key)) map = safelyAddValueToMap(map, key, BlockPos.ZERO);
        return map.get(key);
    }

    public static void setData(IAttachmentHolder holder, String key, boolean value) {
        Map<String, Boolean> map = holder.getData(AttachmentRegistry.MAP_STRING_BOOLEAN);
        map = safelyAddValueToMap(map, key, value);
        holder.setData(AttachmentRegistry.MAP_STRING_BOOLEAN, map);
    }

    public static boolean getBoolean(IAttachmentHolder holder, String key) {
        Map<String, Boolean> map = holder.getData(AttachmentRegistry.MAP_STRING_BOOLEAN);
        if (!map.containsKey(key)) map = safelyAddValueToMap(map, key, true);
        return map.get(key);
    }

    public static void setBlockData(IAttachmentHolder holder, BlockPos pos, String key, float value) {
        Map<BlockPos, Map<String, Float>> map = holder.getData(AttachmentRegistry.MAP_BLOCK_POS_MAP_STRING_FLOAT);
        Map<String, Float> blockMap = getBlockData(holder, pos);
        blockMap = safelyAddValueToMap(blockMap, key, value);
        map = safelyAddValueToMap(map, pos, blockMap);
        holder.setData(AttachmentRegistry.MAP_BLOCK_POS_MAP_STRING_FLOAT, map);
    }

    public static void setBlockData(IAttachmentHolder holder, BlockPos key, Map<String, Float> value) {
        Map<BlockPos, Map<String, Float>> map = holder.getData(AttachmentRegistry.MAP_BLOCK_POS_MAP_STRING_FLOAT);
        map = safelyAddValueToMap(map, key, value);
        holder.setData(AttachmentRegistry.MAP_BLOCK_POS_MAP_STRING_FLOAT, map);
    }

    public static Map<String, Float> getBlockData(IAttachmentHolder holder, BlockPos pos) {
        Map<BlockPos, Map<String, Float>> map = holder.getData(AttachmentRegistry.MAP_BLOCK_POS_MAP_STRING_FLOAT);
        if (!map.containsKey(pos)) map = safelyAddValueToMap(map, pos, new HashMap<>());
        return map.get(pos);
    }

    public static float getBlockData(IAttachmentHolder holder, BlockPos pos, String key) {
        Map<String, Float> map = getBlockData(holder, pos);
        if (!map.containsKey(key)) map = safelyAddValueToMap(map, key, 0f);
        return map.get(key);
    }

    public static void removeBlockData(IAttachmentHolder holder, BlockPos pos) {
        Map<BlockPos, Map<String, Float>> map = holder.getData(AttachmentRegistry.MAP_BLOCK_POS_MAP_STRING_FLOAT);
        map.remove(pos);
        holder.setData(AttachmentRegistry.MAP_BLOCK_POS_MAP_STRING_FLOAT, map);
    }

    public static <E extends Entity> void addBossToAdd(IAttachmentHolder holder, BlockPos key, EntityType<E> value) {
        Map<BlockPos, EntityType<?>> map = getBossesToAdd(holder);
        map = safelyAddValueToMap(map, key, value);
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

    public static void setData(IAttachmentHolder holder, String key, List<Float> data) {
        Map<String, List<Float>> map = new HashMap<>(holder.getData(AttachmentRegistry.MAP_STRING_LIST_FLOAT));
        map = safelyAddValueToMap(map, key, new ArrayList<>(data));
        holder.setData(AttachmentRegistry.MAP_STRING_LIST_FLOAT, map);
    }

    public static void addData(IAttachmentHolder holder, String key, float data) {
        Map<String, List<Float>> map = new HashMap<>(holder.getData(AttachmentRegistry.MAP_STRING_LIST_FLOAT));
        map = safelyAddValueToMap(map, key, new ArrayList<>(map.get(key) == null ? List.of() : map.get(key)));
        map.get(key).add(data);
        holder.setData(AttachmentRegistry.MAP_STRING_LIST_FLOAT, map);
    }

    public static void addDataToAll(IAttachmentHolder holder, String key, float data) {
        Map<String, List<Float>> map = new HashMap<>(holder.getData(AttachmentRegistry.MAP_STRING_LIST_FLOAT));
        map = safelyAddValueToMap(map, key, new ArrayList<>(map.get(key) == null ? List.of() : map.get(key)));
        map.get(key).replaceAll(d -> d + data);
        holder.setData(AttachmentRegistry.MAP_STRING_LIST_FLOAT, map);
    }

    public static List<Float> getList(IAttachmentHolder holder, String key) {
        Map<String, List<Float>> map = new HashMap<>(holder.getData(AttachmentRegistry.MAP_STRING_LIST_FLOAT));
        map = safelyAddValueToMap(map, key, new ArrayList<>(map.get(key) == null ? List.of() : map.get(key)));
        return map.get(key);
    }

    public static void removeData(IAttachmentHolder holder, String key, float data) {
        Map<String, List<Float>> map = new HashMap<>(holder.getData(AttachmentRegistry.MAP_STRING_LIST_FLOAT));
        map = safelyAddValueToMap(map, key, new ArrayList<>(map.get(key) == null ? List.of() : map.get(key)));
        map.get(key).remove(data);
        holder.setData(AttachmentRegistry.MAP_STRING_LIST_FLOAT, map);
    }

    public static void addPseudoEntity(IAttachmentHolder holder, PseudoEntityTypes.PseudoEntityType type, PseudoEntity pseudoEntity) {
        String key = type.id();
        Map<String, List<PseudoEntity>> map = new HashMap<>(holder.getData(AttachmentRegistry.PSEUDO_ENTITIES));
        map = safelyAddValueToMap(map, key, new ArrayList<>(map.get(key) == null ? List.of() : map.get(key)));
        map.get(key).add(pseudoEntity);
        holder.setData(AttachmentRegistry.PSEUDO_ENTITIES, map);
    }

    public static List<PseudoEntity> getPseudoEntities(IAttachmentHolder holder, String key) {
        Map<String, List<PseudoEntity>> map = new HashMap<>(holder.getData(AttachmentRegistry.PSEUDO_ENTITIES));
        if (!map.containsKey(key)) map = safelyAddValueToMap(map, key, new ArrayList<>());
        return map.get(key);
    }

    public static Map<String, List<PseudoEntity>> getAllPseudoEntities(IAttachmentHolder holder) {
        return new HashMap<>(holder.getData(AttachmentRegistry.PSEUDO_ENTITIES));
    }

    public static void removePseudoEntity(IAttachmentHolder holder, String key, PseudoEntity pseudoEntity) {
        Map<String, List<PseudoEntity>> map = new HashMap<>(holder.getData(AttachmentRegistry.PSEUDO_ENTITIES));
        map = safelyAddValueToMap(map, key, new ArrayList<>(map.get(key) == null ? List.of() : map.get(key)));
        map.get(key).remove(pseudoEntity);
        holder.setData(AttachmentRegistry.PSEUDO_ENTITIES, map);
    }

    public static void removePseudoEntities(IAttachmentHolder holder, List<PseudoEntity> pseudoEntities) {
        for (PseudoEntity pseudoEntity : pseudoEntities) {
            for (Map.Entry<String, List<PseudoEntity>> entry : getAllPseudoEntities(holder).entrySet()) {
                if (entry.getValue().contains(pseudoEntity)) {
                    removePseudoEntity(holder, entry.getKey(), pseudoEntity);
                    break;
                }
            }
        }
    }

    public static void addHitEntity(IAttachmentHolder holder, LivingEntity e) {
        String hitEntities = getString(holder, "hit_entities");
        hitEntities += e.getStringUUID() + ";";
        setData(holder, "hit_entities", hitEntities);
    }

    public static boolean hasHitEntity(IAttachmentHolder holder, LivingEntity victim) {
        String hitEntities = getString(holder, "hit_entities");
        if (hitEntities == null || hitEntities.isEmpty()) return false;

        for (String uuid : hitEntities.split(";")) {
            if (uuid.equals(victim.getStringUUID())) {
                return true;
            }
        }
        return false;
    }

    public static void removeHitEntity(IAttachmentHolder holder, Entity entity) {
        String hitEntities = getString(holder, "hit_entities");
        String uuid = entity.getStringUUID();
        hitEntities = hitEntities.replace(uuid + ";", "");
        setData(holder, "hit_entities", hitEntities);
    }

    public static void filterHitEntities(IAttachmentHolder holder, Level level) {
        String hitEntities = getString(holder, "hit_entities");

        if (hitEntities == null || hitEntities.isEmpty()) return;

        for (String uuid : hitEntities.split(";")) {
            if (level.getEntity(UUID.fromString(uuid)) == null) {
                hitEntities = hitEntities.replace(uuid + ";", "");
            }
        }

        setData(holder, "hit_entities", hitEntities);
    }

    public static <K, V> Map<K, V> safelyAddValueToMap(Map<K, V> map, K key, V value) {
        try {
            map.put(key, value);
        } catch (UnsupportedOperationException e) {
            map = new HashMap<>(map);
            map.put(key, value);
        }
        return map;
    }
}
