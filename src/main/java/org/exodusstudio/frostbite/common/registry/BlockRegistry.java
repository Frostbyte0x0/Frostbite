package org.exodusstudio.frostbite.common.registry;

import net.minecraft.world.level.block.*;
import org.exodusstudio.frostbite.Frostbite;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.common.block.HeaterBlock;
import org.exodusstudio.frostbite.common.block.HoarfrostWoodBlock;
import org.exodusstudio.frostbite.common.block.WeavingTable;
import org.exodusstudio.frostbite.common.worldgen.tree.ModTreeGrowers;

import java.util.function.Supplier;

public class BlockRegistry {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Frostbite.MOD_ID);


    public static final DeferredBlock<Block> BLACK_BLOCK = registerBLock("black_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(10f)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "black_block")))));

    public static final DeferredBlock<Block> PERMAFROZEN_DIRT = registerBLock("permafrozen_dirt",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "permafrozen_dirt")))));
    public static final DeferredBlock<Block> PERMAFROZEN_GRASS = registerBLock("permafrozen_grass",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "permafrozen_grass")))));
    public static final DeferredBlock<Block> PETRIFIED_ICE = registerBLock("petrified_ice",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "petrified_ice")))));

    // MISTY WOOD
    public static final DeferredBlock<Block> MISTY_LOG = registerBLock("misty_log",
            () -> new HoarfrostWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "misty_log")))));
    public static final DeferredBlock<Block> MISTY_WOOD = registerBLock("misty_wood",
            () -> new HoarfrostWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "misty_wood")))));
    public static final DeferredBlock<Block> STRIPPED_MISTY_LOG = registerBLock("stripped_misty_log",
            () -> new HoarfrostWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "stripped_misty_log")))));
    public static final DeferredBlock<Block> STRIPPED_MISTY_WOOD = registerBLock("stripped_misty_wood",
            () -> new HoarfrostWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "stripped_misty_wood")))));
    public static final DeferredBlock<Block> MISTY_PLANKS = registerBLock("misty_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "misty_planks")))));
    public static final DeferredBlock<Block> MISTY_LEAVES = registerBLock("misty_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "misty_leaves")))));
    public static final DeferredBlock<Block> MISTY_SAPLING = registerBLock("misty_sapling",
            () -> new SaplingBlock(ModTreeGrowers.MISTY, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).noCollission()
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "misty_sapling")))));

    // DIM WOOD
    public static final DeferredBlock<Block> DIM_LOG = registerBLock("dim_log",
            () -> new HoarfrostWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "dim_log")))));
    public static final DeferredBlock<Block> DIM_WOOD = registerBLock("dim_wood",
            () -> new HoarfrostWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "dim_wood")))));
    public static final DeferredBlock<Block> STRIPPED_DIM_LOG = registerBLock("stripped_dim_log",
            () -> new HoarfrostWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "stripped_dim_log")))));
    public static final DeferredBlock<Block> STRIPPED_DIM_WOOD = registerBLock("stripped_dim_wood",
            () -> new HoarfrostWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "stripped_dim_wood")))));
    public static final DeferredBlock<Block> DIM_PLANKS = registerBLock("dim_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "dim_planks")))));
    public static final DeferredBlock<Block> DIM_LEAVES = registerBLock("dim_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "dim_leaves")))));
    public static final DeferredBlock<Block> DIM_SAPLING = registerBLock("dim_sapling",
            () -> new SaplingBlock(ModTreeGrowers.DIM, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).noCollission()
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "dim_sapling")))));

    // SILVER WOOD
    public static final DeferredBlock<Block> SILVER_LOG = registerBLock("silver_log",
            () -> new HoarfrostWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "silver_log")))));
    public static final DeferredBlock<Block> SILVER_WOOD = registerBLock("silver_wood",
            () -> new HoarfrostWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "silver_wood")))));
    public static final DeferredBlock<Block> STRIPPED_SILVER_LOG = registerBLock("stripped_silver_log",
            () -> new HoarfrostWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "stripped_silver_log")))));
    public static final DeferredBlock<Block> STRIPPED_SILVER_WOOD = registerBLock("stripped_silver_wood",
            () -> new HoarfrostWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "stripped_silver_wood")))));
    public static final DeferredBlock<Block> SILVER_PLANKS = registerBLock("silver_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "silver_planks")))));
    public static final DeferredBlock<Block> SILVER_LEAVES = registerBLock("silver_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "silver_leaves")))));
    public static final DeferredBlock<Block> SILVER_SAPLING = registerBLock("silver_sapling",
            () -> new SaplingBlock(ModTreeGrowers.SILVER, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).noCollission()
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "silver_sapling")))));

    // CHARM WOOD
    public static final DeferredBlock<Block> CHARM_LOG = registerBLock("charm_log",
            () -> new HoarfrostWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "charm_log")))));
    public static final DeferredBlock<Block> CHARM_WOOD = registerBLock("charm_wood",
            () -> new HoarfrostWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "charm_wood")))));
    public static final DeferredBlock<Block> STRIPPED_CHARM_LOG = registerBLock("stripped_charm_log",
            () -> new HoarfrostWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "stripped_charm_log")))));
    public static final DeferredBlock<Block> STRIPPED_CHARM_WOOD = registerBLock("stripped_charm_wood",
            () -> new HoarfrostWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "stripped_charm_wood")))));
    public static final DeferredBlock<Block> CHARM_PLANKS = registerBLock("charm_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "charm_planks")))));
    public static final DeferredBlock<Block> CHARM_LEAVES = registerBLock("charm_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "charm_leaves")))));
    public static final DeferredBlock<Block> CHARM_SAPLING = registerBLock("charm_sapling",
            () -> new SaplingBlock(ModTreeGrowers.CHARM, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).noCollission()
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "charm_sapling")))));

    // SHINNING CEDAR WOOD
    public static final DeferredBlock<Block> SHINNING_CEDAR_LOG = registerBLock("shinning_cedar_log",
            () -> new HoarfrostWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "shinning_cedar_log")))));
    public static final DeferredBlock<Block> SHINNING_CEDAR_WOOD = registerBLock("shinning_cedar_wood",
            () -> new HoarfrostWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "shinning_cedar_wood")))));
    public static final DeferredBlock<Block> STRIPPED_SHINNING_CEDAR_LOG = registerBLock("stripped_shinning_cedar_log",
            () -> new HoarfrostWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "stripped_shinning_cedar_log")))));
    public static final DeferredBlock<Block> STRIPPED_SHINNING_CEDAR_WOOD = registerBLock("stripped_shinning_cedar_wood",
            () -> new HoarfrostWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "stripped_shinning_cedar_wood")))));
    public static final DeferredBlock<Block> SHINNING_CEDAR_PLANKS = registerBLock("shinning_cedar_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "shinning_cedar_planks")))));
    public static final DeferredBlock<Block> SHINNING_CEDAR_LEAVES = registerBLock("shinning_cedar_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).lightLevel(p -> 10)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "shinning_cedar_leaves")))));
    public static final DeferredBlock<Block> SHINNING_CEDAR_SAPLING = registerBLock("shinning_cedar_sapling",
            () -> new SaplingBlock(ModTreeGrowers.CHARM, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).noCollission()
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "shinning_cedar_sapling")))));

    public static final DeferredBlock<HorizontalDirectionalBlock> WEAVING_TABLE = registerBLock("weaving_table",
            () -> new WeavingTable(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "weaving_table")))));

    public static final DeferredBlock<Block> SMALL_HEATER_BLOCK = registerBLock("small_heater",
            () -> new HeaterBlock(BlockBehaviour.Properties.of()
                    .strength(10f)
                    .lightLevel(state -> 5)
                    .setId(ResourceKey.create(Registries.BLOCK,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "small_heater"))), 6, 5));
    public static final DeferredBlock<Block> MEDIUM_HEATER_BLOCK = registerBLock("medium_heater",
            () -> new HeaterBlock(BlockBehaviour.Properties.of()
                    .strength(10f)
                    .lightLevel(state -> 10)
                    .setId(ResourceKey.create(Registries.BLOCK,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "medium_heater"))), 12, 10));
    public static final DeferredBlock<Block> BIG_HEATER_BLOCK = registerBLock("big_heater",
            () -> new HeaterBlock(BlockBehaviour.Properties.of()
                    .strength(10f)
                    .lightLevel(state -> 15)
                    .setId(ResourceKey.create(Registries.BLOCK,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "big_heater"))), 20, 10));


    public static <T extends Block> DeferredBlock<T> registerBLock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, name)))));
    }
}
