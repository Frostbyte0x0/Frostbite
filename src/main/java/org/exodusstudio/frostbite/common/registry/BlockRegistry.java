package org.exodusstudio.frostbite.common.registry;

import org.exodusstudio.frostbite.Frostbite;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BlockRegistry {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Frostbite.MOD_ID);

    public static final DeferredBlock<Block> BLACK_BLOCK = registerBLock("black_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(10f)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "black_block")))), "black_block");
    
    public static <T extends Block> DeferredBlock<T> registerBLock(String name, Supplier<T> block, String path) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, path);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block, String path) {
        ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, path)))));
    }
}
