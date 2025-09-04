package org.exodusstudio.frostbite.common.block;

import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;

import java.util.Optional;

public class FrostbitePortalBlock extends Block implements Portal {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    protected static final VoxelShape X_AXIS_AABB = Block.box(0.0, 0.0, 6.0, 16.0, 16.0, 10.0);
    protected static final VoxelShape Z_AXIS_AABB = Block.box(6.0, 0.0, 0.0, 10.0, 16.0, 16.0);

    public FrostbitePortalBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(AXIS) == Direction.Axis.Z) {
            return Z_AXIS_AABB;
        }
        return X_AXIS_AABB;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier insideBlockEffectApplier) {
        if (entity.canUsePortal(false)) {
            entity.setAsInsidePortal(this, pos);
        }
    }

    @Override
    public int getPortalTransitionTime(ServerLevel serverLevel, Entity entity) {
        return entity instanceof Player player
                ? Math.max(
                0,
                serverLevel.getGameRules()
                        .getInt(
                                player.getAbilities().invulnerable
                                        ? GameRules.RULE_PLAYERS_NETHER_PORTAL_CREATIVE_DELAY
                                        : GameRules.RULE_PLAYERS_NETHER_PORTAL_DEFAULT_DELAY
                        )
        )
                : 0;
    }

    @Override
    public TeleportTransition getPortalDestination(ServerLevel originLevel, Entity entity, BlockPos pos) {
        ResourceKey<Level> frostbiteKey = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "frostbite"));
        ResourceKey<Level> resourcekey = originLevel.dimension() == frostbiteKey ? Level.OVERWORLD : frostbiteKey;
        ServerLevel destinationLevel = originLevel.getServer().getLevel(resourcekey);

        if (destinationLevel == null) {
            return null;
        } else {
            boolean flag = originLevel.dimension() == Level.OVERWORLD;
            return this.getExitPortal(destinationLevel, entity, pos, flag);
        }
    }

    private TeleportTransition getExitPortal(
            ServerLevel destinationLevel, Entity entity, BlockPos pos, boolean isOriginDimensionOverworld
    ) {
        TeleportTransition.PostTeleportTransition teleporttransition$postteleporttransition;

        Holder.Reference<Structure> p_structure = getStructure(destinationLevel);

        BlockPos exitPos;
        if (isOriginDimensionOverworld) {
            try {
                Structure structure = p_structure.value();
                ChunkGenerator chunkgenerator = destinationLevel.getChunkSource().getGenerator();
                StructureStart structurestart = structure.generate(p_structure, destinationLevel.dimension(),
                        destinationLevel.getServer().registryAccess(), chunkgenerator, chunkgenerator.getBiomeSource(),
                        destinationLevel.getChunkSource().randomState(), destinationLevel.getStructureManager(), destinationLevel.getSeed(),
                        new ChunkPos(pos), 0, destinationLevel, (p_214580_) -> true);
                BoundingBox boundingbox = structurestart.getBoundingBox();
                ChunkPos chunkpos = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.minX()),
                        SectionPos.blockToSectionCoord(boundingbox.minZ()));
                ChunkPos chunkpos1 = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.maxX()),
                        SectionPos.blockToSectionCoord(boundingbox.maxZ()));
                ChunkPos.rangeClosed(chunkpos, chunkpos1).forEach((chunkPos) -> structurestart
                        .placeInChunk(destinationLevel, destinationLevel.structureManager(), chunkgenerator, destinationLevel.getRandom(),
                                new BoundingBox(chunkPos.getMinBlockX(), destinationLevel.getMinY(), chunkPos.getMinBlockZ(),
                                        chunkPos.getMaxBlockX(), destinationLevel.getMaxY() + 1, chunkPos.getMaxBlockZ()), chunkPos));
            } catch (IllegalStateException ignored) {}
            exitPos = Frostbite.frostbiteSpawnPoint.offset(0, -2, 0);
        } else {
            exitPos = Frostbite.overworldSpawnPoint.offset(0, -2, 0);
        }

        BlockPos finalExitPos = exitPos;
        teleporttransition$postteleporttransition = TeleportTransition.PLAY_PORTAL_SOUND
                .then(p_351967_ -> p_351967_.placePortalTicket(finalExitPos));

        return getDimensionTransitionFromExit(entity, pos, exitPos, destinationLevel, teleporttransition$postteleporttransition);
    }

    private static TeleportTransition getDimensionTransitionFromExit(
            Entity entity, BlockPos pos, BlockPos blockpos, ServerLevel level, TeleportTransition.PostTeleportTransition postTeleportTransition
    ) {
        BlockState blockstate = entity.level().getBlockState(pos);
        Direction.Axis direction$axis;
        Vec3 vec3;
        if (blockstate.hasProperty(BlockStateProperties.HORIZONTAL_AXIS)) {
            direction$axis = blockstate.getValue(BlockStateProperties.HORIZONTAL_AXIS);
            BlockUtil.FoundRectangle blockutil$foundrectangle = BlockUtil.getLargestRectangleAround(
                    pos, direction$axis, 21, Direction.Axis.Y, 21, p_351016_ -> entity.level().getBlockState(p_351016_) == blockstate
            );
            vec3 = entity.getRelativePortalPosition(direction$axis, blockutil$foundrectangle);
        } else {
            direction$axis = Direction.Axis.X;
            vec3 = new Vec3(0.5, 0.0, 0.0);
        }

        return createDimensionTransition(level, blockpos, direction$axis, vec3, entity, postTeleportTransition);
    }

    private static TeleportTransition createDimensionTransition(
            ServerLevel level,
            BlockPos blockpos,
            Direction.Axis axis,
            Vec3 offset,
            Entity entity,
            TeleportTransition.PostTeleportTransition postTeleportTransition
    ) {
        BlockState blockstate = level.getBlockState(blockpos);
        Direction.Axis direction$axis = blockstate.getOptionalValue(BlockStateProperties.HORIZONTAL_AXIS).orElse(Direction.Axis.X);
        EntityDimensions entitydimensions = entity.getDimensions(entity.getPose());
        int i = axis == direction$axis ? 0 : 90;
        double d2 = (double)entitydimensions.width() / 2.0 + - (double)entitydimensions.width() * offset.x();
        double d3 = (double)entitydimensions.height() * offset.y();
        double d4 = 0.5 + offset.z();
        boolean flag = direction$axis == Direction.Axis.X;
        Vec3 vec3 = new Vec3((double)blockpos.getX() + (flag ? d2 : d4), (double)blockpos.getY() + d3, (double)blockpos.getZ() + (flag ? d4 : d2));
        Vec3 vec31 = PortalShape.findCollisionFreePosition(vec3, level, entity, entitydimensions);
        return new TeleportTransition(level, vec31, Vec3.ZERO, (float)i, 0.0F, Relative.union(Relative.DELTA, Relative.ROTATION), postTeleportTransition);
    }

    @Override
    public Portal.Transition getLocalTransition() {
        return Portal.Transition.CONFUSION;
    }

    @Override
    public void animateTick(BlockState p_221794_, Level p_221795_, BlockPos p_221796_, RandomSource p_221797_) {
        if (p_221797_.nextInt(100) == 0) {
            p_221795_.playLocalSound(
                    (double)p_221796_.getX() + 0.5,
                    (double)p_221796_.getY() + 0.5,
                    (double)p_221796_.getZ() + 0.5,
                    SoundEvents.PORTAL_AMBIENT,
                    SoundSource.BLOCKS,
                    0.5F,
                    p_221797_.nextFloat() * 0.4F + 0.8F,
                    false
            );
        }

        for (int i = 0; i < 4; i++) {
            double d0 = (double)p_221796_.getX() + p_221797_.nextDouble();
            double d1 = (double)p_221796_.getY() + p_221797_.nextDouble();
            double d2 = (double)p_221796_.getZ() + p_221797_.nextDouble();
            double d3 = ((double)p_221797_.nextFloat() - 0.5) * 0.5;
            double d4 = ((double)p_221797_.nextFloat() - 0.5) * 0.5;
            double d5 = ((double)p_221797_.nextFloat() - 0.5) * 0.5;
            int j = p_221797_.nextInt(2) * 2 - 1;
            if (!p_221795_.getBlockState(p_221796_.west()).is(this) && !p_221795_.getBlockState(p_221796_.east()).is(this)) {
                d0 = p_221796_.getX() + 0.5 + 0.25 * (double)j;
                d3 = (p_221797_.nextFloat() * 2.0F * (float)j);
            } else {
                d2 = p_221796_.getZ() + 0.5 + 0.25 * (double)j;
                d5 = (p_221797_.nextFloat() * 2.0F * (float)j);
            }

            p_221795_.addParticle(ParticleRegistry.SNOWFLAKE_PARTICLE.get(), d0, d1, d2, d3, d4, d5);
        }
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rot) {
        return switch (rot) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (state.getValue(AXIS)) {
                case Z -> state.setValue(AXIS, Direction.Axis.X);
                case X -> state.setValue(AXIS, Direction.Axis.Z);
                default -> state;
            };
            default -> state;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    public static Holder.Reference<Structure> getStructure(ServerLevel level) {
        ResourceKey<?> resourcekey = ResourceKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "fto_portal"));
        Optional<ResourceKey<Structure>> optional = resourcekey.cast(Registries.STRUCTURE);
        ResourceKey<Structure> structureKey = optional.orElseThrow();
        return level.getServer().registryAccess().lookupOrThrow(Registries.STRUCTURE).get(structureKey).orElse(null);
    }
}
