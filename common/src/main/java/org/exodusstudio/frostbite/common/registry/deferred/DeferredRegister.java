package org.exodusstudio.frostbite.common.registry.deferred;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@SuppressWarnings("unchecked")
public class DeferredRegister<T> {
    private final dev.architectury.registry.registries.DeferredRegister<T> register;
    protected final ResourceKey<? extends Registry<T>> registryKey;
    protected final String namespace;
    private final List<DeferredHolder<T, ? extends T>> entries = new ArrayList<>();

    protected DeferredRegister(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
        this.register = dev.architectury.registry.registries.DeferredRegister.create(namespace, (ResourceKey<Registry<T>>) registryKey);
        this.registryKey = registryKey;
        this.namespace = namespace;
    }

    public static <T> DeferredRegister<T> create(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
        return new DeferredRegister<>(registryKey, namespace);
    }

    public static <T> DeferredRegister<T> create(Registry<T> registry, String namespace) {
        return new DeferredRegister<>(registry.key(), namespace);
    }

    public static Items createItems(String namespace) {
        return new Items(namespace);
    }

    public static Blocks createBlocks(String namespace) {
        return new Blocks(namespace);
    }

    public static DataComponents createDataComponents(ResourceKey<Registry<DataComponentType<?>>> registryKey, String namespace) {
        return new DataComponents(registryKey, namespace);
    }

    public <I extends T> DeferredHolder<T, I> register(String name, Supplier<? extends I> supplier) {
        return register(name, id -> supplier.get());
    }

    public <I extends T> DeferredHolder<T, I> register(String name, Function<Identifier, ? extends I> factory) {
        Identifier id = Identifier.fromNamespaceAndPath(namespace, name);
        register.register(id, () -> factory.apply(id));
        DeferredHolder<T, I> holder = createHolder(registryKey, id);
        entries.add(holder);
        return holder;
    }

    protected <I extends T> DeferredHolder<T, I> createHolder(ResourceKey<? extends Registry<T>> registryKey, Identifier id) {
        return DeferredHolder.create(registryKey, id);
    }

    public Collection<DeferredHolder<T, ? extends T>> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    public void register() {
        register.register();
    }

    public static class Items extends DeferredRegister<Item> {
        protected Items(String namespace) {
            super(Registries.ITEM, namespace);
        }

        @Override
        public <I extends Item> DeferredItem<I> register(String name, Supplier<? extends I> supplier) {
            return (DeferredItem<I>) super.register(name, supplier);
        }

        @Override
        public <I extends Item> DeferredItem<I> register(String name, Function<Identifier, ? extends I> factory) {
            return (DeferredItem<I>) super.register(name, factory);
        }

        @Override
        protected <I extends Item> DeferredItem<I> createHolder(ResourceKey<? extends Registry<Item>> registryKey, Identifier id) {
            return DeferredItem.createItem(id);
        }
    }

    public static class Blocks extends DeferredRegister<Block> {
        protected Blocks(String namespace) {
            super(Registries.BLOCK, namespace);
        }

        @Override
        public <B extends Block> DeferredBlock<B> register(String name, Supplier<? extends B> supplier) {
            return (DeferredBlock<B>) super.register(name, supplier);
        }

        @Override
        public <B extends Block> DeferredBlock<B> register(String name, Function<Identifier, ? extends B> factory) {
            return (DeferredBlock<B>) super.register(name, factory);
        }

        @Override
        protected <B extends Block> DeferredBlock<B> createHolder(ResourceKey<? extends Registry<Block>> registryKey, Identifier id) {
            return DeferredBlock.createBlock(id);
        }
    }

    public static class DataComponents extends DeferredRegister<DataComponentType<?>> {
        protected DataComponents(ResourceKey<Registry<DataComponentType<?>>> registryKey, String namespace) {
            super(registryKey, namespace);
        }

        public <D> DeferredHolder<DataComponentType<?>, DataComponentType<D>> registerComponentType(String name, UnaryOperator<DataComponentType.Builder<D>> builder) {
            return register(name, () -> builder.apply(DataComponentType.builder()).build());
        }
    }
}
