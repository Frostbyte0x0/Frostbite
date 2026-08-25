package org.exodusstudio.frostbite.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.exodusstudio.frostbite.common.block.block_entities.RuneBlockEntity;
import org.exodusstudio.frostbite.common.registry.BlockEntityRegistry;
import org.exodusstudio.frostbite.common.util.helpers.DataHelper;
import org.exodusstudio.frostbite.common.util.helpers.ParticleHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RuneBlock extends BaseEntityBlock {
    private static final RandomSource random = RandomSource.create();
    public static final MapCodec<RuneBlock> CODEC = simpleCodec(RuneBlock::new);
    public static final EnumProperty<RuneLootLevel> LOOT_LEVEL =
            EnumProperty.create("loot_level", RuneLootLevel.class);
    public static final BooleanProperty OPENED =
            BlockStateProperties.CRACKED;

    public RuneBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(LOOT_LEVEL, RuneLootLevel.WOOD)
                .setValue(OPENED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LOOT_LEVEL, OPENED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new RuneBlockEntity(blockPos, blockState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityRegistry.RUNE.get(),
                level.isClientSide() ? RuneBlockEntity::clientTick : RuneBlockEntity::serverTick);
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        super.destroy(level, pos, state);
        DataHelper.removeBlockData(level.getChunk(pos), pos);
    }

    @Override
    protected void attack(BlockState state, Level level, BlockPos pos, Player player) {
        super.attack(state, level, pos, player);

        if (level.getBlockEntity(pos) instanceof RuneBlockEntity rune) {
            boolean vulnerable = (int) Math.floor((DataHelper.getBlockData(level.getChunkAt(pos), pos, "allies_left"))) == 0;
            if (!vulnerable || state.getValue(OPENED)) {
                failHitAndShowNearbyAllies(level, pos, rune);
                return;
            }

            boolean opened = rune.damage(level, pos, state, (int) Math.floor(getDamage(player)));
            if (opened && !state.getValue(OPENED)) {
                level.setBlock(pos, state.setValue(OPENED, true), 2);
                openAndSpawnLoot(level, pos);
            } else {
                if (level instanceof ServerLevel serverLevel)
                    showDamage(serverLevel, pos);
            }
        }
    }

    public void failHitAndShowNearbyAllies(Level level, BlockPos pos, RuneBlockEntity rune) {
        level.playSound(null, pos, SoundEvents.ITEM_BREAK.value(), SoundSource.BLOCKS, 1, random.nextFloat() * 0.25f + 0.5f);
        if (!(level instanceof ServerLevel)) return;

        Vec3 c = Vec3.atCenterOf(pos);
        for (Vec3 v : rune.allyPositions) {
            ParticleHelper.ring(level, ParticleTypes.SOUL_FIRE_FLAME, v.add(0, 1, 0), Vec3.Y_AXIS, 10, 0.75);
//            ParticleHelper.completeSphere(level, ParticleTypes.SOUL_FIRE_FLAME, v.add(0, 1, 0), Vec3.Y_AXIS, 10, 10, 1);
            ParticleHelper.spawnParticlesFromVector(level, ParticleTypes.FLAME, c, v.subtract(c).add(0, 1, 0), 10);
        }
    }

    public void openAndSpawnLoot(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.WITHER_DEATH, SoundSource.BLOCKS, 1, random.nextFloat() * 0.25f + 0.5f);

        if (!(level instanceof ServerLevel serverLevel)) return;

        serverLevel.sendParticles(ParticleTypes.RAID_OMEN, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 20, 0.5, 0.2, 0.5, 0.05);

        Vec3 center = Vec3.atCenterOf(pos);
        LootParams lootParams = (new LootParams.Builder(serverLevel))
                .withParameter(LootContextParams.ORIGIN, center)
                .create(LootContextParamSets.CHEST);

        List<ItemStack> drops = level.getServer().reloadableRegistries()
                .getLootTable(level.getBlockState(pos).getValue(LOOT_LEVEL).getLootTableKey()).getRandomItems(lootParams);

        drops.forEach((drop) -> {
            ItemEntity entity = new ItemEntity(level, center.x, center.y + 0.5, center.z, drop.copy());
            entity.setDeltaMovement((random.nextFloat() - 0.5) * 0.25, 0.25, (random.nextFloat() - 0.5) * 0.25);
            entity.setDefaultPickUpDelay();
            level.addFreshEntity(entity);
        });
    }

    public void showDamage(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.WITHER_HURT, SoundSource.BLOCKS, 1, random.nextFloat() * 0.25f + 0.75f);
        level.sendParticles(ParticleTypes.SOUL, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 20, 0.5, 0.2, 0.5, 0.05);
    }

    public static float getDamage(Player player) {
        float baseDamage = player.isAutoSpinAttack() ? player.autoSpinAttackDmg : (float)player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        ItemStack attackingItemStack = player.getWeaponItem();
        DamageSource damageSource = attackingItemStack.getDamageSource(player);
        float attackStrengthScale = player.getAttackStrengthScale(0.5F);
//        float magicBoost = attackStrengthScale * (EnchantmentHelper.modifyDamage(player.level(), player.getWeaponItem(), null, damageSource, baseDamage) - baseDamage);
        float magicBoost = 0;
        baseDamage *= 0.2F + attackStrengthScale * attackStrengthScale * 0.8F;

        if (baseDamage > 0.0F || magicBoost > 0.0F) {
            boolean fullStrengthAttack = attackStrengthScale > 0.9F;

            boolean criticalAttack = fullStrengthAttack &&
                    player.fallDistance > 0 &&
                    !player.onGround() && !player.onClimbable() && !player.isInWater() &&
                    !player.isMobilityRestricted() && !player.isPassenger() && !player.isSprinting();

            if (criticalAttack) {
                baseDamage *= 1.5F;
            }

            return baseDamage + magicBoost;
        }

        return 0;
    }

    protected VoxelShape getOcclusionShape(BlockState state) {
        return Shapes.empty();
    }

    protected VoxelShape getVisualShape(BlockState p_309057_, BlockGetter p_308936_, BlockPos p_308956_, CollisionContext p_309006_) {
        return Shapes.empty();
    }

    protected float getShadeBrightness(BlockState p_308911_, BlockGetter p_308952_, BlockPos p_308918_) {
        return 1.0F;
    }

    protected boolean propagatesSkylightDown(BlockState p_309084_) {
        return true;
    }
}
