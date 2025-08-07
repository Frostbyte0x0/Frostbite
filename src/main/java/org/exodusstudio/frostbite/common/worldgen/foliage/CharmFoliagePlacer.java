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

public class CharmFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<CharmFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            foliagePlacerParts(instance)
                    .and(IntProvider.codec(0, 24).fieldOf("trunk_height")
                    .forGetter((foliagePlacer) -> foliagePlacer.trunkHeight))
                    .apply(instance, CharmFoliagePlacer::new));
    private final IntProvider trunkHeight;
    private int treeHeight;
    private final List<Pair<BlockPos, BlockPos>> leafPositions = new ArrayList<>();
    private final List<Pair<BlockPos, BlockPos>> leafPositionsToRemove = new ArrayList<>();

    public CharmFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider trunkHeight) {
        super(radius, offset);
        this.trunkHeight = trunkHeight;
    }

    protected FoliagePlacerType<?> type() {
        return FoliagePlacerRegistry.CHARM_FOLIAGE_PLACER.get();
    }


    protected void createFoliage(
            LevelSimulatedReader level,
            FoliageSetter blockSetter,
            RandomSource random,
            TreeConfiguration config,
            int maxTreeHeight,
            FoliageAttachment attachment,
            int foliageHeight,
            int foliageRadius,
            int offset
    ) {
        // Place a sphere center on the trunk
        // Loop over all the potential locations of leaves and place leaves if they're within a certain distance of center
        // Loop over all the locations of leaves and remove them at random based on y level
        // Loop over all the locations of leaves and remove leaves if they don't have enough neighbors

        this.leafPositions.clear();

        int bottomOffset = treeHeight - foliageHeight;
        float radius = random.nextFloat();

        AABB locations = new AABB(
                -Math.floor(radius), bottomOffset, -Math.floor(radius),
                Math.ceil(radius), treeHeight + 3, Math.ceil(radius)
        );

        placeLeavesFromSphereCenter(new BlockPos(0, treeHeight, 0), 3, blockSetter, random,
                attachment.pos().offset(0, -treeHeight, 0), locations, level, config);


        deleteLeavesAtRandom(level, blockSetter, random);
        deleteLonelyLeaves(level, blockSetter);
    }


    private void placeLeavesFromSphereCenter(
            BlockPos sphereCenter,
            float radius,
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
                    double dist =
                            Math.pow(leafPos.getX() - sphereCenter.getX(), 2) +
                            Math.pow(leafPos.getY() - sphereCenter.getY(), 2) * 4 +
                            Math.pow(leafPos.getZ() - sphereCenter.getZ(), 2);
                    if (dist <= radius) {
                        if (tryPlaceLeaf(level, foliageSetter, random, treeConfiguration,
                                leafPos.setWithOffset(origin, x + 1, y, z + 1))) {
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

    private void deleteLeavesAtRandom(LevelSimulatedReader level, FoliageSetter foliageSetter, RandomSource random) {
        for (Pair<BlockPos, BlockPos> positions : leafPositions) {
            BlockPos absolutePos = positions.getSecond();
            BlockPos pos = positions.getFirst();

            boolean isLeaf = level.isStateAtPosition(absolutePos, state -> state.is(BlockRegistry.MISTY_LEAVES));

            double dist = Math.sqrt(Math.pow(pos.getX(), 2) + Math.pow(pos.getZ(), 2));

            if (isLeaf && random.nextFloat() * 7 < dist) { // && dist > 1.5 && random.nextFloat() < 0.35f
                foliageSetter.set(absolutePos, Blocks.AIR.defaultBlockState());
                leafPositionsToRemove.add(positions);
            }
        }

        leafPositions.removeAll(leafPositionsToRemove);
        leafPositionsToRemove.clear();
    }


    private void deleteLonelyLeaves(LevelSimulatedReader level, FoliageSetter foliageSetter) {
        for (Pair<BlockPos, BlockPos> positions : leafPositions) {
            BlockPos pos = positions.getSecond();
            boolean isLeaf = level.isStateAtPosition(pos, state -> state.is(BlockRegistry.MISTY_LEAVES));
            int neighborCount = 0;

            if (level.isStateAtPosition(newPosWithOff(pos, 1, 0, 0), CharmFoliagePlacer::isMistyBlock)) neighborCount++;
            if (level.isStateAtPosition(newPosWithOff(pos, -1, 0, 0), CharmFoliagePlacer::isMistyBlock)) neighborCount++;
            if (level.isStateAtPosition(newPosWithOff(pos, 0, 1, 0), CharmFoliagePlacer::isMistyBlock)) neighborCount++;
            if (level.isStateAtPosition(newPosWithOff(pos, 0, -1, 0), CharmFoliagePlacer::isMistyBlock)) neighborCount++;
            if (level.isStateAtPosition(newPosWithOff(pos, 0, 0, 1), CharmFoliagePlacer::isMistyBlock)) neighborCount++;
            if (level.isStateAtPosition(newPosWithOff(pos, 0, 0, -1), CharmFoliagePlacer::isMistyBlock)) neighborCount++;

            if (isLeaf && neighborCount <= 1) {
                foliageSetter.set(pos, Blocks.AIR.defaultBlockState());
                leafPositionsToRemove.add(positions);
            }
        }

        leafPositions.removeAll(leafPositionsToRemove);
        leafPositionsToRemove.clear();
    }


    private static boolean isMistyBlock(BlockState state) {
        return state.is(BlockRegistry.MISTY_LEAVES) || state.is(BlockRegistry.MISTY_LOG);
    }

    private BlockPos.MutableBlockPos newPosWithOff(BlockPos pos, int x, int y, int z) {
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