package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;

import java.util.HashSet;
import java.util.Set;

public class IceBlockEntity extends FallingBlockEntity {
    public IceBlockEntity(EntityType<? extends FallingBlockEntity> p_31950_, Level p_31951_) {
        super(p_31950_, p_31951_);
        this.blockState = Blocks.BLUE_ICE.defaultBlockState();
    }

    @Override
    protected double getDefaultGravity() {
        if (!hurtMarked) {
            return 0.025;
        }
        return 1;
    }

    @Override
    public boolean isAttackable() {
        return true;
    }

    @Override
    public void tick() {
        ++this.time;
        this.applyGravity();
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.applyEffectsFromBlocks();
        this.handlePortal();

        if (this.isAlive() || this.forceTickAfterTeleportToDuplicate) {
            if (this.onGround() || !level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox()).isEmpty() || this.horizontalCollision) {
                level().explode(this, this.getX(), this.getY(), this.getZ(), 3f, Level.ExplosionInteraction.BLOCK);
                Set<BlockPos> set = new HashSet<>();
                for (int j = 0; j < 16; ++j) {
                    for (int k = 0; k < 16; ++k) {
                        for (int l = 0; l < 16; ++l) {
                            if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                                double d0 = (float)j / 15.0F * 2.0F - 1.0F;
                                double d1 = (float)k / 15.0F * 2.0F - 1.0F;
                                double d2 = (float)l / 15.0F * 2.0F - 1.0F;
                                double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                                d0 /= d3;
                                d1 /= d3;
                                d2 /= d3;
                                float f = 3 * (0.7F + this.level().random.nextFloat() * 0.6F);
                                double d4 = this.getX();
                                double d5 = this.getY();
                                double d6 = this.getZ();

                                for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                                    BlockPos blockpos = BlockPos.containing(d4, d5, d6);
                                    BlockState blockstate = this.level().getBlockState(blockpos);
                                    if (!this.level().isInWorldBounds(blockpos)) {
                                        break;
                                    }

                                    if (!blockstate.is(Blocks.AIR) && blockstate.getExplosionResistance(this.level(), blockpos, null) < 10
                                            && this.distanceToSqr(blockpos.getCenter()) < 9f) {
                                        set.add(blockpos);
                                    }

                                    d4 += d0 * (double)0.3F;
                                    d5 += d1 * (double)0.3F;
                                    d6 += d2 * (double)0.3F;
                                }
                            }
                        }
                    }
                }
                this.discard();
                for (BlockPos blockPos : set) {
                    level().setBlock(blockPos, Blocks.BLUE_ICE.defaultBlockState(), 3);
                }
            }
        }
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
    }

    @Override
    public final boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float damage) {
        if (damageSource.getEntity() instanceof Player player &&
                (player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistry.ICE_HAMMER) ||
                        player.getItemInHand(InteractionHand.OFF_HAND).is(ItemRegistry.ICE_HAMMER))) {
            level().playSound(null, player.blockPosition(), SoundEvents.ANVIL_LAND, SoundSource.PLAYERS);
            float x = player.getXRot();
            float y = player.getYRot();
            float z = 0f;
            float velocity = 1f;
            float inaccuracy = 2f;

            float f = -Mth.sin(y * ((float)Math.PI / 180F)) * Mth.cos(x * ((float)Math.PI / 180F));
            float f1 = -Mth.sin((x + z) * ((float)Math.PI / 180F));
            float f2 = Mth.cos(y * ((float)Math.PI / 180F)) * Mth.cos(x * ((float)Math.PI / 180F));

            Vec3 vec3 = player.getKnownMovement();
            Vec3 vec31 = (new Vec3(f, f1, f2)).normalize().add(
                    random.triangle(0.0F, 0.0172275 * inaccuracy),
                    random.triangle(0.0F, 0.0172275 * inaccuracy),
                    random.triangle(0.0F, 0.0172275 * inaccuracy)).scale(velocity);
            Vec3 vec32 = vec31.add(vec3);

            this.setDeltaMovement(this.getDeltaMovement().add(vec32));
        }
        markHurt();

        return true;
    }
}
