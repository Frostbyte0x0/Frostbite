package org.exodusstudio.frostbite.common.worldgen.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.util.Util;
import org.exodusstudio.frostbite.common.util.helpers.Vec3Helper;

public class IceCoreSpikeFeature extends Feature<IceCoreSpikeFeatureConfiguration> {
    public static final RandomSource random = RandomSource.create();

    public IceCoreSpikeFeature(Codec<IceCoreSpikeFeatureConfiguration> codec) {
        super(codec);
    }

    private static void setSpikeBlock(WorldGenLevel level, Vec3 pos, IceCoreSpikeFeatureConfiguration config) {
        level.setBlock(BlockPos.containing(pos), config.spikeProvider.getState(level, random, BlockPos.containing(pos)), 19);
    }

    private static void setCoreBlock(WorldGenLevel level, BlockPos pos, IceCoreSpikeFeatureConfiguration config) {
        level.setBlock(pos, config.coreProvider.getState(level, random, pos), 19);
    }

    private static void placeDisk(Vec3 normal, float radius, WorldGenLevel level, Vec3 origin, IceCoreSpikeFeatureConfiguration config) {
        setSpikeBlock(level, origin, config);

        if (radius == 0) return;

        for (int i = 1; i < radius; i++) {
            for (float j = 0; j < 6 * i; j += 0.5f) {
                Vec3 d = normal.scale(i);
                Vec3 perpNormal = Vec3Helper.perpendicularY(d);
                Vec3 add = Vec3Helper.rotateFirstAroundSecond(perpNormal, normal, 2 * Math.PI * j / (6f * i));

                Vec3 pos = origin.add(
                        add.x,
                        add.y,
                        add.z);
                setSpikeBlock(level, pos, config);
            }
        }
    }

    private static void placeSpike(Vec3 dir, WorldGenLevel level, BlockPos origin, IceCoreSpikeFeatureConfiguration config) {
        dir = dir.normalize();
        Vec3 pos = new Vec3(origin).add(0.5, 0, 0.5);
        for (int i = 0; i < config.length; i++) {
            pos = pos.add(dir);
            placeDisk(dir, config.baseSpikeRadius * (config.length - i) / config.length, level, pos, config);
        }
    }

