package org.exodusstudio.frostbite.common.item.weapons.elf;

import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.entity.custom.elves.ElfEntity;
import org.exodusstudio.frostbite.common.entity.custom.helper.PseudoEntity;
import org.exodusstudio.frostbite.common.entity.custom.misc.IceSpikeEntity;
import org.exodusstudio.frostbite.common.particle.options.Vec3ParticleOption;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;
import org.exodusstudio.frostbite.common.registry.PseudoEntityTypes;
import org.exodusstudio.frostbite.common.util.helpers.DataHelper;
import org.exodusstudio.frostbite.common.util.helpers.Vec3Helper;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CastingStaffItem extends ModeWeapon {
    public static final String[] MODES = new String[]{"icy_breath", "spikes"};
    private static final double AIMED_BREATH_RANGE = 5.0;
    private static final double AIMED_BREATH_RADIUS = 1.0;
    private static final double RADIAL_BREATH_RANGE = 2.5;
    private static final int RADIAL_BREATH_RAYS = 16;
    private static final int AIMED_BREATH_SEGMENTS = 10;
    private static final int AIMED_BREATH_RING_PARTICLES = 12;

    public CastingStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void performAttack(Level level, LivingEntity owner) {
        Vec3 vec3 = owner.position().add(0, owner.getEyeHeight(), 0);
        Vec3 vec31 = owner.getLookAngle();
        Vec3 vec32 = vec31.normalize();

        switch (getMode(owner.getItemInHand(InteractionHand.MAIN_HAND))) {
            case "icy_breath":
                if (level.isClientSide()) {
                    int particleCount = 30;
                    for (int i = 1; i < 4; i++) {
                        for (int j = 0; j < particleCount; j++) {
                            double angle = j * Math.PI * 2 / particleCount;
                            double y = 1 - 2 * (j + 0.5) / particleCount;
                            double horizontal = Math.sqrt(1 - y * y);
                            Vec3 direction = new Vec3(
                                    Math.cos(angle) * horizontal,
                                    y,
                                    Math.sin(angle) * horizontal);
                            double largestComponent = Math.max(Math.abs(direction.x),
                                    Math.max(Math.abs(direction.y), Math.abs(direction.z)));
                            Vec3 boundaryDirection = direction.scale(RADIAL_BREATH_RANGE / largestComponent);
                            Vec3 position = vec3.add(boundaryDirection.scale(i / 3.0));
                            addIcyBreathParticle(level, position,
                                    direction.scale(i / 3.0 * 0.001),
                                    direction.scale(0.05 * (i + 1)));
                        }
                    }

                    Vec3 reference = Math.abs(vec32.y) < 0.9 ? new Vec3(0, 1, 0) : new Vec3(1, 0, 0);
                    Vec3 side = vec32.cross(reference).normalize();
                    Vec3 up = side.cross(vec32).normalize();
                    for (int i = 1; i <= AIMED_BREATH_SEGMENTS; i++) {
                        Vec3 center = vec3.add(vec32.scale(AIMED_BREATH_RANGE * i / AIMED_BREATH_SEGMENTS));
                        for (int j = 0; j < AIMED_BREATH_RING_PARTICLES; j++) {
                            double angle = j * Math.PI * 2 / AIMED_BREATH_RING_PARTICLES;
                            Vec3 offset = side.scale(Math.cos(angle) * AIMED_BREATH_RADIUS)
                                    .add(up.scale(Math.sin(angle) * AIMED_BREATH_RADIUS));
                            addIcyBreathParticle(level, center.add(offset),
                                    vec32.scale(0.001),
                                    vec32.scale(0.08).add(offset.scale(0.01)));
                        }
                    }

                } else {
                    Set<Entity> affectedEntities = new HashSet<>();
                    AABB radialBounds = new AABB(
                            vec3.subtract(RADIAL_BREATH_RANGE),
                            vec3.add(RADIAL_BREATH_RANGE));

                    for (Entity entity1 : owner.level().getEntities(owner, radialBounds)) {
                        if (affectedEntities.add(entity1)) {
                            entity1.hurtServer((ServerLevel) level, owner.damageSources().freeze(), 4f);
                        }
                    }

                    Vec3 end = vec3.add(vec32.scale(AIMED_BREATH_RANGE));

                    for (Entity entity1 : owner.level().getEntities(owner, new AABB(vec3, end).inflate(AIMED_BREATH_RADIUS))) {
                        AABB aabb = entity1.getBoundingBox().inflate(AIMED_BREATH_RADIUS);
                        Optional<Vec3> optional1 = aabb.clip(vec3, end);
                        if (optional1.isPresent() && affectedEntities.add(entity1)) {
                            entity1.hurtServer((ServerLevel) level, owner.damageSources().freeze(), 4f);
                        }
                    }

                    addBreathTrail(level, owner, vec32, 0.3);
                    for (int i = 0; i < RADIAL_BREATH_RAYS; i++) {
                        double angle = i * Math.PI * 2 / RADIAL_BREATH_RAYS;
                        addBreathTrail(level, owner, new Vec3(Math.cos(angle), 0, Math.sin(angle)), 0.15);
                    }
                }
                break;
            case "spikes":
                if (!level.isClientSide()) {
                    Vec3 look = owner.getLookAngle().normalize();
                    Vec3 spawnPos = owner.position().add(0, owner.getEyeHeight(), 0).add(look.scale(1.5));

                    for (int i = 0; i < 6; i++) {
                        Vector3f add = vec32.toVector3f()
                                .rotate(Vec3Helper.getRotationQuaternionAroundLookVector(i, 6, owner, vec32));

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

    private static void addIcyBreathParticle(Level level, Vec3 position, Vec3 acceleration, Vec3 velocity) {
        level.addAlwaysVisibleParticle(
                Vec3ParticleOption.create(ParticleRegistry.ICY_BREATH_PARTICLE.get(), acceleration),
                position.x + (0.5 - random.nextDouble()) * 0.08,
                position.y + (0.5 - random.nextDouble()) * 0.08,
                position.z + (0.5 - random.nextDouble()) * 0.08,
                velocity.x + (0.5 - random.nextDouble()) * 0.02,
                velocity.y + (0.5 - random.nextDouble()) * 0.02,
                velocity.z + (0.5 - random.nextDouble()) * 0.02);
    }

    private static void addBreathTrail(Level level, LivingEntity owner, Vec3 direction, double speed) {
        Vec3 origin = owner.position().add(0, owner.getEyeHeight(), 0);
        DataHelper.addPseudoEntity(level, PseudoEntityTypes.BREATH,
                new PseudoEntity(direction.scale(speed),
                        new AABB(origin.add(0.75), origin.subtract(0.75)), owner.getUUID(), level.getGameTime()));
    }

    @Override
    public String[] getModes() {
        return MODES;
    }

    @Override
    public ChatFormatting regularColour() {
        return ChatFormatting.RED;
    }

    @Override
    public ChatFormatting selectedColour() {
        return ChatFormatting.DARK_RED;
    }
}
