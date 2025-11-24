package org.exodusstudio.frostbite.common.item.weapons.elf;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.custom.misc.IceSpikeEntity;
import org.exodusstudio.frostbite.common.entity.custom.elves.ElfEntity;
import org.exodusstudio.frostbite.common.particle.options.Vec3ParticleOption;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;
import org.exodusstudio.frostbite.common.util.BreathEntityLike;
import org.exodusstudio.frostbite.common.util.Util;
import org.joml.Vector3f;

import java.util.Optional;

public class CastingStaffItem extends AbstractStaff {
    public CastingStaffItem(Properties properties) {
        super(properties, new String[]{"icy breath", "spikes"});
    }

    @Override
    public void attack(Level level, LivingEntity owner) {
        Vec3 vec3 = owner.position().add(0, owner.getEyeHeight(), 0);
        Vec3 vec31 = owner.getLookAngle();
        Vec3 vec32 = vec31.normalize();

        switch (this.mode) {
            case "icy breath":
                if (level.isClientSide) {
                    int particleCount = 30;
                    for (int i = 1; i < 4; i++) {
                        Vec3 vec33 = vec3.add(vec32.scale(i / 10f + 0.3f));
                        for (int j = 0; j < particleCount; j++) {
                            Vector3f add = vec32.scale(i / 3f - 0.3f).toVector3f()
                                    .rotate(Util.getRotationQuaternionAroundLookVector(j, particleCount, owner, vec32));

                            Vec3 vec34 = vec33.add(
                                    add.x * 0.1,
                                    add.y * 0.1,
                                    add.z * 0.1);
                            owner.level().addAlwaysVisibleParticle(
                                    Vec3ParticleOption.create(ParticleRegistry.ICY_BREATH_PARTICLE.get(),
                                            add.normalize(i / 3f * 0.001f)),
                                    vec34.x + (0.5 - random.nextDouble()) * 0.1f,
                                    vec34.y + (0.5 - random.nextDouble()) * 0.1f,
                                    vec34.z + (0.5 - random.nextDouble()) * 0.1f,
                                    vec32.x * 0.05 * (i + 1) + add.x * 0.03 + (0.5 - random.nextDouble()) * 0.025,
                                    vec32.y * 0.05 * (i + 1) + add.y * 0.03 + (0.5 - random.nextDouble()) * 0.025,
                                    vec32.z * 0.05 * (i + 1) + add.z * 0.03 + (0.5 - random.nextDouble()) * 0.025);
                        }
                    }

                } else {
                    Vec3 end = vec3.add(vec32.scale(2.5));

                    for (Entity entity1 : owner.level().getEntities(owner, new AABB(vec3, end).inflate(1.0))) {
                        AABB aabb = entity1.getBoundingBox().inflate(1);
                        Optional<Vec3> optional1 = aabb.clip(vec3, end);
                        if (optional1.isPresent()) {
                            entity1.hurtServer((ServerLevel) level, owner.damageSources().freeze(), 4f);
                        }
                    }

                    Frostbite.breathEntityLikes.add(
                            new BreathEntityLike(vec32.scale(0.15),
                                    new AABB(vec3.add(0.75), vec3.subtract(0.75)), (ServerLevel) level, owner));
                }
                break;
            case "spikes":
                if (!level.isClientSide) {
                    Vec3 look = owner.getLookAngle().normalize();
                    Vec3 spawnPos = owner.position().add(0, owner.getEyeHeight(), 0).add(look.scale(1.5));

                    for (int i = 0; i < 6; i++) {
                        Vector3f add = vec32.toVector3f()
                                .rotate(Util.getRotationQuaternionAroundLookVector(i, 6, owner, vec32));

                        Vec3 vec33 = vec3.add(vec32.scale(i / 10f + 0.3f));
                        Vec3 vec34 = vec33.add(
                                add.x,
                                add.y,
                                add.z);

                        Vec3 v = spawnPos.add(look.scale(8));

                        double d0 = v.x - vec34.x;
                        double d1 = v.y - vec34.y;
                        double d2 = v.z - vec34.z;
                        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                        float yRot = (float) (Mth.atan2(d2, d0) * 180f / Math.PI - 90f);
                        float xRot = (float) -(Mth.atan2(d1, d3) * 180f / Math.PI);

                        IceSpikeEntity spike = new IceSpikeEntity(level,
                                vec34.x,
                                vec34.y,
                                vec34.z,
                                yRot,
                                xRot,
                                i * 3 + 10,
                                owner,
                                false);

                        Vec3 end = vec3.add(vec32.scale(15));
                        double distance = Double.MAX_VALUE;
                        LivingEntity tracked = null;

                        for (Entity entity1 : owner.level().getEntities(owner, new AABB(vec3, end).inflate(1.0))) {
                            if (owner instanceof ElfEntity && entity1 instanceof ElfEntity) continue;
                            AABB aabb = entity1.getBoundingBox().inflate(1);
                            Optional<Vec3> optional1 = aabb.clip(vec3, end);
                            if (optional1.isPresent() && entity1 instanceof LivingEntity livingEntity
                                    && optional1.get().distanceTo(v) < distance) {
                                distance = optional1.get().distanceTo(v);
                                tracked = livingEntity;
                            }
                        }

                        spike.setTrackedEntity(tracked);
                        level.addFreshEntity(spike);
                    }
                }
                break;
        }
    }
}