package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.entity.custom.helper.PseudoEntity;
import org.exodusstudio.frostbite.common.util.TE;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class PseudoEntityTypes {
    public static final Map<String, PseudoEntityType> PSEUDO_ENTITY_TYPES = new HashMap<>();

    public static final PseudoEntityType BREATH = of("breath", c -> {
        PseudoEntity pseudoEntity = c.pseudoEntity();
        Level level = c.level();
        Entity owner = c.owner();

        if (owner == null) return;

        pseudoEntity.aabb = pseudoEntity.aabb.move(pseudoEntity.speed);
        if (level.getGameTime() % 5 == 0) {
            for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, pseudoEntity.aabb)) {
                if (entity == owner) continue;
                ((TE) entity).decreaseTemperature(6, false);
                ((TE) entity).decreaseTemperature(4, true);
            }
        }
    },
    c -> c.ticksAlive() > 40,
    _ -> false
    );

//    public static final PseudoEntityType PLAYER_ILLUSION = of("player_illusion", c -> {
//        PseudoEntity pseudoEntity = c.pseudoEntity();
//        Entity owner = c.owner();
//
//        if (owner == null) return;
//
//        Vec3 d = owner.getDeltaMovement();
//        if (owner.onGround()) {
//            d = d.multiply(1, 0 ,1);
//            pseudoEntity.aabb.setMaxY(owner.y);
//        }
//        pseudoEntity.aabb = pseudoEntity.aabb.move(d);
//    },
//    c -> !c.owner().isAlive() || c.ticksAlive() > 200,
//    c -> {
//        ServerLevel l = c.level();
//        PseudoEntity pe = c.pseudoEntity();
//        Vec3 pos = pe.aabb.getCenter();
//        Entity e = c.owner();
//        if (!(e instanceof Player p)) return true;
//
//        for (int i = 0; i < 80; i++) {
//            l.sendParticles(
//                    ParticleRegistry.SWIRLING_LEAF_PARTICLE.get(),
//                    pos.x + 0.5f * l.getRandom().nextDouble() - Math.sin(p.yHeadRot * Math.PI / 180) / 1.5f,
//                    pos.y + 0.5f * l.getRandom().nextDouble() + 1.25f,
//                    pos.z + 0.5f * l.getRandom().nextDouble() + Math.cos(p.yHeadRot * Math.PI / 180) / 1.5f,
//                    1,
//                    (0.5 - l.getRandom().nextDouble()) * 0.3,
//                    (0.5 - l.getRandom().nextDouble()) * 0.3,
//                    (0.5 - l.getRandom().nextDouble()) * 0.3,
//                    0.1);
//        }
//        l.playSound(null, BlockPos.containing(pos), SoundEvents.LAVA_EXTINGUISH, SoundSource.HOSTILE, 1f, l.getRandom().nextFloat() * 0.1F + 0.9F);
//        return true;
//    }
//    );

    public static PseudoEntityType of(
            String id,
            Consumer<PseudoEntity.PseudoEntityContext> tickFunction,
            Function<PseudoEntity.PseudoEntityContext, Boolean> removeFunction,
            Function<PseudoEntity.PseudoEntityContext, Boolean> despawnOnHit) {
        PseudoEntityType pseudoEntityType = new PseudoEntityType(id, tickFunction, removeFunction, despawnOnHit);
        PSEUDO_ENTITY_TYPES.put(id, pseudoEntityType);
        return pseudoEntityType;
    }

    public record PseudoEntityType(
            String id,
            Consumer<PseudoEntity.PseudoEntityContext> tick,
            Function<PseudoEntity.PseudoEntityContext, Boolean> shouldRemove,
            Function<PseudoEntity.PseudoEntityContext, Boolean> despawnOnHit
    ) {}
}