    @Override
    public boolean place(FeaturePlaceContext<IceCoreSpikeFeatureConfiguration> context) {
        Vec3 anchor = new Vec3(
                random.nextDouble() * Util.plusOrMinus(),
                random.nextDouble() * 4,
                random.nextDouble() * Util.plusOrMinus()).normalize();
        placeSpike(anchor, context.level(), context.origin(), context.config());

        int n = random.nextBoolean() ? 3 : 4;
        for (int i = 0; i < n; i++) {
            double lengthXZ = Util.distanceBetween(anchor.x, anchor.z);
            Vec3 perpAnchor = new Vec3(
                    -anchor.x * anchor.y / lengthXZ,
                    lengthXZ,
                    -anchor.z * anchor.y / lengthXZ);
            Vec3 add = Vec3Helper.rotateFirstAroundSecond(perpAnchor, anchor, 2 * Math.PI * i / n).normalize()
                    .add(0, 0.5, 0).normalize();
            add = new Vec3(add.x, Math.abs(add.y), add.z);


            placeSpike(add, context.level(), context.origin(), context.config());
        }

        setCoreBlock(context.level(), context.origin(), context.config());
        return false;
    }

//    private boolean doPlace(WorldGenLevel level, RandomSource random, BlockPos origin, BiConsumer<BlockPos, BlockState> rootSetter, BiConsumer<BlockPos, BlockState> trunkSetter, FoliagePlacer.FoliageSetter foliageSetter, IceCoreSpikeFeatureConfiguration config) {
//        int treeHeight = config.trunkPlacer.getTreeHeight(random);
//        int foliageHeight = config.foliagePlacer.foliageHeight(random, treeHeight, config);
//        int trunkHeight = treeHeight - foliageHeight;
//        int leafRadius = config.foliagePlacer.foliageRadius(random, trunkHeight);
//        BlockPos trunkOrigin = config.rootPlacer.map((rootPlacer) -> rootPlacer.getTrunkOrigin(origin, random)).orElse(origin);
//        int minY = Math.min(origin.getY(), trunkOrigin.getY());
//        int maxY = Math.max(origin.getY(), trunkOrigin.getY()) + treeHeight + 1;
//        if (minY >= level.getMinY() + 1 && maxY <= level.getMaxY() + 1) {
//            OptionalInt minClippedHeight = config.minimumSize.minClippedHeight();
//            int clippedTreeHeight = this.getMaxFreeTreeHeight(level, treeHeight, trunkOrigin, config);
//            if (clippedTreeHeight >= treeHeight || minClippedHeight.isPresent() && clippedTreeHeight >= minClippedHeight.getAsInt()) {
//                if (config.rootPlacer.isPresent() && !config.rootPlacer.get().placeRoots(level, rootSetter, random, origin, trunkOrigin, config)) {
//                    return false;
//                } else {
//                    List<FoliagePlacer.FoliageAttachment> foliageAttachments = config.trunkPlacer.placeTrunk(level, trunkSetter, random, clippedTreeHeight, trunkOrigin, config);
//                    foliageAttachments.forEach((foliageAttachment) -> config.foliagePlacer.createFoliage(level, foliageSetter, random, config, clippedTreeHeight, foliageAttachment, foliageHeight, leafRadius));
//                    return true;
//                }
//            } else {
//                return false;
//            }
//        } else {
//            return false;
//        }
//    }
//
//    public final boolean place(FeaturePlaceContext<IceCoreSpikeFeatureConfiguration> context) {
//        final WorldGenLevel level = context.level();
//        RandomSource random = context.random();
//        BlockPos origin = context.origin();
//        IceCoreSpikeFeatureConfiguration config = context.config();
//        Set<BlockPos> rootPositions = Sets.newHashSet();
//        Set<BlockPos> trunks = Sets.newHashSet();
//        final Set<BlockPos> foliage = Sets.newHashSet();
//        Set<BlockPos> decorations = Sets.newHashSet();
//        BiConsumer<BlockPos, BlockState> rootSetter = (pos, state) -> {
//            rootPositions.add(pos.immutable());
//            level.setBlock(pos, state, 19);
//        };
//        BiConsumer<BlockPos, BlockState> trunkSetter = (pos, state) -> {
//            trunks.add(pos.immutable());
//            level.setBlock(pos, state, 19);
//        };
//        FoliagePlacer.FoliageSetter foliageSetter = new FoliagePlacer.FoliageSetter() {
//            {
//                Objects.requireNonNull(TreeFeature.this);
//            }
//
//            public void set(BlockPos pos, BlockState state) {
//                foliage.add(pos.immutable());
//                level.setBlock(pos, state, 19);
//            }
//
//            public boolean isSet(BlockPos pos) {
//                return foliage.contains(pos);
//            }
//        };
//        BiConsumer<BlockPos, BlockState> decorationSetter = (pos, state) -> {
//            decorations.add(pos.immutable());
//            level.setBlock(pos, state, 19);
//        };
//        boolean result = this.doPlace(level, random, origin, rootSetter, trunkSetter, foliageSetter, config);
//        if (result && (!trunks.isEmpty() || !foliage.isEmpty())) {
//            if (!config.decorators.isEmpty()) {
//                TreeDecorator.Context decoratorContext = new TreeDecorator.Context(level, decorationSetter, random, trunks, foliage, rootPositions);
//                config.decorators.forEach((decorator) -> decorator.place(decoratorContext));
//            }
//
//            return (Boolean) BoundingBox.encapsulatingPositions(Iterables.concat(rootPositions, trunks, foliage, decorations)).map((bounds) -> {
//                DiscreteVoxelShape shape = updateLeaves(level, bounds, trunks, decorations, rootPositions);
//                StructureTemplate.updateShapeAtEdge(level, 3, shape, bounds.minX(), bounds.minY(), bounds.minZ());
//                return true;
//            }).orElse(false);
//        } else {
//            return false;
//        }
//    }
//
//    private static DiscreteVoxelShape updateLeaves(LevelAccessor level, BoundingBox bounds, Set<BlockPos> logs, Set<BlockPos> decorationSet, Set<BlockPos> rootPositions) {
//        DiscreteVoxelShape shape = new BitSetDiscreteVoxelShape(bounds.getXSpan(), bounds.getYSpan(), bounds.getZSpan());
//        int maxDistance = 7;
//        List<Set<BlockPos>> toCheck = Lists.newArrayList();
//
//        for(int i = 0; i < 7; ++i) {
//            toCheck.add(Sets.newHashSet());
//        }
//
//        for(BlockPos pos : Lists.newArrayList(Sets.union(decorationSet, rootPositions))) {
//            if (bounds.isInside(pos)) {
//                shape.fill(pos.getX() - bounds.minX(), pos.getY() - bounds.minY(), pos.getZ() - bounds.minZ());
//            }
//        }
//
//        BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
//        int smallestDistance = 0;
//        toCheck.getFirst().addAll(logs);
//
//        while(true) {
//            while(smallestDistance >= 7 || !toCheck.get(smallestDistance).isEmpty()) {
//                if (smallestDistance >= 7) {
//                    return shape;
//                }
//
//                Iterator<BlockPos> iterator = toCheck.get(smallestDistance).iterator();
//                BlockPos pos = iterator.next();
//                iterator.remove();
//                if (bounds.isInside(pos)) {
//                    if (smallestDistance != 0) {
//                        BlockState state = level.getBlockState(pos);
//                        setBlockKnownShape(level, pos, state.setValue(BlockStateProperties.DISTANCE, smallestDistance));
//                    }
//
//                    shape.fill(pos.getX() - bounds.minX(), pos.getY() - bounds.minY(), pos.getZ() - bounds.minZ());
//
//                    for(Direction direction : Direction.values()) {
//                        neighborPos.setWithOffset(pos, direction);
//                        if (bounds.isInside(neighborPos)) {
//                            int xInShape = neighborPos.getX() - bounds.minX();
//                            int yInShape = neighborPos.getY() - bounds.minY();
//                            int zinShape = neighborPos.getZ() - bounds.minZ();
//                            if (!shape.isFull(xInShape, yInShape, zinShape)) {
//                                BlockState currentState = level.getBlockState(neighborPos);
//                                OptionalInt distance = LeavesBlock.getOptionalDistanceAt(currentState);
//                                if (distance.isPresent()) {
//                                    int newDistance = Math.min(distance.getAsInt(), smallestDistance + 1);
//                                    if (newDistance < 7) {
//                                        toCheck.get(newDistance).add(neighborPos.immutable());
//                                        smallestDistance = Math.min(smallestDistance, newDistance);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            ++smallestDistance;
//        }
//    }
//
//    public static List<BlockPos> getLowestTrunkOrRootOfTree(TreeDecorator.Context context) {
//        List<BlockPos> blockPositions = Lists.newArrayList();
//        List<BlockPos> roots = context.roots();
//        List<BlockPos> logs = context.logs();
//        if (roots.isEmpty()) {
//            blockPositions.addAll(logs);
//        } else if (!logs.isEmpty() && roots.getFirst().getY() == logs.getFirst().getY()) {
//            blockPositions.addAll(logs);
//            blockPositions.addAll(roots);
//        } else {
//            blockPositions.addAll(roots);
//        }
//
//        return blockPositions;
//    }
}
