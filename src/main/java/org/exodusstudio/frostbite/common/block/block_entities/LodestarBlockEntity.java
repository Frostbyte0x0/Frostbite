package org.exodusstudio.frostbite.common.block.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.registry.BlockEntityRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class LodestarBlockEntity extends BlockEntity {
    public int tickCount;
    public float activeRotation;

    public LodestarBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.LODESTAR.get(), pos, blockState);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, LodestarBlockEntity blockEntity) {
        ++blockEntity.tickCount;
        ++blockEntity.activeRotation;
        //animationTick(level, pos, list, blockEntity.destroyTarget, blockEntity.tickCount);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, LodestarBlockEntity blockEntity) {
        ++blockEntity.tickCount;
    }

    private static void animationTick(Level level, BlockPos pos, List<BlockPos> positions, @Nullable Entity entity, int tickCount) {
        RandomSource randomsource = level.random;
        double d0 = (Mth.sin((float)(tickCount + 35) * 0.1F) / 2.0F + 0.5F);
        d0 = (d0 * d0 + d0) * (double)0.3F;
        Vec3 vec3 = new Vec3((double)pos.getX() + (double)0.5F, (double)pos.getY() + (double)1.5F + d0, (double)pos.getZ() + (double)0.5F);

        for (BlockPos blockpos : positions) {
            if (randomsource.nextInt(50) == 0) {
                BlockPos blockpos1 = blockpos.subtract(pos);
                float f = -0.5F + randomsource.nextFloat() + (float)blockpos1.getX();
                float f1 = -2.0F + randomsource.nextFloat() + (float)blockpos1.getY();
                float f2 = -0.5F + randomsource.nextFloat() + (float)blockpos1.getZ();
                level.addParticle(ParticleTypes.NAUTILUS, vec3.x, vec3.y, vec3.z, f, f1, f2);
            }
        }

        if (entity != null) {
            Vec3 vec31 = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
            float f3 = (-0.5F + randomsource.nextFloat()) * (3.0F + entity.getBbWidth());
            float f4 = -1.0F + randomsource.nextFloat() * entity.getBbHeight();
            float f5 = (-0.5F + randomsource.nextFloat()) * (3.0F + entity.getBbWidth());
            Vec3 vec32 = new Vec3(f3, f4, f5);
            level.addParticle(ParticleTypes.NAUTILUS, vec31.x, vec31.y, vec31.z, vec32.x, vec32.y, vec32.z);
        }
    }

    public float getActiveRotation(float partialTick) {
        return (this.activeRotation + partialTick) * -0.0375F;
    }
}
