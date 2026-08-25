package org.exodusstudio.frostbite.common.block.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.block.RuneBlock;
import org.exodusstudio.frostbite.common.registry.BlockEntityRegistry;
import org.exodusstudio.frostbite.common.registry.BlockRegistry;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.util.Util;
import org.exodusstudio.frostbite.common.util.helpers.DataHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RuneBlockEntity extends BlockEntity {
    private static final RandomSource random = RandomSource.create();
    private static final int WIDTH = 10;
    private static final int HEIGHT = 5;
    private static final Set<EntityType<? extends Monster>> ALLIES = new HashSet<>(){{
        add(EntityRegistry.ICED_ZOMBIE.get());
        add(EntityRegistry.ICED_SKELETON.get());
        add(EntityRegistry.ICED_CREEPER.get());
        add(EntityRegistry.TORCH.get());
        add(EntityRegistry.REVENANT.get());
        add(EntityRegistry.SPECTER.get());
    }};
    private static final Set<EntityType<? extends Monster>> GUARDS = new HashSet<>(){{
        add(EntityRegistry.GUARD.get());
        add(EntityRegistry.CHIEF_GUARD.get());
        add(EntityRegistry.HEAVY_GUARD.get());
    }};

    public int tickCount;
    public float health;
    public boolean opened;
    public int alliesDetected;
    public int vulnerableStart;
    public boolean isVulnerable;
    public int maxHealth;
    public Set<Vec3> allyPositions = new HashSet<>();

//    public final boolean isGuardingRune;

    public RuneBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.RUNE.get(), pos, blockState);
//        this.isGuardingRune = isGuardingRune;
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, RuneBlockEntity blockEntity) {
        if (blockEntity.tickCount == 0) {
            DataHelper.setBlockData(level.getChunkAt(pos), pos, "max_health", state.getValue(RuneBlock.LOOT_LEVEL).getMaxHealth());

            if (!state.getValue(RuneBlock.OPENED) && DataHelper.getBlockData(level.getChunkAt(pos), pos, "health") == 0)
                DataHelper.setBlockData(level.getChunkAt(pos), pos, "health", state.getValue(RuneBlock.LOOT_LEVEL).getMaxHealth());
        }

        ++blockEntity.tickCount;
        blockEntity.health = DataHelper.getBlockData(level.getChunkAt(pos), pos, "health");
        blockEntity.maxHealth = (int) Math.floor(DataHelper.getBlockData(level.getChunkAt(pos), pos, "max_health"));
        blockEntity.alliesDetected = (int) Math.floor(DataHelper.getBlockData(level.getChunkAt(pos), pos, "allies_left"));
        blockEntity.opened = state.getValue(RuneBlock.OPENED);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, RuneBlockEntity blockEntity) {
        clientTick(level, pos, state, blockEntity);

        if (state.getValue(RuneBlock.OPENED)) return;

        int d = blockEntity.tickCount - blockEntity.vulnerableStart;
        if (d % 20 == 0 && d < 100 && blockEntity.isVulnerable) {
            level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1, 0.5f + d / 100f);
        }

        if (blockEntity.tickCount % 20 != 0) return;

        Vec3 c = Vec3.atCenterOf(pos);
        AABB aabb = new AABB(
                c.x - WIDTH / 2f,
                c.y - HEIGHT / 2f,
                c.z - WIDTH / 2f,
                c.x + WIDTH / 2f,
                c.y + HEIGHT / 2f,
                c.z + WIDTH / 2f
        );
        blockEntity.allyPositions = level.getEntities((Entity) null, aabb, e -> blockEntity.getAllies().contains(e.typeHolder().value()))
                .stream().map(Entity::position).collect(Collectors.toSet());
        DataHelper.setBlockData(level.getChunkAt(pos), pos, "allies_left", blockEntity.allyPositions.size());

        if (blockEntity.allyPositions.isEmpty() && !blockEntity.isGuardingRune() && !blockEntity.isVulnerable) {
            blockEntity.vulnerableStart = blockEntity.tickCount;
            blockEntity.isVulnerable = true;
        }

        d = blockEntity.tickCount - blockEntity.vulnerableStart;

        if (d >= 100 && blockEntity.isVulnerable) {
            blockEntity.isVulnerable = false;
            List<EntityType<? extends Monster>> m = blockEntity.getAllies().stream().toList();
            level.playSound(null, pos, SoundEvents.APPLY_EFFECT_RAID_OMEN, SoundSource.BLOCKS, 1, 0.75f);

            for (int i = 0; i < 24; i++) {
                int j = random.nextIntBetweenInclusive(0, m.size() - 1);
                Util.spawnMonsterRandomlyAroundBlockPos(() -> m.get(j).create(level, EntitySpawnReason.MOB_SUMMONED), (ServerLevel) level, pos, 1, 6);
            }
        }
    }

    public boolean damage(Level level, BlockPos pos, BlockState state, int amount) {
        Map<String, Float> data = DataHelper.getBlockData(level.getChunkAt(pos), pos);
        float health = data.get("health");
        if ((int) Math.floor(data.get("allies_left")) > 0) return health == 0;

        health -= amount;
        vulnerableStart = tickCount;

        health = Math.clamp(health, 0, maxHealth);
        if (level instanceof ServerLevel)
            DataHelper.setBlockData(level.getChunkAt(pos), pos, "health", health);
        return health == 0;
    }

    public Set<EntityType<? extends Monster>> getAllies() {
        return isGuardingRune() ? GUARDS : ALLIES;
    }

    public boolean isGuardingRune() {
        Map<String, Float> m = DataHelper.getBlockData(level.getChunkAt(worldPosition), worldPosition);
        return DataHelper.getBlockData(level.getChunkAt(worldPosition), worldPosition, "is_guarding_rune") == 1;
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        DataHelper.setBlockData(level.getChunkAt(worldPosition), worldPosition, "is_guarding_rune",
                level.getBlockState(worldPosition).is(BlockRegistry.GUARDING_RUNE) ? 1 : 0);
//        if (isGuardingRune || isGuardingRune()) {
//            DataHelper.setBlockData(level.getChunkAt(worldPosition), worldPosition, "is_guarding_rune", 1);
//        }
    }

    // Detect entities in set radius
    // If none detected, become vulnerable
    //     If Challenge: start timer and summon entities and the end and stop vulnerable
}
