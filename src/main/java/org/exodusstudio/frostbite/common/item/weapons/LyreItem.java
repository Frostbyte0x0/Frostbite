package org.exodusstudio.frostbite.common.item.weapons;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.entity.custom.misc.EtherealHammerEntity;
import org.exodusstudio.frostbite.common.entity.custom.misc.EtherealHandsEntity;
import org.exodusstudio.frostbite.common.entity.custom.misc.EtherealSwordEntity;
import org.exodusstudio.frostbite.common.entity.custom.misc.EtherealWeaponEntity;
import org.exodusstudio.frostbite.common.item.weapons.elf.ModeWeapon;
import org.exodusstudio.frostbite.common.util.Util;

public class LyreItem extends ModeWeapon {
    public LyreItem(Properties properties) {
        super(properties, new String[]{"hammer", "hands", "sword"});
    }

    @Override
    public void attack(Level level, LivingEntity owner) {
        if (level instanceof ServerLevel serverLevel) {
            Vec3 v = owner.calculateViewVector(owner.getXRot(), owner.getYRot()).normalize();
            EtherealWeaponEntity weapon = null;
            switch (mode) {
                case "hammer" -> weapon = new EtherealHammerEntity(null, serverLevel);
                case "hands" -> weapon = new EtherealHandsEntity(null, serverLevel);
                case "sword" -> weapon = new EtherealSwordEntity(null, serverLevel);
            }
            assert weapon != null;
            weapon.setPos(owner.position().add(v.scale(3)).add(0, 1.5, 0));
            float[] angles = Util.getXYRot(v);
            weapon.setXRot(angles[0]);
            weapon.setYRot(angles[1]);
            serverLevel.addFreshEntity(weapon);
        }
    }
}
