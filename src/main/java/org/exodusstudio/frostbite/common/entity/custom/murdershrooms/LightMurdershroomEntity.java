package org.exodusstudio.frostbite.common.entity.custom.murdershrooms;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.registry.EffectRegistry;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

public class LightMurdershroomEntity extends AbstractMurdershroom {
    public LightMurdershroomEntity(EntityType<? extends Monster> entityType, Level level) {
        super(EntityRegistry.LIGHT_MURDERSHROOM.get(), level, EffectRegistry.TWITCHING);
    }
}
