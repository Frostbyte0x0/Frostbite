package org.exodusstudio.frostbite.common.worldgen.foliage;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import org.exodusstudio.frostbite.common.registry.FoliagePlacerRegistry;

public class MegaMistyFoliagePlacer extends MistyFoliagePlacer {
    public static final MapCodec<MegaMistyFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            foliagePlacerParts(instance)
                    .and(IntProvider.codec(0, 24).fieldOf("trunk_height")
                            .forGetter((foliagePlacer) -> foliagePlacer.trunkHeight))
                    .apply(instance, MegaMistyFoliagePlacer::new));
    private final IntProvider trunkHeight;

    public MegaMistyFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider trunkHeight) {
        super(radius, offset, trunkHeight);
        this.trunkHeight = trunkHeight;
        this.minCount = 5;
        this.maxCount = 7;
        this.large = true;
    }

    protected FoliagePlacerType<?> type() {
        return FoliagePlacerRegistry.MISTY_FOLIAGE_PLACER.get();
    }

    @Override
    protected float getSphereRadius(int y, int availableHeight, RandomSource random) {
        return 3f * (2f - 1.5f * y / availableHeight) + random.nextFloat() * 1.25f;
    }
}
