package org.exodusstudio.frostbite.common.registry.deferred;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public class DeferredBlock<T extends Block> extends DeferredHolder<Block, T> implements ItemLike {
    protected DeferredBlock(ResourceKey<Block> key) {
        super(key);
    }

    public static <T extends Block> DeferredBlock<T> createBlock(Identifier id) {
        return new DeferredBlock<>(ResourceKey.create(Registries.BLOCK, id));
    }

    @Override
    public Item asItem() {
        return get().asItem();
    }
}
