package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;

import static org.exodusstudio.frostbite.common.util.MathsUtil.calculateDir;

public class IceBlockEntity extends FallingBlockEntity {
    public IceBlockEntity(EntityType<? extends FallingBlockEntity> p_31950_, Level p_31951_) {
        super(p_31950_, p_31951_);
        this.blockState = Blocks.BLUE_ICE.defaultBlockState();
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float damage) {
        Frostbite.LOGGER.debug("A");
        if (damageSource.getEntity() instanceof Player player
                && (player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistry.ICE_HAMMER)
                    || player.getItemInHand(InteractionHand.OFF_HAND).is(ItemRegistry.ICE_HAMMER))) {
            this.addDeltaMovement(calculateDir(player, this, new Vec3(1, 1, 1)));
            Frostbite.LOGGER.debug("E");
            return true;
        }

        return false;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.03;
    }
}
