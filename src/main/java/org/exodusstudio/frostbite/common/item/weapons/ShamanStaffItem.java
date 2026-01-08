package org.exodusstudio.frostbite.common.item.weapons;

import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.entity.custom.misc.*;
import org.exodusstudio.frostbite.common.entity.custom.shaman.CurseBallEntity;
import org.exodusstudio.frostbite.common.entity.custom.shaman.WhirlpoolEntity;
import org.exodusstudio.frostbite.common.item.weapons.elf.ModeWeapon;
import org.exodusstudio.frostbite.common.util.Util;

public class ShamanStaffItem extends ModeWeapon {
    public ShamanStaffItem(Properties properties) {
        super(properties, new String[]{"Ethereal", "Whirlpool", "Curse"}, ChatFormatting.LIGHT_PURPLE, ChatFormatting.DARK_PURPLE);
    }

    @Override
    public void attack(Level level, LivingEntity owner) {
        if (level instanceof ServerLevel serverLevel) {
            switch (mode) {
                case "Ethereal" -> {
                    Vec3 v = owner.calculateViewVector(owner.getXRot(), owner.getYRot()).normalize();
                    EtherealWeaponEntity weapon;

                    if (level.getRandom().nextFloat() < 0.333f) {
                        weapon = new EtherealHandsEntity(null, serverLevel);
                    } else if (level.getRandom().nextFloat() < 0.5f) {
                        weapon = new EtherealHammerEntity(null, serverLevel);
                    } else {
                        weapon = new EtherealSwordEntity(null, serverLevel);
                    }

                    weapon.setPos(owner.position().add(v.scale(3)).add(0, 1.5, 0));
                    float[] angles = Util.getXYRot(v);
                    weapon.setXRot(angles[0]);
                    weapon.setYRot(angles[1]);
                    serverLevel.addFreshEntity(weapon);
                }
                case "Whirlpool" -> {
                    WhirlpoolEntity whirlpool = new WhirlpoolEntity(null, serverLevel);
                    whirlpool.setPos(owner.position());
                    whirlpool.setOwnerUUID(owner.getUUID());
                    serverLevel.addFreshEntity(whirlpool);
                }
                case "Curse" -> {
                    CurseBallEntity curse = new CurseBallEntity(null, serverLevel);
                    Vec3 v = owner.calculateViewVector(owner.getXRot(), owner.getYRot()).normalize();
                    curse.setPos(owner.position().add(v));
                    curse.setOwnerUUID(owner.getUUID());
                    curse.setLaunchDirection(v);
                    serverLevel.addFreshEntity(curse);
                }
            }
        }
    }
}
