package org.exodusstudio.frostbite.common.item.custom;

import org.exodusstudio.frostbite.common.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class StunningBellItem extends Item {
    private final int range = 10;
    private boolean used = false;

    public StunningBellItem(Properties properties) {
        super(properties);
    }


    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (used) {
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class,
                    new AABB(new Vec3(player.getBlockX() - range, player.getBlockY() - range, player.getBlockZ() - range),
                            new Vec3(player.getBlockX() + range, player.getBlockY() + range, player.getBlockZ() + range)));

            if (!entities.isEmpty()) {
                for (LivingEntity entity : entities) {
                    if (!entity.is(player)) {
                        float num = 10f / (player.distanceTo(entity) + 3f);
                        Vec3 mul = new Vec3(num, num * 0.5, num);
                        entity.addDeltaMovement(calculateDir(player, entity, mul));
                    }
                }
            }

            level.playSound(null, player.blockPosition(), ModSounds.STUNNING_BELL_RING.get(), SoundSource.PLAYERS, 1f, 1f);
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
