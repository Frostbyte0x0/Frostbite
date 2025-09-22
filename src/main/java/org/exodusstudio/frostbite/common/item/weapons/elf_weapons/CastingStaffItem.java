package org.exodusstudio.frostbite.common.item.weapons.elf_weapons;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;

public class CastingStaffItem extends AbstractStaff {
    public CastingStaffItem(Properties properties) {
        super(properties, new String[]{"icy breath", "spikes"});
    }

    @Override
    public void attack(Level level, LivingEntity owner) {
        switch (this.mode) {
            case "icy breath":
                double xPos = Math.sin(owner.yHeadRot * Math.PI / 180);
                double zPos = Math.cos(owner.yHeadRot * Math.PI / 180);
                if (level.isClientSide) {
                    for (int i = 0; i < 80; i++) {
                        level.addAlwaysVisibleParticle(
                                ColorParticleOption.create(ParticleRegistry.BLIZZARD_PARTICLE.get(),
                                        (random.nextInt(80) + 80) / 255f,
                                        (random.nextInt(30) + 30) / 255f,
                                        (random.nextInt(80) + 150) / 255f),
                                owner.getX() + 0.5f * random.nextDouble() - xPos / 2f,
                                owner.getY() + 0.5f * random.nextDouble() + 1,
                                owner.getZ() + 0.5f * random.nextDouble() + zPos / 2f,
//                                owner.getX() + 0.5f * random.nextDouble() - owner.getLookAngle().x / 1.5f,
//                                owner.getY() + 0.5f * random.nextDouble(),
//                                owner.getZ() + 0.5f * random.nextDouble() + owner.getLookAngle().z / 1.5f,
                                (0.5D - random.nextDouble()) * 0.12 - xPos * 0.1f,
                                (0.5D - random.nextDouble()) * 0.12,
                                (0.5D - random.nextDouble()) * 0.12 + zPos * 0.1f);
                    }
                } else {
                    level.getEntitiesOfClass(LivingEntity.class,
                            owner.getBoundingBox().move(xPos, 0, zPos))
                            .forEach(entity -> {
                        entity.hurtServer((ServerLevel) level, owner.damageSources().magic(), 4.0f);
                        Frostbite.temperatureStorage.decreaseTemperature(entity, 20, false);
                        Frostbite.temperatureStorage.decreaseTemperature(entity, 20, true);
                    });
                }
                break;
            case "spikes":

                break;
        }
    }
}