package org.exodusstudio.frostbite.common.entity.custom.murdershrooms;

import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class AgaricMurdershroomEntity extends AbstractMurdershroom {
    public AgaricMurdershroomEntity(EntityType<? extends Monster> entityType, Level level) {
        super(EntityRegistry.AGARIC_MURDERSHROOM.get(), level);
    }
}
