package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.block.*;
import org.exodusstudio.frostbite.common.worldgen.tree.TreeGrowers;

import java.util.function.Supplier;

public class BlockRegistry {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Frostbite.MOD_ID);


    public static final DeferredBlock<Block> BLACK_BLOCK = registerBlock("black_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(10f)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "black_block")))));



    public static final DeferredBlock<Block> REINFORCED_BLACK_ICE = registerBlock("reinforced_black_ice",
            () -> new Block(BlockBehaviour.Properties.of().strength(-1f)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "reinforced_black_ice")))));
    public static final DeferredBlock<Block> REINFORCED_BLACK_ICE_RECEPTACLE = registerBlock("reinforced_black_ice_receptacle",
            () -> new ReinforcedBlackIceReceptacleBlock(BlockBehaviour.Properties.of().strength(-1f)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "reinforced_black_ice_receptacle")))));
    public static final DeferredBlock<Block> FROSTBITE_PORTAL = registerBlock("frostbite_portal",
            () -> new FrostbitePortalBlock(BlockBehaviour.Properties.of()
                    .noCollission()
                    .randomTicks()
                    .strength(-1.0F)
                    .sound(SoundType.GLASS)
                    .lightLevel(p_50884_ -> 11)
                    .pushReaction(PushReaction.BLOCK)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "frostbite_portal")))));


    public static final DeferredBlock<Block> PERMAFROZEN_DIRT = registerBlock("permafrozen_dirt",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).strength(1f)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "permafrozen_dirt")))));

    public static final DeferredBlock<Block> SLATED_ICE = registerBlock("slated_ice",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "slated_ice")))));
    public static final DeferredBlock<Block> CHISELED_SLATED_ICE = registerBlock("chiseled_slated_ice",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_DEEPSLATE)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "chiseled_slated_ice")))));
    public static final DeferredBlock<Block> COBBLED_SLATED_ICE = registerBlock("cobbled_slated_ice",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "cobbled_slated_ice")))));
    public static final DeferredBlock<Block> CRACKED_SLATED_ICE_TILES = registerBlock("cracked_slated_ice_tiles",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CRACKED_DEEPSLATE_TILES)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "cracked_slated_ice_tiles")))));
    public static final DeferredBlock<Block> POLISHED_SLATED_ICE = registerBlock("polished_slated_ice",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_DEEPSLATE)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "polished_slated_ice")))));
    public static final DeferredBlock<Block> SLATED_ICE_BRICKS = registerBlock("slated_ice_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICKS)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "slated_ice_bricks")))));
    public static final DeferredBlock<Block> SLATED_ICE_TILES = registerBlock("slated_ice_tiles",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_TILES)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "slated_ice_tiles")))));

    public static final DeferredBlock<Block> MARBLE = registerBlock("marble",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "marble")))));
    public static final DeferredBlock<Block> CHISELED_MARBLE = registerBlock("chiseled_marble",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_QUARTZ_BLOCK)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "chiseled_marble")))));
    public static final DeferredBlock<Block> MARBLE_BRICKS = registerBlock("marble_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BRICKS)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "marble_bricks")))));
    public static final DeferredBlock<Block> MARBLE_PILLAR = registerBlock("marble_pillar",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_PILLAR)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "marble_pillar")))));


    // MISTY WOOD
    public static final DeferredBlock<Block> MISTY_LOG = registerBlock("misty_log",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "misty_log")))));
    public static final DeferredBlock<Block> MISTY_WOOD = registerBlock("misty_wood",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "misty_wood")))));
    public static final DeferredBlock<Block> STRIPPED_MISTY_LOG = registerBlock("stripped_misty_log",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "stripped_misty_log")))));
    public static final DeferredBlock<Block> STRIPPED_MISTY_WOOD = registerBlock("stripped_misty_wood",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "stripped_misty_wood")))));
    public static final DeferredBlock<Block> MISTY_PLANKS = registerBlock("misty_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "misty_planks")))));
    public static final DeferredBlock<Block> MISTY_STAIRS = registerBlock("misty_stairs",
            () -> new StairBlock(MISTY_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "misty_stairs")))));
    public static final DeferredBlock<Block> MISTY_SLAB = registerBlock("misty_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "misty_slab")))));
    public static final DeferredBlock<Block> MISTY_FENCE = registerBlock("misty_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "misty_fence")))));
    public static final DeferredBlock<Block> MISTY_FENCE_GATE = registerBlock("misty_fence_gate",
            () -> new FenceGateBlock(WoodType.ACACIA, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "misty_fence_gate")))));
    public static final DeferredBlock<Block> MISTY_DOOR = registerBlock("misty_door",
            () -> new DoorBlock(BlockSetType.ACACIA, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "misty_door")))));
    public static final DeferredBlock<Block> MISTY_TRAPDOOR = registerBlock("misty_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.ACACIA, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "misty_trapdoor")))));
    public static final DeferredBlock<Block> MISTY_BUTTON = registerBlock("misty_button",
            () -> new ButtonBlock(BlockSetType.ACACIA, 30, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "misty_button")))));
    public static final DeferredBlock<Block> MISTY_LEAVES = registerBlock("misty_leaves",
            () -> new RangedLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "misty_leaves")))));
    public static final DeferredBlock<Block> MISTY_SAPLING = registerBlock("misty_sapling",
            () -> new SnowySaplingBlock(TreeGrowers.MISTY, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING).noCollission()
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "misty_sapling")))));
    public static final DeferredBlock<Block> MISTY_SIGN = registerBlock("misty_sign",
            () -> new StandingSignBlock(WoodType.ACACIA, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "misty_sign")))));
    public static final DeferredBlock<Block> MISTY_HANGING_SIGN = registerBlock("misty_hanging_sign",
            () -> new WallHangingSignBlock(WoodType.ACACIA, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "misty_hanging_sign")))));

    // DIM WOOD
    public static final DeferredBlock<Block> DIM_LOG = registerBlock("dim_log",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "dim_log")))));
    public static final DeferredBlock<Block> DIM_WOOD = registerBlock("dim_wood",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "dim_wood")))));
    public static final DeferredBlock<Block> STRIPPED_DIM_LOG = registerBlock("stripped_dim_log",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "stripped_dim_log")))));
    public static final DeferredBlock<Block> STRIPPED_DIM_WOOD = registerBlock("stripped_dim_wood",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "stripped_dim_wood")))));
    public static final DeferredBlock<Block> DIM_PLANKS = registerBlock("dim_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "dim_planks")))));
    public static final DeferredBlock<Block> DIM_LEAVES = registerBlock("dim_leaves",
            () -> new RangedLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "dim_leaves")))));
    public static final DeferredBlock<Block> DIM_SAPLING = registerBlock("dim_sapling",
            () -> new SaplingBlock(TreeGrowers.DIM, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).noCollission()
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "dim_sapling")))));

    // SILVER WOOD
    public static final DeferredBlock<Block> SILVER_LOG = registerBlock("silver_log",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "silver_log")))));
    public static final DeferredBlock<Block> SILVER_WOOD = registerBlock("silver_wood",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "silver_wood")))));
    public static final DeferredBlock<Block> STRIPPED_SILVER_LOG = registerBlock("stripped_silver_log",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "stripped_silver_log")))));
    public static final DeferredBlock<Block> STRIPPED_SILVER_WOOD = registerBlock("stripped_silver_wood",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "stripped_silver_wood")))));
    public static final DeferredBlock<Block> SILVER_PLANKS = registerBlock("silver_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "silver_planks")))));
    public static final DeferredBlock<Block> SILVER_LEAVES = registerBlock("silver_leaves",
            () -> new RangedLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "silver_leaves")))));
    public static final DeferredBlock<Block> SILVER_SAPLING = registerBlock("silver_sapling",
            () -> new SaplingBlock(TreeGrowers.SILVER, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).noCollission()
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "silver_sapling")))));

    // CHARM WOOD
    public static final DeferredBlock<Block> CHARM_LOG = registerBlock("charm_log",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "charm_log")))));
    public static final DeferredBlock<Block> CHARM_WOOD = registerBlock("charm_wood",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "charm_wood")))));
    public static final DeferredBlock<Block> STRIPPED_CHARM_LOG = registerBlock("stripped_charm_log",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "stripped_charm_log")))));
    public static final DeferredBlock<Block> STRIPPED_CHARM_WOOD = registerBlock("stripped_charm_wood",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "stripped_charm_wood")))));
    public static final DeferredBlock<Block> CHARM_PLANKS = registerBlock("charm_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "charm_planks")))));
    public static final DeferredBlock<Block> CHARM_LEAVES = registerBlock("charm_leaves",
            () -> new RangedLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "charm_leaves")))));
    public static final DeferredBlock<Block> FLOWERING_CHARM_LEAVES = registerBlock("flowering_charm_leaves",
            () -> new RangedLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "flowering_charm_leaves")))));
    public static final DeferredBlock<Block> CHARM_SAPLING = registerBlock("charm_sapling",
            () -> new SaplingBlock(TreeGrowers.CHARM, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).noCollission()
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "charm_sapling")))));

    // MOSSY CHARM WOOD
    public static final DeferredBlock<Block> MOSSY_CHARM_LOG = registerBlock("mossy_charm_log",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "mossy_charm_log")))));
    public static final DeferredBlock<Block> MOSSY_CHARM_WOOD = registerBlock("mossy_charm_wood",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "mossy_charm_wood")))));
    public static final DeferredBlock<Block> MOSSY_STRIPPED_CHARM_LOG = registerBlock("mossy_stripped_charm_log",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "mossy_stripped_charm_log")))));
    public static final DeferredBlock<Block> MOSSY_STRIPPED_CHARM_WOOD = registerBlock("mossy_stripped_charm_wood",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "mossy_stripped_charm_wood")))));
    public static final DeferredBlock<Block> MOSSY_CHARM_PLANKS = registerBlock("mossy_charm_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "mossy_charm_planks")))));

    // SHINNING CEDAR WOOD
    public static final DeferredBlock<Block> SHINNING_CEDAR_LOG = registerBlock("shinning_cedar_log",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "shinning_cedar_log")))));
    public static final DeferredBlock<Block> SHINNING_CEDAR_WOOD = registerBlock("shinning_cedar_wood",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "shinning_cedar_wood")))));
    public static final DeferredBlock<Block> STRIPPED_SHINNING_CEDAR_LOG = registerBlock("stripped_shinning_cedar_log",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "stripped_shinning_cedar_log")))));
    public static final DeferredBlock<Block> STRIPPED_SHINNING_CEDAR_WOOD = registerBlock("stripped_shinning_cedar_wood",
            () -> new FrostbiteWoodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "stripped_shinning_cedar_wood")))));
    public static final DeferredBlock<Block> SHINNING_CEDAR_PLANKS = registerBlock("shinning_cedar_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "shinning_cedar_planks")))));
    public static final DeferredBlock<Block> SHINNING_CEDAR_LEAVES = registerBlock("shinning_cedar_leaves",
            () -> new RangedLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).lightLevel(p -> 10)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "shinning_cedar_leaves")))));
    public static final DeferredBlock<Block> SHINNING_CEDAR_SAPLING = registerBlock("shinning_cedar_sapling",
            () -> new SaplingBlock(TreeGrowers.CHARM, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).noCollission()
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "shinning_cedar_sapling")))));

    public static final DeferredBlock<HorizontalDirectionalBlock> WEAVING_TABLE = registerBlock("weaving_table",
            () -> new WeavingTable(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "weaving_table")))));

    public static final DeferredBlock<Block> SMALL_HEATER_BLOCK = registerBlock("small_heater",
            () -> new HeaterBlock(BlockBehaviour.Properties.of()
                    .strength(10f)
                    .lightLevel(state -> 5)
                    .setId(ResourceKey.create(Registries.BLOCK,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "small_heater"))), 6, 5));
    public static final DeferredBlock<Block> MEDIUM_HEATER_BLOCK = registerBlock("medium_heater",
            () -> new HeaterBlock(BlockBehaviour.Properties.of()
                    .strength(10f)
                    .lightLevel(state -> 10)
                    .setId(ResourceKey.create(Registries.BLOCK,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "medium_heater"))), 12, 10));
    public static final DeferredBlock<Block> BIG_HEATER_BLOCK = registerBlock("big_heater",
            () -> new HeaterBlock(BlockBehaviour.Properties.of()
                    .strength(10f)
                    .lightLevel(state -> 15)
                    .setId(ResourceKey.create(Registries.BLOCK,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "big_heater"))), 20, 10));

    public static final DeferredBlock<Block> LODESTAR = registerBlock("lodestar",
            () -> new LodestarBlock(BlockBehaviour.Properties.of().strength(10f).noOcclusion()
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "lodestar")))));



    public static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, name)))));
    }
}
