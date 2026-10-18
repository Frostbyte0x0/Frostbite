package org.exodusstudio.frostbite.common.worldgen.foliage;

import com.mojang.datafixers.Products;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import org.exodusstudio.frostbite.common.registry.FoliagePlacerRegistry;

public class CharmFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<CharmFoliagePlacer> CODEC =
            RecordCodecBuilder.mapCodec(i -> parts(i).apply(i, CharmFoliagePlacer::new));

    protected static <P extends CharmFoliagePlacer> Products.P3<RecordCodecBuilder.Mu<P>, IntProvider, IntProvider, IntProvider> parts(RecordCodecBuilder.Instance<P> instance) {
        return foliagePlacerParts(instance).and(IntProviders.codec(0, 16).fieldOf("trunk_height").forGetter(p -> p.trunkHeight));
    }

    final IntProvider trunkHeight;

    public CharmFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider trunkHeight) {
        super(radius, offset);
        this.trunkHeight = trunkHeight;
    }

    protected FoliagePlacerType<?> type() {
        return FoliagePlacerRegistry.CHARM_FOLIAGE_PLACER.get();
    }

    protected void createFoliage(
            WorldGenLevel level,
            FoliageSetter blockSetter,
            RandomSource random,
            TreeConfiguration config,
            int maxTreeHeight,
            FoliageAttachment attachment,
            int foliageHeight,
            int foliageRadius,
            int offset
    ) {
        // Keep the canopy at the attachment point. The old implementation built
        // the shape relative to the whole trunk, which allowed leaves to spread
        // down the sides of the tree and made the grove look like a dense hedge.
        int radius = Math.max(2, foliageRadius);
        int bottom = Math.min(2, Math.max(1, foliageHeight / 2));

        for (int localY = -bottom; localY <= 1; localY++) {
            int layerRadius = localY == -bottom
                    ? Math.max(1, radius - 1)
                    : localY == 1 ? Math.max(1, radius - 2) : radius;

            for (int localX = -layerRadius; localX <= layerRadius; localX++) {
                for (int localZ = -layerRadius; localZ <= layerRadius; localZ++) {
                    if (localX * localX + localZ * localZ > layerRadius * layerRadius + 1) continue;
                    if (localY < 0 && random.nextFloat() < 0.08F) continue;

                    BlockPos leafPos = attachment.pos().offset(localX, localY, localZ);
                    if (tryPlaceLeaf(level, blockSetter, random, config, leafPos)
                            && localY == 1
                            && random.nextFloat() < 0.35F
                            && level.isEmptyBlock(leafPos.above())) {
                        level.setBlock(leafPos.above(), Blocks.SNOW.defaultBlockState(), 2);
                    }
                }
            }
        }
    }

    public int foliageHeight(RandomSource random, int treeHeight, TreeConfiguration treeConfiguration) {
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
