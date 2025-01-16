package com.frostbyte.frostbite.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class StunningBellItem extends Item {
    private final int range = 10;
    private boolean used = false;
    private List<Entity> affected_entities = new ArrayList<>();

    public StunningBellItem(Properties properties) {
        super(properties);
    }

    private int x = 10;

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemUtils.startUsingInstantly(level, player, hand);
        if (used) {
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class,
                    new AABB(new Vec3(player.getBlockX() - range, player.getBlockY() - range, player.getBlockZ() - range),
                             new Vec3(player.getBlockX() + range, player.getBlockY() + range, player.getBlockZ() + range)));

            if (!entities.isEmpty()) {
                HashMap<Integer, List<LivingEntity>> entities_per_distance = new HashMap<>();

                for (LivingEntity entity : entities) {
                    Integer rounded_dist = (int) player.distanceTo(entity);

                    if (entities_per_distance.containsKey(rounded_dist)) {
                        entities_per_distance.get(rounded_dist).add(entity);
                    } else {
                        ArrayList<LivingEntity> new_list = new ArrayList<>();
                        new_list.add(entity);
                        entities_per_distance.put(rounded_dist, new_list);
                    }
                }

                entities_per_distance.forEach((i, e) -> e.forEach((entity) -> {
                    if (x == 0 && !entity.is(player) && !affected_entities.contains(entity)) {
                        float num = 10f / (i + 3f);
                        Vec3 mul = new Vec3(num, num * 0.5, num);
                        entity.addDeltaMovement(calculateDir(player, entity, mul));
                        affected_entities.add(entity);
                        x = 10;
                    }

                    x--;
                }));

                affected_entities.clear();
            }
        }
        used = !used;
        return super.use(level, player, hand);
    }


    public Vec3 calculateDir(Entity e1, Entity e2, Vec3 multiplier) {
        double theta = 0;
        double alpha = 0;
        double sign = 1;

        double x = e1.position().x;
        double y = e1.position().y;
        double z = e1.position().z;

        double u = e2.position().x;
        double v = e2.position().y;
        double w = e2.position().z;

        if (!(u - x == 0)) {
            sign = (u - x) / Math.abs(u - x);
            theta = Math.atan((v - y) / (u - x));
            alpha = Math.atan((w - z) / (u - x));
        }

        return new Vec3(Math.cos(theta) * multiplier.x * sign,
                Math.sin(theta) * multiplier.y * sign,
                Math.sin(alpha) * multiplier.z * sign);
    }
}
