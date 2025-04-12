package org.exodusstudio.frostbite.common.entity.custom.murdershrooms;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.registry.EffectRegistry;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

public class DecayingMurdershroomEntity extends AbstractMurdershroom {
    public DecayingMurdershroomEntity(EntityType<? extends Monster> entityType, Level level) {
        super(EntityRegistry.DECAYING_MURDERSHROOM.get(), level, EffectRegistry.DECAY);
    }
}
