package org.exodusstudio.frostbite.common.registry;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
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
    }, c -> c.ticksAlive() > 40);

    public static PseudoEntityType of(String id, Consumer<PseudoEntity.PseudoEntityContext> tickFunction, Function<PseudoEntity.PseudoEntityContext, Boolean> removeFunction) {
        PseudoEntityType pseudoEntityType = new PseudoEntityType(id, tickFunction, removeFunction);
        PSEUDO_ENTITY_TYPES.put(id, pseudoEntityType);
        return pseudoEntityType;
    }

    public record PseudoEntityType(
            String id,
            Consumer<PseudoEntity.PseudoEntityContext> tick,
            Function<PseudoEntity.PseudoEntityContext, Boolean> shouldRemove
    ) {}
}
