package org.exodusstudio.frostbite.common.item.weapons.elf_weapons;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.particle.Vec3ParticleOptions;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;
import org.joml.Quaternionf;
import org.joml.Vector3f;

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
                    Vec3 vec3 = owner.position().add(owner.getAttachments().get(EntityAttachment.WARDEN_CHEST, 0, owner.getYRot()));
                    Vec3 vec31 = owner.getLookAngle();
                    Vec3 vec32 = vec31.normalize();
                    Vec3 vec33 = vec3.add(vec32);
                    for (int j = 0; j < 30; j++) {
                        float angle = (float) (j * Math.PI / 15);
                        float playerYAngle = (float) ((90 - owner.getYRot()) * Math.PI / 180);
                        float playerXAngle = (float) (owner.getXRot() * Math.PI / 180);
                        Quaternionf quaternion;
                        if (Math.abs(vec32.x) < 0.5f) {
                            quaternion = new Quaternionf()
                                    .rotateLocalX(angle)
                                    .rotateLocalZ(playerXAngle)
                                    .rotateLocalY(playerYAngle);
                        } else {
                            quaternion = new Quaternionf()
                                    .rotateLocalZ(angle)
                                    .rotateLocalX(-playerXAngle)
                                    .rotateLocalY((float) (playerYAngle + Math.PI / 2));
                        }

                        Vector3f add = vec32.toVector3f().rotate(quaternion);

                        Vec3 vec34 = vec33.add(
                                add.x,
                                add.y,
                                add.z);
                        owner.level().addAlwaysVisibleParticle(
                                Vec3ParticleOptions.create(ParticleRegistry.ICY_BREATH_PARTICLE.get(), add),
                                vec34.x, vec34.y, vec34.z,
                                vec32.x * 0.1f,
                                vec32.y * 0.1f,
                                vec32.z * 0.1f);
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