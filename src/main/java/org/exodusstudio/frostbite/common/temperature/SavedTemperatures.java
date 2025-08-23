package org.exodusstudio.frostbite.common.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import org.exodusstudio.frostbite.common.util.MathsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavedTemperatures {
    private final HashMap<String, List<Float>> entityTemperatures = new HashMap<>();
    private static final Map<String, Float> tempsPerBlock = new HashMap<>();

    public static SavedTemperatures init() {
        tempsPerBlock.put("lava", 2f);
        tempsPerBlock.put("torch", 0.5f);
        tempsPerBlock.put("wall_torch", 1f);
        tempsPerBlock.put("lantern", 1f);
        tempsPerBlock.put("soul_lantern", 1f);
        tempsPerBlock.put("campfire", 5f);
        tempsPerBlock.put("soul_campfire", 5f);
        tempsPerBlock.put("fire", 2f);
        tempsPerBlock.put("furnace", 1.5f);
        tempsPerBlock.put("blast_furnace", 1.5f);
        tempsPerBlock.put("smoker", 1.5f);
        return new SavedTemperatures();
    }

    public void updateEntityTemperatures(List<LivingEntity> entities) {
        for (LivingEntity entity : entities) {
            float outerTempChange = 0;
            String entityUUID = entity.getStringUUID();
            if (!entityTemperatures.containsKey(entityUUID)) {
                entityTemperatures.put(entityUUID, List.of(20f, 20f));
            }

            List<Float> temperatures = entityTemperatures.get(entityUUID);
            float innerTemperature = temperatures.get(0);
            float outerTemperature = temperatures.get(1);

            if (entity.isDeadOrDying() || entity.isRemoved()) {
                entityTemperatures.remove(entityUUID);
                continue;
            }

            if (entity.tickCount % 20 == 0) {
                if (entity.level() instanceof ServerLevel serverLevel) {
                    affectEntity(serverLevel, entity, innerTemperature);
                }

                if (entity.level().dimension().toString().equals("ResourceKey[minecraft:dimension / frostbite:frostbite]")) {
                    outerTempChange -= 1.5f;
                } else if (entity.level().dimension().toString().equals("ResourceKey[minecraft:dimension / minecraft:nether]")) {
                    outerTempChange += 5;
                } else {
                    outerTempChange += 2;
                }

                outerTempChange += calculateBlockTemperature(entity);
                innerTemperature = Math.clamp(updateInnerTemperature(innerTemperature, outerTemperature), -60, 20);

                if (entity.isInWater()) {
                    outerTempChange -= 1;
                } else if (entity.isOnFire()) {
                    outerTempChange += 3;
                }

                outerTemperature = Math.clamp(outerTempChange + outerTemperature, -60, 20);
                entityTemperatures.put(entityUUID, List.of(innerTemperature, outerTemperature));
            }
        }
    }

    public float calculateBlockTemperature(LivingEntity entity) {
        final float[] temp = {0};

        AABB aabb = MathsUtil.squareAABB(entity.position(), 3f);
        // List<BlockPos> l = MathsUtil.getBlockPositionsInAABB(aabb);
        for (BlockPos pos : MathsUtil.getBlockPositionsInAABB(aabb)) {
            String blockname = entity.level().getBlockState(pos).getBlock().toString().replace("Block{minecraft:", "").replace("}", "");
            if (tempsPerBlock.containsKey(blockname)) {
                temp[0] += (float) (tempsPerBlock.get(blockname) / (0.5 + entity.distanceToSqr(pos.getCenter())));
            }
        }

        //return Math.min(Math.round(temp[0] * 2) / 2f, 5);
        return Math.min(temp[0], 5);
    }

    public float updateInnerTemperature(float innerTemperature, float outerTemperature) {
        if (innerTemperature != outerTemperature) {
            float delta = outerTemperature - innerTemperature + 20;
            //innerTemperature += (float) Math.round(delta / 10) / 2;
            innerTemperature += delta;
        }
        return innerTemperature;
    }

    public void affectEntity(ServerLevel serverLevel, LivingEntity entity, float innerTemperature) {
        if (innerTemperature < -10 && entity.canFreeze()) {
            entity.hurtServer(serverLevel, entity.damageSources().freeze(), Mth.clamp(-innerTemperature / 10, 1, 3));
        }
    }

    public void setTemperatures(LivingEntity entity, List<Float> temps) {
        String entityUUID = entity.getStringUUID();
        entityTemperatures.put(entityUUID, temps);
    }

    public void setTemperatures(String entityUUID, List<Float> temps) {
        entityTemperatures.put(entityUUID, temps);
    }

    public float getTemperature(LivingEntity entity, boolean inner) {
        String entityUUID = entity.getStringUUID();
        List<Float> list;
        if (entityTemperatures.containsKey(entityUUID) && (list = entityTemperatures.get(entityUUID)) != null) {
            return list.get(inner ? 0 : 1);
        }
        return 20f;
    }

    public float getTemperature(String uuid, boolean inner) {
        if (entityTemperatures.containsKey(uuid)) {
            return entityTemperatures.get(uuid).get(inner ? 0 : 1);
        }
        return 20f;
    }

    public void decreaseTemperature(LivingEntity entity, float temperature, boolean inner) {
        String entityUUID = entity.getStringUUID();
        if (!entityTemperatures.containsKey(entityUUID)) {
            entityTemperatures.put(entityUUID, List.of(20f, 20f));
        }

        List<Float> temperatures = new ArrayList<>(entityTemperatures.get(entityUUID));
        byte index = (byte) (inner ? 0 : 1);
        temperatures.set(index, Math.max(-temperature + temperatures.get(index), 60));

        entityTemperatures.put(entityUUID, temperatures);
    }

    public void increaseTemperature(LivingEntity entity, float temperature, boolean inner) {
        String entityUUID = entity.getStringUUID();
        if (!entityTemperatures.containsKey(entityUUID)) {
            entityTemperatures.put(entityUUID, List.of(20f, 20f));
        }

        List<Float> temperatures = new ArrayList<>(entityTemperatures.get(entityUUID));
        byte index = (byte) (inner ? 0 : 1);
        temperatures.set(index, Math.min(temperature + temperatures.get(index), 20));

        entityTemperatures.put(entityUUID, temperatures);
    }

    public void clear() {
        entityTemperatures.clear();
    }
}
