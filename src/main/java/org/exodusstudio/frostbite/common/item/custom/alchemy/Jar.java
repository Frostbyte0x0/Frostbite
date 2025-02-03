package org.exodusstudio.frostbite.common.item.custom.alchemy;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import org.exodusstudio.frostbite.common.registry.RegistryRegistry;

import java.util.List;

public class Jar implements FeatureElement {
    public static final Codec<Holder<Jar>> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Jar>> STREAM_CODEC;
    private final String name;
    private final List<MobEffectInstance> effects;
    private FeatureFlagSet requiredFeatures;

    public Jar(String name, MobEffectInstance... effects) {
        this.requiredFeatures = FeatureFlags.VANILLA_SET;
        this.name = name;
        this.effects = List.of(effects);
    }

    public Jar requiredFeatures(FeatureFlag... requiredFeatures) {
        this.requiredFeatures = FeatureFlags.REGISTRY.subset(requiredFeatures);
        return this;
    }

    public List<MobEffectInstance> getEffects() {
        return this.effects;
    }

    public String name() {
        return this.name;
    }

    static {
        CODEC = RegistryRegistry.JAR_REGISTRY.holderByNameCodec();
        STREAM_CODEC = ByteBufCodecs.holderRegistry(RegistryRegistry.JAR_REGISTRY_KEY);
    }

    @Override
    public FeatureFlagSet requiredFeatures() {
        return this.requiredFeatures;
    }

    public boolean isEnabled(FeatureFlagSet requiredFeatures) {
        return this.requiredFeatures().isSubsetOf(requiredFeatures);
    }
}
