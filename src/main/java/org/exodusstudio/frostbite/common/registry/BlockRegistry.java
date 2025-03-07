package org.exodusstudio.frostbite.common.registry;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
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

    public static final DeferredBlock<Block> HOARFROST_LOG = registerBLock("hoarfrost_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "hoarfrost_log")))), "hoarfrost_log");
    public static final DeferredBlock<Block> HOARFROST_WOOD = registerBLock("hoarfrost_wood",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "hoarfrost_wood")))), "hoarfrost_wood");
    public static final DeferredBlock<Block> STRIPPED_HOARFROST_LOG = registerBLock("stripped_hoarfrost_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "stripped_hoarfrost_log")))), "stripped_hoarfrost_log");
    public static final DeferredBlock<Block> STRIPPED_HOARFROST_WOOD = registerBLock("stripped_hoarfrost_wood",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "stripped_hoarfrost_wood")))), "stripped_hoarfrost_wood");
    
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
