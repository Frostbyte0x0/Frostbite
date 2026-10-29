package org.exodusstudio.frostbite.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class BrazierBlock extends Block {
    private final int fireDamage;

    public BrazierBlock(Properties properties, int fireDamage) {
        super(properties);
        this.fireDamage = fireDamage;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(BlockStateProperties.LIT, true));
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState onState, Entity entity) {
        if (onState.getValue(BlockStateProperties.LIT) && entity instanceof LivingEntity && level instanceof ServerLevel l) {
            entity.hurtServer(l, level.damageSources().campfire(), this.fireDamage);
        }

        super.stepOn(level, pos, onState, entity);
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(BlockStateProperties.LIT)) {
            if (random.nextInt(10) == 0) {
                level.playLocalSound(
                        pos.getX() + 0.5,
                        pos.getY() + 0.5,
                        pos.getZ() + 0.5,
                        SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS,
                        0.5F + random.nextFloat(), random.nextFloat() * 0.5F + 0.3F, false);
            }

            if (random.nextInt(5) == 0) {
                for(int i = 0; i < random.nextInt(1) + 1; ++i) {
                    level.addParticle(ParticleTypes.LAVA,
                            pos.getX() + 0.5,
                            pos.getY() + 0.5,
                            pos.getZ() + 0.5,
                            random.nextFloat() / 2,
                            5.0E-5,
                            random.nextFloat() / 2);
                }
            }
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.LIT);
    }
    protected BlockState updateShape(BlockState state, LevelReader reader, ScheduledTickAccess access, BlockPos pos, Direction dir, BlockPos pos1, BlockState state1, RandomSource source) {
        BlockState s = getState(reader, pos);

        return super.updateShape(state, reader, access, pos, dir, pos1, state1, source)
                .setValue(BlockStateProperties.LIT, s.getValue(BlockStateProperties.LIT));
    }

    public BlockState getState(LevelReader reader, BlockPos pos) {
        BlockState state = reader.getBlockState(pos).is(this) ? reader.getBlockState(pos) : defaultBlockState();

        if (reader.getBlockState(pos.above()).is(Blocks.SNOW) && reader instanceof Level level) {
            state = state.setValue(BlockStateProperties.LIT, false);
            level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), 2);
        }

        return state;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState s = getState(context.getLevel(), context.getClickedPos());
        return super.getStateForPlacement(context)
                .setValue(BlockStateProperties.LIT, s.getValue(BlockStateProperties.LIT));
    }
}
