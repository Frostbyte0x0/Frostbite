package org.exodusstudio.frostbite.common.worldgen.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class IceCoreSpikeFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<IceCoreSpikeFeatureConfiguration> CODEC = RecordCodecBuilder.create((i) -> i.group(
            BlockStateProvider.CODEC.fieldOf("spike_provider").forGetter((c) -> c.spikeProvider),
            BlockStateProvider.CODEC.fieldOf("core_provider").forGetter((c) -> c.coreProvider),
            Codec.FLOAT.fieldOf("length").forGetter((c) -> c.length),
            Codec.FLOAT.fieldOf("base_spike_radius").forGetter((c) -> c.baseSpikeRadius),
            Codec.BOOL.fieldOf("both_up_and_down").orElse(false).forGetter((c) -> c.bothUpAndDown))
            .apply(i, IceCoreSpikeFeatureConfiguration::new));
    public final BlockStateProvider spikeProvider;
    public final BlockStateProvider coreProvider;
    public final float length;
    public final float baseSpikeRadius;
    public final boolean bothUpAndDown;

    protected IceCoreSpikeFeatureConfiguration(
            BlockStateProvider spikeProvider,
            BlockStateProvider coreProvider,
            float length,
            float baseSpikeRadius,
            boolean bothUpAndDown
    ) {
        this.spikeProvider = spikeProvider;
        this.coreProvider = coreProvider;
        this.length = length;
        this.baseSpikeRadius = baseSpikeRadius;
        this.bothUpAndDown = bothUpAndDown;
    }
}
