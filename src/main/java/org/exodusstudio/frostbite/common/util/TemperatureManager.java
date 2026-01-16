package org.exodusstudio.frostbite.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.exodusstudio.frostbite.Frostbite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.exodusstudio.frostbite.common.util.Util.isFrostbite;

public class TemperatureManager {
    public static float MIN_INNER_TEMP;
    public static final float MAX_TEMP = 20f;
    public static final float MIN_TEMP = -60f;
    public static final float INNER_TEMP_DIFFERENCE = 10;
    private static final Map<String, Float> tempsPerBlock = new HashMap<>();
    private static TemperatureManager instance;

    public static TemperatureManager getInstance() {
        if (instance == null) {
            MIN_INNER_TEMP = MIN_TEMP + INNER_TEMP_DIFFERENCE;
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
            instance = new TemperatureManager();
        }
        return instance;
    }

    public void updateEntityTemperatures(List<LivingEntity> entities) {
        for (LivingEntity entity : entities) {
            float outerTempChange = 0;

            float innerTemperature = ((TE) entity).getInnerTemp();
            float outerTemperature = ((TE) entity).getOuterTemp();

            if (entity.tickCount % 20 == 0) {
                if (entity.level() instanceof ServerLevel serverLevel) {
                    affectEntity(serverLevel, entity, innerTemperature);
                }

                if (isFrostbite(entity.level())) {
                    float delta = 1.5f;
                    if (Frostbite.weatherInfo.isWhiteouting) {
                        delta = 3.5f;
                    } else if (Frostbite.weatherInfo.isBlizzarding) {
                        delta = 2.5f;
                    }
                    outerTempChange -= delta;
                } else if (entity.level().dimension().toString().equals("ResourceKey[minecraft:dimension / minecraft:nether]")) {
                    outerTempChange += 5;
                } else {
                    outerTempChange += 2;
                }

                outerTempChange += calculateBlockTemperature(entity);
                if (entity instanceof Player player) {
                    outerTempChange = calculateLiningDamping(player, outerTempChange);
                }
                innerTemperature = Math.clamp(updateInnerTemperature(innerTemperature, outerTemperature), MIN_TEMP, MAX_TEMP);

                if (entity.isInWater()) {
                    outerTempChange -= 1;
                } else if (entity.isOnFire()) {
                    outerTempChange += 3;
                }

                outerTemperature = Math.clamp(outerTempChange + outerTemperature, MIN_TEMP, MAX_TEMP);
                if (entity instanceof TemperatureEntity tempEntity) {
                    outerTemperature += tempEntity.getBaseOuterTempIncrease();
                    tempEntity.tickAttributes();
                }

                innerTemperature = Math.round(innerTemperature * 100f) / 100f;
                outerTemperature = Math.round(outerTemperature * 100f) / 100f;
                ((TE) entity).setInnerTemp(innerTemperature);
                ((TE) entity).setOuterTemp(outerTemperature);
            }
        }
    }

    public float calculateLiningDamping(Player player, float outerTempChange) {
        int i = ((PlayerWrapper) player).frostbite$getLiningLevel();

        if (i <= 12) {
            return Mth.lerp(i / 12f, outerTempChange, 0);
        } else {
            return Mth.lerp((i - 12) / 12f, 0, 4);
        }
    }

    public float calculateBlockTemperature(LivingEntity entity) {
        final float[] temp = {0};

        AABB aabb = Util.squareAABB(entity.position(), 3f);
        for (BlockPos pos : Util.getBlockPositionsInAABB(aabb)) {
            String blockname = entity.level().getBlockState(pos).getBlock().toString().replace("Block{minecraft:", "").replace("}", "");
            if (tempsPerBlock.containsKey(blockname)) {
                temp[0] += (float) (tempsPerBlock.get(blockname) / (0.5 + entity.distanceToSqr(pos.getCenter())));
            }
        }

        return Math.min(temp[0], 5);
    }

    public float updateInnerTemperature(float innerTemperature, float outerTemperature) {
        if (innerTemperature != outerTemperature) {
            float delta = outerTemperature - innerTemperature + INNER_TEMP_DIFFERENCE;
            innerTemperature += delta * 0.2f;
        }
        return innerTemperature;
    }

    public void affectEntity(ServerLevel serverLevel, LivingEntity entity, float innerTemperature) {
        if (innerTemperature < -10 && entity.canFreeze()) {
            entity.hurtServer(serverLevel, entity.damageSources().freeze(), Mth.clamp(-innerTemperature / 10, 1, 3));
        }
    }
}
