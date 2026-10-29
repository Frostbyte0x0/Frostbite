package org.exodusstudio.frostbite.common.attachment;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public final class DataAttachment<T> {
    private final Identifier id;
    private final Supplier<T> defaultValue;
    private final MapCodec<T> serializer;
    private final @Nullable StreamCodec<? super RegistryFriendlyByteBuf, T> syncCodec;
    private final boolean copyOnDeath;

    private DataAttachment(Identifier id, Builder<T> builder) {
        this.id = id;
        this.defaultValue = builder.defaultValue;
        this.serializer = builder.serializer;
        this.syncCodec = builder.syncCodec;
        this.copyOnDeath = builder.copyOnDeath;
    }

    public static <T> Builder<T> builder(Supplier<T> defaultValue) {
        return new Builder<>(defaultValue);
    }

    public Identifier id() {
        return id;
    }

    public Supplier<T> defaultValue() {
        return defaultValue;
    }

    public MapCodec<T> serializer() {
        return serializer;
    }

    public @Nullable StreamCodec<? super RegistryFriendlyByteBuf, T> syncCodec() {
        return syncCodec;
    }

    public boolean copyOnDeath() {
        return copyOnDeath;
    }

    public static class Builder<T> {
        private final Supplier<T> defaultValue;
        private MapCodec<T> serializer;
        private @Nullable StreamCodec<? super RegistryFriendlyByteBuf, T> syncCodec;
        private boolean copyOnDeath;

        private Builder(Supplier<T> defaultValue) {
            this.defaultValue = defaultValue;
        }

        public Builder<T> serialize(MapCodec<T> serializer) {
            this.serializer = serializer;
            return this;
        }

        public Builder<T> sync(StreamCodec<? super RegistryFriendlyByteBuf, T> syncCodec) {
            this.syncCodec = syncCodec;
            return this;
        }

        public Builder<T> copyOnDeath() {
            this.copyOnDeath = true;
            return this;
        }

        public Builder<T> build() {
            return this;
        }
    }

    public static class Register {
        private final String namespace;
        private final List<DataAttachment<?>> entries = new ArrayList<>();

        public Register(String namespace) {
            this.namespace = namespace;
        }

        public <T> DataAttachment<T> register(String name, Supplier<Builder<T>> builder) {
            DataAttachment<T> attachment = new DataAttachment<>(Identifier.fromNamespaceAndPath(namespace, name), builder.get());
            entries.add(attachment);
            return attachment;
        }

        public List<DataAttachment<?>> getEntries() {
            return Collections.unmodifiableList(entries);
        }
    }
}
