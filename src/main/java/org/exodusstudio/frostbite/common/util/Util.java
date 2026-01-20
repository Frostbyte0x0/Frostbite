package org.exodusstudio.frostbite.common.util;

import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class Util {
    static RandomSource random = RandomSource.create();
    static Item[] liningItems = new Item[] {
            ItemRegistry.WOOLLY_WOOL.asItem(),
            ItemRegistry.FROZEN_FUR.asItem(),
            ItemRegistry.INSULATED_CLOTH.asItem(),
            ItemRegistry.HEATED_COATING.asItem(),
            ItemRegistry.FROZEN_PLATING.asItem(),
    };
    static Item[] weavingPatterns = new Item[] {
            ItemRegistry.HELMET_WEAVING_PATTERN.asItem(),
            ItemRegistry.CHESTPLATE_WEAVING_PATTERN.asItem(),
            ItemRegistry.LEGGINGS_WEAVING_PATTERN.asItem(),
            ItemRegistry.BOOTS_WEAVING_PATTERN.asItem(),
    };

    public static HashMap<Item, HashMap<Item, Item>> linings = new HashMap<>() {{
        put(Items.WHITE_WOOL.asItem(), new HashMap<>() {{
            put(ItemRegistry.HELMET_WEAVING_PATTERN.asItem(),     ItemRegistry.WOOL_LINING_HELMET.asItem());
            put(ItemRegistry.CHESTPLATE_WEAVING_PATTERN.asItem(), ItemRegistry.WOOL_LINING_CHESTPLATE.asItem());
            put(ItemRegistry.LEGGINGS_WEAVING_PATTERN.asItem(),   ItemRegistry.WOOL_LINING_LEGGINGS.asItem());
            put(ItemRegistry.BOOTS_WEAVING_PATTERN.asItem(),      ItemRegistry.WOOL_LINING_BOOTS.asItem());
        }});
        put(ItemRegistry.WOOLLY_WOOL.asItem(), new HashMap<>() {{
            put(ItemRegistry.HELMET_WEAVING_PATTERN.asItem(),     ItemRegistry.WOOLLY_WOOL_LINING_HELMET.asItem());
            put(ItemRegistry.CHESTPLATE_WEAVING_PATTERN.asItem(), ItemRegistry.WOOLLY_WOOL_LINING_CHESTPLATE.asItem());
            put(ItemRegistry.LEGGINGS_WEAVING_PATTERN.asItem(),   ItemRegistry.WOOLLY_WOOL_LINING_LEGGINGS.asItem());
            put(ItemRegistry.BOOTS_WEAVING_PATTERN.asItem(),      ItemRegistry.WOOLLY_WOOL_LINING_BOOTS.asItem());
        }});
        put(ItemRegistry.FROZEN_FUR.asItem(), new HashMap<>() {{
            put(ItemRegistry.HELMET_WEAVING_PATTERN.asItem(),     ItemRegistry.FROZEN_FUR_LINING_HELMET.asItem());
            put(ItemRegistry.CHESTPLATE_WEAVING_PATTERN.asItem(), ItemRegistry.FROZEN_FUR_LINING_CHESTPLATE.asItem());
            put(ItemRegistry.LEGGINGS_WEAVING_PATTERN.asItem(),   ItemRegistry.FROZEN_FUR_LINING_LEGGINGS.asItem());
            put(ItemRegistry.BOOTS_WEAVING_PATTERN.asItem(),      ItemRegistry.FROZEN_FUR_LINING_BOOTS.asItem());
        }});
        put(ItemRegistry.INSULATED_CLOTH.asItem(), new HashMap<>() {{
            put(ItemRegistry.HELMET_WEAVING_PATTERN.asItem(),     ItemRegistry.INSULATED_CLOTH_LINING_HELMET.asItem());
            put(ItemRegistry.CHESTPLATE_WEAVING_PATTERN.asItem(), ItemRegistry.INSULATED_CLOTH_LINING_CHESTPLATE.asItem());
            put(ItemRegistry.LEGGINGS_WEAVING_PATTERN.asItem(),   ItemRegistry.INSULATED_CLOTH_LINING_LEGGINGS.asItem());
            put(ItemRegistry.BOOTS_WEAVING_PATTERN.asItem(),      ItemRegistry.INSULATED_CLOTH_LINING_BOOTS.asItem());
        }});
        put(ItemRegistry.HEATED_COATING.asItem(), new HashMap<>() {{
            put(ItemRegistry.HELMET_WEAVING_PATTERN.asItem(),     ItemRegistry.HEATED_COATING_LINING_HELMET.asItem());
            put(ItemRegistry.CHESTPLATE_WEAVING_PATTERN.asItem(), ItemRegistry.HEATED_COATING_LINING_CHESTPLATE.asItem());
            put(ItemRegistry.LEGGINGS_WEAVING_PATTERN.asItem(),   ItemRegistry.HEATED_COATING_LINING_LEGGINGS.asItem());
            put(ItemRegistry.BOOTS_WEAVING_PATTERN.asItem(),      ItemRegistry.HEATED_COATING_LINING_BOOTS.asItem());
        }});
        put(ItemRegistry.FROZEN_PLATING.asItem(), new HashMap<>() {{
            put(ItemRegistry.HELMET_WEAVING_PATTERN.asItem(),     ItemRegistry.FROZEN_PLATING_LINING_HELMET.asItem());
            put(ItemRegistry.CHESTPLATE_WEAVING_PATTERN.asItem(), ItemRegistry.FROZEN_PLATING_LINING_CHESTPLATE.asItem());
            put(ItemRegistry.LEGGINGS_WEAVING_PATTERN.asItem(),   ItemRegistry.FROZEN_PLATING_LINING_LEGGINGS.asItem());
            put(ItemRegistry.BOOTS_WEAVING_PATTERN.asItem(),      ItemRegistry.FROZEN_PLATING_LINING_BOOTS.asItem());
        }});
    }};

    public static boolean isLiningMaterial(ItemStack item) {
        return Arrays.asList(liningItems).contains(item.getItem()) || item.is(ItemTags.WOOL);
    }

    public static boolean isWeavingPattern(Item item) {
        return Arrays.asList(weavingPatterns).contains(item);
    }

    public static Vec3 calculateDir(Entity e1, Entity e2, Vec3 multiplier) {
        double theta = 0;
        double alpha = 0;
        double sign = 1;

        double x = e1.position().x;
        double y = e1.position().y;
        double z = e1.position().z;

        double u = e2.position().x;
        double v = e2.position().y;
        double w = e2.position().z;

        if (!(u - x == 0)) {
            sign = (u - x) / Math.abs(u - x);
            theta = Math.atan((v - y) / (u - x));
            alpha = Math.atan((w - z) / (u - x));
        }

        return new Vec3(Math.cos(theta) * multiplier.x * sign,
                Math.sin(theta) * multiplier.y * sign,
                Math.sin(alpha) * multiplier.z * sign);
    }

    public static int plusOrMinus() {
        return random.nextBoolean() ? 1 : -1;
    }

    public static void spawnParticleRandomly(Entity entity, SimpleParticleType particleType, double positionVariation, double speedVariation) {
        double d0 = entity.getX() + (0.5D - entity.getRandom().nextDouble()) * positionVariation;
        double d1 = entity.getY() + (0.5D - entity.getRandom().nextDouble()) * positionVariation;
        double d2 = entity.getZ() + (0.5D - entity.getRandom().nextDouble()) * positionVariation;

        entity.level().addAlwaysVisibleParticle(
                particleType,
                d0, d1, d2,
                (0.5 - entity.getRandom().nextDouble()) * speedVariation,
                (0.5 - entity.getRandom().nextDouble()) * speedVariation,
                (0.5 - entity.getRandom().nextDouble()) * speedVariation);
    }

    public static AABB squareAABB(AABB aabb, double inflation) {
        double sizeX = aabb.maxX - aabb.minX;
        double sizeY = aabb.maxY - aabb.minY;
        double sizeZ = aabb.maxZ - aabb.minZ;

        double minSize = Math.min(sizeX, Math.min(sizeY, sizeZ));
        Vec3 start = new Vec3(aabb.minX + sizeX / 2, aabb.minY + sizeY / 2, aabb.minZ + sizeZ / 2);
        Vec3 end = start.add(minSize);
        return new AABB(start, end).inflate(inflation);
    }

    public static AABB squareAABB(Vec3 center, double size) {
        Vec3 start = new Vec3(center.x - size / 2, center.y - size / 2, center.z - size / 2);
        Vec3 end = start.add(size);
        return new AABB(start, end);
    }

    public static List<BlockPos> getBlockPositionsInAABB(AABB aabb) {
        List<BlockPos> positions = new ArrayList<>();

        int sizeX = Math.toIntExact(Math.round(aabb.maxX) - Math.round(aabb.minX));
        int sizeY = Math.toIntExact(Math.round(aabb.maxY) - Math.round(aabb.minY));
        int sizeZ = Math.toIntExact(Math.round(aabb.maxZ) - Math.round(aabb.minZ));

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                for (int k = 0; k < sizeZ; k++) {
                    positions.add(new BlockPos(Math.round((float) aabb.minX) + i,
                                               Math.round((float) aabb.minY) + j,
                                               Math.round((float) aabb.minZ) + k));
                }
            }
        }

        return positions;
    }

    public static double distanceBetweenVec(Vec3 v1, Vec3 v2) {
        return Math.sqrt(Math.pow(v1.x - v2.x, 2) + Math.pow(v1.y - v2.y, 2) + Math.pow(v1.z - v2.z, 2));
    }

    public static double distanceBetween(double v1, double v2) {
        return Math.sqrt(Math.pow(v1, 2) + Math.pow(v2, 2));
    }

    public static boolean isFrostbite(Level level) {
        return level.dimension().toString().equals("ResourceKey[minecraft:dimension / frostbite:frostbite]");
    }

    public static <T extends Monster> void spawnMonsterRandomlyAroundEntity(Supplier<T> monsterSupplier, ServerLevel serverLevel, Entity e, int minDist, int maxDist) {
        BlockPos blockpos = e.blockPosition().offset(
                random.nextIntBetweenInclusive(minDist, maxDist) * plusOrMinus(),
                random.nextIntBetweenInclusive(0, 4) * plusOrMinus(),
                random.nextIntBetweenInclusive(minDist, maxDist) * plusOrMinus());

        if (serverLevel.getBlockState(blockpos.below()).isAir()) return;

        T monster = monsterSupplier.get();

        if (getBlockPositionsInAABB(monster.getBoundingBox()).stream().allMatch(
                pos -> serverLevel.getBlockState(pos).isAir())) {
            monster.setPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            if (monster instanceof Ownable && e instanceof Player player) {
                ((Ownable) monster).setOwner(player);
            }

            serverLevel.addFreshEntityWithPassengers(monster);
            serverLevel.gameEvent(GameEvent.ENTITY_PLACE, blockpos, GameEvent.Context.of(e));
        }
    }

    public static void spawnParticlesFromAABB(Level level, AABB aabb, int count) {
        int[] xyz = new int[]{1, 0, 0};

        for (int j = 0; j < 3; j++) {
            for (float i = 0; i <= count; i++) {
                double x = Mth.lerp(xyz[0] * i / count, aabb.minX, aabb.maxX);
                double y = Mth.lerp(xyz[1] * i / count, aabb.minY, aabb.maxY);
                double z = Mth.lerp(xyz[2] * i / count, aabb.minZ, aabb.maxZ);

                level.addAlwaysVisibleParticle(
                        ParticleRegistry.DEBUG_PARTICLE.get(),
                        x,
                        y,
                        z,
                        0,
                        0,
                        0);
            }
            rotateArrayByOneRight(xyz);
        }

        for (int j = 0; j < 3; j++) {
            for (float i = 0; i <= count; i++) {
                double x = Mth.lerp(xyz[0] * i / count, aabb.maxX, aabb.minX);
                double y = Mth.lerp(xyz[1] * i / count, aabb.maxY, aabb.minY);
                double z = Mth.lerp(xyz[2] * i / count, aabb.maxZ, aabb.minZ);

                level.addAlwaysVisibleParticle(
                        ParticleRegistry.DEBUG_PARTICLE.get(),
                        x,
                        y,
                        z,
                        0,
                        0,
                        0);
            }
            rotateArrayByOneRight(xyz);
        }
    }

    public static void spawnParticlesFromVector(Level level, Vec3 origin, Vec3 vector, int count) {
        Vec3 pos = origin;
        for (int i = 0; i <= count; i++) {
            pos = Mth.lerp((double) i / count, pos, origin.add(vector));
            level.addAlwaysVisibleParticle(
                    ParticleRegistry.DEBUG_PARTICLE.get(),
                    pos.x,
                    pos.y,
                    pos.z,
                    0,
                    0,
                    0);
        }
    }

    public static void rotateArrayByOneRight(int[] arr) {
        if (arr == null || arr.length <= 1) return;

        int lastElement = arr[arr.length - 1];
        for (int i = arr.length - 1; i > 0; i--) {
            arr[i] = arr[i - 1];
        }

        arr[0] = lastElement;
    }

    public static Quaternionf getRotationQuaternionAroundLookVector(int j, int count, Entity owner, Vec3 vec32) {
        float angle = (float) (j * 2 * Math.PI / count);
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

        return quaternion;
    }

    public static Quaternionf getRotationQuaternionAroundVector(float angle, Vec3 vec32) {
        float playerYAngle = (float) (Math.atan2(vec32.z, vec32.x) + Math.PI / 4);
        float playerXAngle = (float) -Math.atan2(vec32.y, vec32.x);
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

        return quaternion;
    }

    public static float[] getXYRot(Vec3 dir) {
        float xRot = (float) Math.toDegrees(Math.asin(-dir.y));
        float yRot = (float) -Math.toDegrees(Math.atan2(dir.x, dir.z));
        return new float[]{xRot, yRot};
    }

    public static void blendAnimations(
            int ticksSinceLastChange,
            int blendTicks,
            float partialTick,
            float ageInTicks,
            KeyframeAnimation lastAnimation,
            AnimationState lastAnimationState,
            KeyframeAnimation newAnimation,
            AnimationState newAnimationState
    ) {
        if (ticksSinceLastChange < blendTicks) {
            float blendProgress = Mth.clamp((ticksSinceLastChange + partialTick) / blendTicks, 0f, 1f);

            lastAnimationState.ifStarted((state) -> lastAnimation.apply(state.getTimeInMillis(ageInTicks), 1 - blendProgress));
            newAnimationState.ifStarted((state) -> newAnimation.apply(state.getTimeInMillis(ageInTicks), blendProgress));
        }
    }

    public static void drawTexture(GuiGraphics graphics, int leftPos, int topPos, int width, int height, Identifier texture) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos, topPos, 0f, 0f, width, height, width, height);
    }
}
