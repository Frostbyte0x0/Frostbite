package org.exodusstudio.frostbite.common.worldgen.foliage;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.registry.BlockRegistry;
import org.exodusstudio.frostbite.common.registry.FoliagePlacerRegistry;

import java.util.ArrayList;
import java.util.List;

import static org.exodusstudio.frostbite.common.util.MathsUtil.plusOrMinus;

public class MistyFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<MistyFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            foliagePlacerParts(instance)
                    .and(IntProvider.codec(0, 24).fieldOf("trunk_height")
                    .forGetter((foliagePlacer) -> foliagePlacer.trunkHeight))
                    .apply(instance, MistyFoliagePlacer::new));
    protected final IntProvider trunkHeight;
    protected int treeHeight;
    protected int maxRadius;
    protected int maxY;
    protected int minY;
    protected final List<Pair<BlockPos, BlockPos>> leafPositions = new ArrayList<>();
    protected final List<Pair<BlockPos, BlockPos>> leafPositionsToRemove = new ArrayList<>();

    // Parameters to avoid copying over the whole functions in MegaMistyFoliagePlacer
    protected int minCount = 4;
    protected int maxCount = 6;
    protected int topRadius = 1;
    protected int offsetXZ = 0;
    protected int range = 4;

    public MistyFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider trunkHeight) {
        super(radius, offset);
        this.trunkHeight = trunkHeight;
    }

    protected FoliagePlacerType<?> type() {
        return FoliagePlacerRegistry.MEGA_MISTY_FOLIAGE_PLACER.get();
    }

    protected void createFoliage(
            LevelSimulatedReader level,
            FoliagePlacer.FoliageSetter blockSetter,
            RandomSource random,
            TreeConfiguration config,
            int maxTreeHeight,
            FoliagePlacer.FoliageAttachment attachment,
            int foliageHeight,
            int foliageRadius,
            int offset
    ) {
        // Place some sphere centers on the trunk
        // Loop over all the potential locations of leaves and place leaves if they're within a certain distance of center
        // Loop over all the locations of leaves and remove them at random
        // Loop over all the locations of leaves and remove leaves if they don't have enough neighbors

        this.leafPositions.clear();

        List<Pair<BlockPos, Float>> sphereCenters = generateSphereCenters(treeHeight - foliageHeight, treeHeight,
                minCount, maxCount, random);


        AABB locations = new AABB(
                -maxRadius, minY, -maxRadius,
                maxRadius, maxY, maxRadius
        );

        placeLeavesFromSphereCenters(sphereCenters, blockSetter, random,
                attachment.pos().offset(0, -treeHeight, 0), locations, level, config);


        deleteLeavesAtRandom(level, blockSetter, random);
        deleteLonelyLeaves(level, blockSetter);
    }

    protected List<Pair<BlockPos, Float>> generateSphereCenters(int bottomOffset, int trunkHeight, int minCount, int maxCount, RandomSource random) {
        List<Pair<BlockPos, Float>> centers = new ArrayList<>();
        centers.add(new Pair<>(new BlockPos(0, trunkHeight, 0), 1.5f + random.nextFloat()));

        int count = random.nextIntBetweenInclusive(minCount, maxCount) - 1;
        int availableHeight = trunkHeight - bottomOffset + offsetXZ;

        for (int i = 0; i < count; i++) {
            int y = bottomOffset + 2 + Math.round((float) i / count * availableHeight) + random.nextInt(1) * plusOrMinus();
            float radius = getSphereRadius(y, availableHeight, random);
            Pair<BlockPos, Float> pair = new Pair<>(new BlockPos(0, y, 0), radius);
            centers.add(pair);

            if (radius > maxRadius) {
                maxRadius = (int) Math.ceil(radius);
            }
        }

        minY = bottomOffset;
        maxY = trunkHeight + 3;

        return centers;
    }

    protected void placeLeavesFromSphereCenters(
            List<Pair<BlockPos, Float>> sphereCenters,
            FoliageSetter foliageSetter,
            RandomSource random,
            BlockPos origin,
            AABB locations,
            LevelSimulatedReader level,
            TreeConfiguration treeConfiguration
    ) {
        BlockPos.MutableBlockPos relativeLeafPos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos leafPos = new BlockPos.MutableBlockPos();

        for (int x = (int) locations.minX - 1; x < locations.maxX + 1; x++) {
            for (int y = (int) locations.minY; y < locations.maxY; y++) {
                for (int z = (int) locations.minZ - 1; z < locations.maxZ + 1; z++) {

                    leafPos.setWithOffset(Vec3i.ZERO, x, y, z);
                    for (Pair<BlockPos, Float> sphereCenter : sphereCenters) {
                        double dist = offsetXZ == 0 ? leafPos.distSqr(sphereCenter.getFirst()) : leafPos.distToCenterSqr(new Vec3(sphereCenter.getFirst()));
                        if (Math.sqrt(dist) <= sphereCenter.getSecond()) {
                            if (tryPlaceLeaf(level, foliageSetter, random, treeConfiguration,
                                    leafPos.setWithOffset(origin, x + offsetXZ, y, z + offsetXZ))) {
                                this.leafPositions.add(new Pair<>(
                                        relativeLeafPos.setWithOffset(Vec3i.ZERO, x, y, z).immutable(),
                                        leafPos.immutable()));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    protected void deleteLeavesAtRandom(LevelSimulatedReader level, FoliageSetter foliageSetter, RandomSource random) {
        for (Pair<BlockPos, BlockPos> positions : leafPositions) {
            BlockPos absolutePos = positions.getSecond();
            BlockPos pos = positions.getFirst();

            boolean isLeaf = level.isStateAtPosition(absolutePos, state -> state.is(BlockRegistry.MISTY_LEAVES));

            double dist = Math.sqrt(Math.pow(pos.getX(), 2) + Math.pow(pos.getZ(), 2));

            if (isLeaf && dist > 1 && random.nextFloat() * 7 < dist) { // && dist > 1.5 && random.nextFloat() < 0.35f
                foliageSetter.set(absolutePos, Blocks.AIR.defaultBlockState());
                leafPositionsToRemove.add(positions);
            }
        }

        leafPositions.removeAll(leafPositionsToRemove);
        leafPositionsToRemove.clear();
    }

    protected void deleteLonelyLeaves(LevelSimulatedReader level, FoliageSetter foliageSetter) {
        for (Pair<BlockPos, BlockPos> positions : leafPositions) {
            BlockPos pos = positions.getSecond();
            boolean isLeaf = level.isStateAtPosition(pos, state -> state.is(BlockRegistry.MISTY_LEAVES));
            int neighborCount = 0;

            if (level.isStateAtPosition(newPosWithOff(pos, 1, 0, 0), MistyFoliagePlacer::isMistyBlock)) neighborCount++;
            if (level.isStateAtPosition(newPosWithOff(pos, -1, 0, 0), MistyFoliagePlacer::isMistyBlock)) neighborCount++;
            if (level.isStateAtPosition(newPosWithOff(pos, 0, 1, 0), MistyFoliagePlacer::isMistyBlock)) neighborCount++;
            if (level.isStateAtPosition(newPosWithOff(pos, 0, -1, 0), MistyFoliagePlacer::isMistyBlock)) neighborCount++;
            if (level.isStateAtPosition(newPosWithOff(pos, 0, 0, 1), MistyFoliagePlacer::isMistyBlock)) neighborCount++;
            if (level.isStateAtPosition(newPosWithOff(pos, 0, 0, -1), MistyFoliagePlacer::isMistyBlock)) neighborCount++;

            if (isLeaf && neighborCount <= 1) {
                foliageSetter.set(pos, Blocks.AIR.defaultBlockState());
                leafPositionsToRemove.add(positions);
            }
        }

        leafPositions.removeAll(leafPositionsToRemove);
        leafPositionsToRemove.clear();
    }

    protected float getSphereRadius(int y, int availableHeight, RandomSource random) {
        return 2.5f * (1.25f - 1f * y / availableHeight) + random.nextFloat() * 1.25f;
    }

    protected static boolean isMistyBlock(BlockState state) {
        return state.is(BlockRegistry.MISTY_LEAVES) || state.is(BlockRegistry.MISTY_LOG);
    }

    protected BlockPos.MutableBlockPos newPosWithOff(BlockPos pos, int x, int y, int z) {
        return (new BlockPos.MutableBlockPos()).setWithOffset(pos, x, y, z);
    }

    public int foliageHeight(RandomSource random, int treeHeight, TreeConfiguration treeConfiguration) {
        this.treeHeight = treeHeight;
        return Math.max(4, treeHeight - this.trunkHeight.sample(random));
    }

    protected boolean shouldSkipLocation(
            RandomSource random,
            int localX,
            int localY,
            int localZ,
            int range,
            boolean large
    ) {
        return false;
    }
}