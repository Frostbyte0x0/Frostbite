package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class IceBlockEntity extends FallingBlockEntity {
    public IceBlockEntity(EntityType<? extends FallingBlockEntity> p_31950_, Level p_31951_) {
        super(p_31950_, p_31951_);
        this.blockState = Blocks.BLUE_ICE.defaultBlockState();
    }

    @Override
    protected double getDefaultGravity() {
        return 0.03;
    }
}
