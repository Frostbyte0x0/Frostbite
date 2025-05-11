package org.exodusstudio.frostbite.common.item.custom;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.registry.SoundRegistry;

import java.util.List;

import static org.exodusstudio.frostbite.common.util.MathsUtil.calculateDir;

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

            level.playSound(null, player.blockPosition(), SoundRegistry.STUNNING_BELL_RING.get(), SoundSource.PLAYERS, 1f, 1f);
        }

        used = !used;
        return super.use(level, player, hand);
    }
}
