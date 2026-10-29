package org.exodusstudio.frostbite.common.registry.deferred;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class DeferredHolder<R, T extends R> extends Holder.Reference<R> implements Supplier<T> {
    protected final ResourceKey<R> key;
    private Holder.Reference<R> holder;

    protected DeferredHolder(ResourceKey<R> key) {
        super(Type.STAND_ALONE, new HolderOwner<>() {}, key, null);
        this.key = key;
    }

    public static <R, T extends R> DeferredHolder<R, T> create(ResourceKey<? extends Registry<R>> registryKey, Identifier id) {
        return new DeferredHolder<>(ResourceKey.create(registryKey, id));
    }

    @Override
    public T get() {
        return (T) value();
    }

    public Optional<T> asOptional() {
        return isBound() ? Optional.of(get()) : Optional.empty();
    }

    public ResourceKey<R> getKey() {
        return key;
    }

    public Identifier getId() {
        return key.identifier();
    }

    private Optional<Holder.Reference<R>> tryBind() {
        if (holder == null) {
            Registry<R> registry = (Registry<R>) BuiltInRegistries.REGISTRY.getValue(key.registry());
            if (registry == null) return Optional.empty();
            registry.get(key).ifPresent(reference -> holder = reference);
        }
        return Optional.ofNullable(holder);
    }

    private Holder.Reference<R> bound() {
        return tryBind().orElseThrow(() -> new IllegalStateException("Registry entry not present: " + key));
    }

    @Override
    public ResourceKey<R> key() {
        return key;
    }

    @Override
    public R value() {
        return bound().value();
    }

    @Override
    public boolean isBound() {
        return tryBind().map(Holder::isBound).orElse(false);
    }

    @Override
    public boolean areComponentsBound() {
        return tryBind().map(Holder::areComponentsBound).orElse(false);
    }

    @Override
    public boolean is(Identifier id) {
        return key.identifier().equals(id);
    }

    @Override
    public boolean is(ResourceKey<R> resourceKey) {
        return key.equals(resourceKey);
    }

    @Override
    public boolean is(Predicate<ResourceKey<R>> predicate) {
        return predicate.test(key);
    }

    @Override
    public boolean is(TagKey<R> tag) {
        return bound().is(tag);
    }

    @Override
    public boolean is(Holder<R> other) {
        return other.is(key);
    }

    @Override
    public Stream<TagKey<R>> tags() {
        return bound().tags();
    }

    @Override
    public DataComponentMap components() {
        return bound().components();
    }

    @Override
    public Either<ResourceKey<R>, R> unwrap() {
        return Either.left(key);
    }

    @Override
    public Optional<ResourceKey<R>> unwrapKey() {
        return Optional.of(key);
    }

    @Override
    public Kind kind() {
        return Kind.REFERENCE;
    }

    @Override
    public boolean canSerializeIn(HolderOwner<R> owner) {
        return bound().canSerializeIn(owner);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof Holder<?> other && other.kind() == Kind.REFERENCE
                && other.unwrapKey().map(key::equals).orElse(false);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return "DeferredHolder{" + key + "}";
    }
}
