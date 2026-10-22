package org.exodusstudio.frostbite.common.item.weapons;

import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.entity.custom.misc.EtherealHammerEntity;
import org.exodusstudio.frostbite.common.entity.custom.misc.EtherealHandsEntity;
import org.exodusstudio.frostbite.common.entity.custom.misc.EtherealSwordEntity;
import org.exodusstudio.frostbite.common.entity.custom.misc.EtherealWeaponEntity;
import org.exodusstudio.frostbite.common.entity.custom.shaman.CurseBallEntity;
import org.exodusstudio.frostbite.common.entity.custom.shaman.WhirlpoolEntity;
import org.exodusstudio.frostbite.common.item.weapons.elf.ModeWeapon;
import org.exodusstudio.frostbite.common.util.helpers.Vec3Helper;

public class ShamanStaffItem extends ModeWeapon {
    public static final String[] MODES = new String[]{"ethereal", "whirlpool", "curse"};

    public ShamanStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public void attack(Level level, LivingEntity owner) {
        if (level instanceof ServerLevel serverLevel) {
            switch (getMode(owner.getItemInHand(InteractionHand.MAIN_HAND))) {
                case "ethereal" -> {
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
                    float[] angles = Vec3Helper.getXYRot(v);
                    weapon.setXRot(angles[0]);
                    weapon.setYRot(angles[1]);
                    serverLevel.addFreshEntity(weapon);
                }
                case "whirlpool" -> {
                    WhirlpoolEntity whirlpool = new WhirlpoolEntity(null, serverLevel);
                    whirlpool.setPos(owner.position());
                    whirlpool.setOwnerUUID(owner.getUUID());
                    serverLevel.addFreshEntity(whirlpool);
                }
                case "curse" -> {
                    CurseBallEntity curse = new CurseBallEntity(null, serverLevel);
                    Vec3 v = owner.calculateViewVector(owner.getXRot(), owner.getYRot()).normalize();
                    curse.setPos(owner.getEyePosition().add(v));
                    curse.setOwnerUUID(owner.getUUID());
                    curse.setLaunchDirection(v);
                    serverLevel.addFreshEntity(curse);
                }
            }
        }
    }

    @Override
    public String[] getModes() {
        return MODES;
    }

    @Override
    public ChatFormatting regularColour() {
        return ChatFormatting.LIGHT_PURPLE;
    }

    @Override
    public ChatFormatting selectedColour() {
        return ChatFormatting.DARK_PURPLE;
    }
}
