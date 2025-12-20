package org.exodusstudio.frostbite.common.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.StructureRegistry;

import java.util.Optional;
import java.util.function.Predicate;

public class FTOPortal extends Structure {
    public static final MapCodec<FTOPortal> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(FTOPortal.settingsCodec(instance),
                    StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
                    Identifier.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.startJigsawName),
                    Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.size),
                    HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight),
                    Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap),
                    JigsawStructure.MaxDistance.CODEC.fieldOf("max_distance_from_center").forGetter(p_432727_ -> p_432727_.maxDistanceFromCenter),
                    DimensionPadding.CODEC.optionalFieldOf("dimension_padding", JigsawStructure.DEFAULT_DIMENSION_PADDING).forGetter(structure -> structure.dimensionPadding),
                    LiquidSettings.CODEC.optionalFieldOf("liquid_settings", JigsawStructure.DEFAULT_LIQUID_SETTINGS).forGetter(structure -> structure.liquidSettings)
            ).apply(instance, FTOPortal::new));

    private final Holder<StructureTemplatePool> startPool;
    private final Optional<Identifier> startJigsawName;
    private final int size;
    private final HeightProvider startHeight;
    private final Optional<Heightmap.Types> projectStartToHeightmap;
    private final JigsawStructure.MaxDistance maxDistanceFromCenter;
    private final DimensionPadding dimensionPadding;
    private final LiquidSettings liquidSettings;
    public static int count = 0;
    public static boolean canSpawn = true;

    public FTOPortal(Structure.StructureSettings config,
                     Holder<StructureTemplatePool> startPool,
                     Optional<Identifier> startJigsawName,
                     int size,
                     HeightProvider startHeight,
                     Optional<Heightmap.Types> projectStartToHeightmap,
                     JigsawStructure.MaxDistance maxDistanceFromCenter,
                     DimensionPadding dimensionPadding,
                     LiquidSettings liquidSettings)
    {
        super(config);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.size = size;
        this.startHeight = startHeight;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
        this.dimensionPadding = dimensionPadding;
        this.liquidSettings = liquidSettings;
    }

    @Override
    public void afterPlace(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, PiecesContainer pieces) {
        super.afterPlace(level, structureManager, chunkGenerator, random, boundingBox, chunkPos, pieces);
        Frostbite.frostbiteSpawnPoint = pieces.calculateBoundingBox().getCenter();
    }

    @Override
    public StructureStart generate(
            Holder<Structure> structure,
            ResourceKey<Level> level,
            RegistryAccess registryAccess,
            ChunkGenerator chunkGenerator,
            BiomeSource biomeSource,
            RandomState randomState,
            StructureTemplateManager structureTemplateManager,
            long seed,
            ChunkPos chunkPos,
            int references,
            LevelHeightAccessor heightAccessor,
            Predicate<Holder<Biome>> validBiome
    ) {
        StructureStart structureStart = super.generate(structure, level, registryAccess, chunkGenerator, biomeSource,
                randomState, structureTemplateManager, seed, new ChunkPos(0, 0), references, heightAccessor, validBiome);

        if (!canSpawn) {//count > 1) {
            return StructureStart.INVALID_START;
        }

        if (!structureStart.equals(StructureStart.INVALID_START)) {
            //count++;
            structureStart.getPieces().forEach(p -> p.setOrientation(Direction.NORTH));
            Frostbite.frostbiteSpawnPoint = structureStart.getPieces().getFirst().getLocatorPosition();
            canSpawn = false;
        }

        return structureStart;
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        if (!canSpawn) {//count > 0) {
            return Optional.empty();
        }

        int startY = this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));

        BlockPos blockPos = new BlockPos(0, startY, 0);

        return JigsawPlacement.addPieces(
                context,
                this.startPool,
                this.startJigsawName,
                this.size,
                blockPos,
                false,
                this.projectStartToHeightmap,
                this.maxDistanceFromCenter,
                PoolAliasLookup.EMPTY,
                this.dimensionPadding,
                this.liquidSettings);
    }

    @Override
    public StructureType<?> type() {
        return StructureRegistry.FTO_PORTAL.get();
    }
}
