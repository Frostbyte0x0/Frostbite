package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.GameRuleRegistry;

import java.util.Optional;
import java.util.UUID;

public class FrozenRemnantsEntity extends Mob{
    protected NonNullList<ItemStack> items;
    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID;
    private static final EntityDataAccessor<Integer> DATA_OWNER_ID;
    private static final EntityDataAccessor<Boolean> DATA_IS_ON_SCREEN;
    private static final EntityDataAccessor<Float> DATA_HEAD_PITCH;
    private static final EntityDataAccessor<Boolean> DATA_SHOULD_PLAY_SOUND;

    public FrozenRemnantsEntity(EntityType<? extends Mob> entityType, Level level) {
        super(EntityRegistry.FROZEN_REMNANTS.get(), level);
        this.items = NonNullList.createWithCapacity(54);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_OWNER_UUID, Optional.empty());
        builder.define(DATA_OWNER_ID, 0);
        builder.define(DATA_IS_ON_SCREEN, true);
        builder.define(DATA_HEAD_PITCH, 0f);
        builder.define(DATA_SHOULD_PLAY_SOUND, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putUUID("ownerUUID", this.getOwnerUUID());
        compound.put("items", this.saveItems(new ListTag()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setOwnerUUID(compound.getUUID("ownerUUID"));
        ListTag listtag = compound.getList("items", 10);
        this.loadItems(listtag);
    }

    public ListTag saveItems(ListTag listTag) {
        for (int i = 0; i < this.items.size(); ++i) {
            if (!(this.items.get(i)).isEmpty()) {
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.putByte("Slot", (byte)i);
                listTag.add((this.items.get(i)).save(this.registryAccess(), compoundtag));
            }
        }

        return listTag;
    }

    public void loadItems(ListTag listTag) {
        this.items = NonNullList.withSize(54, ItemStack.EMPTY);

        for (int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundtag = listTag.getCompound(i);
            int j = compoundtag.getByte("Slot") & 255;
            ItemStack itemstack = ItemStack.parse(this.registryAccess(), compoundtag).orElse(ItemStack.EMPTY);
            this.items.set(j, itemstack);
        }
    }

    public void setOnScreen(boolean isOnScreen) {
        if (!this.isOnScreen() && isOnScreen) {
            this.setShouldPlaySound(true);
        }
        this.getEntityData().set(DATA_IS_ON_SCREEN, isOnScreen);
    }

    public boolean shouldPlaySound() {
        return this.getEntityData().get(DATA_SHOULD_PLAY_SOUND);
    }

    public void setShouldPlaySound(boolean shouldPlaySound) {
        this.getEntityData().set(DATA_SHOULD_PLAY_SOUND, shouldPlaySound);
    }

    public boolean isOnScreen() {
        return this.getEntityData().get(DATA_IS_ON_SCREEN);
    }

    public float getHeadPitch() {
        return this.getEntityData().get(DATA_HEAD_PITCH);
    }

    public void setHeadPitch(float headPitch) {
        this.getEntityData().set(DATA_HEAD_PITCH, headPitch);
    }

    public void setOwnerId(int ownerId) {
        this.getEntityData().set(DATA_OWNER_ID, ownerId);
    }

    public int getOwnerId() {
        return this.getEntityData().get(DATA_OWNER_ID);
    }

    public void setOwner(Entity owner) {
        this.getEntityData().set(DATA_OWNER_UUID, Optional.of(owner.getUUID()));
    }

    public Entity getOwner() {
        if (this.getOwnerUUID() == null) {
            return null;
        }
        if (this.level() instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(this.getOwnerUUID());
            this.setOwnerId(entity == null ? 0 : entity.getId());
            return entity;
        }
        return this.level().getEntity(this.getOwnerId());
    }

    public void setOwnerUUID(UUID uuid) {
        this.getEntityData().set(DATA_OWNER_UUID, Optional.of(uuid));
    }

    public UUID getOwnerUUID() {
        return this.getEntityData().get(DATA_OWNER_UUID).orElse(null);
    }

    public void setItems(NonNullList<ItemStack> items) {
        this.items = NonNullList.copyOf(items);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 8D);
    }

    public static boolean shouldSpawnFrozenRemnants(ServerLevel serverLevel) {
        return !serverLevel.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)
                && serverLevel.getGameRules().getBoolean(GameRuleRegistry.RULE_SPAWN_FROZEN_REMNANTS)
                && serverLevel.dimension().toString().equals("ResourceKey[minecraft:dimension / frostbite:hoarfrost]");
    }

    @Override
    public void tick() {
        super.tick();
        Entity owner = this.getOwner();
        if (owner == null || !owner.isAlive()) {
            return;
        }

        if (!isOnScreen()) {
            this.setYRot(calculateYRot().orElse(0f));
            this.setHeadPitch(calculateHeadPitch().orElse(0f));
        }

        if (this.shouldPlaySound()) {
            this.setShouldPlaySound(false);
            this.level().playSound(null, this.getOnPos(), SoundEvents.CREAKING_FREEZE, SoundSource.HOSTILE, 1f, this.level().getRandom().nextFloat() * 0.1F + 0.9F);
        }
    }

    public Optional<Float> calculateHeadPitch() {
        Entity owner = this.getOwner();
        if (owner == null || !owner.isAlive()) {
            return Optional.empty();
        }
        double d0 = owner.getX() - this.getX();
        double d1 = owner.getEyeY() - this.getEyeY();
        double d2 = owner.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        return !(Math.abs(d1) > (double)1.0E-5F) && !(Math.abs(d3) > (double)1.0E-5F) ? Optional.empty() : Optional.of((float)(-(Mth.atan2(d1, d3) * (double)180.0F / (double)(float)Math.PI)));
    }

    public Optional<Float> calculateYRot() {
        Entity owner = this.getOwner();
        double d0 = owner.getX() - this.getX();
        double d1 = owner.getZ() - this.getZ();
        return !(Math.abs(d1) > (double)1.0E-5F) && !(Math.abs(d0) > (double)1.0E-5F) ? Optional.empty() : Optional.of((float)(Mth.atan2(d1, d0) * (double)180.0F / (double)(float)Math.PI) - 90.0F);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float damage) {
        if (damageSource.getEntity() == null) {
            return false;
        } else if (!damageSource.getEntity().is(getOwner())) {
            return false;
        }

        Player player = Minecraft.getInstance().player;
        assert player != null;
        for (int i = 0; i < 80; i++) {
            player.level().addAlwaysVisibleParticle(
                    ParticleTypes.DRIPPING_WATER,
                    this.getX() - random.nextFloat() + 0.5f,
                    this.getY() + 1.5f * random.nextFloat() + 0.5f,
                    this.getZ() - random.nextFloat() + 0.5f,
                    0,
                    0,
                    0);

        }

        this.level().playSound(null, this.getOnPos(), SoundEvents.LAVA_EXTINGUISH, SoundSource.HOSTILE, 1f, this.level().getRandom().nextFloat() * 0.1F + 0.9F);
        this.dropEquipment(serverLevel);
        this.dead = true;
        this.discard();
        return false;
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    protected void dropEquipment(ServerLevel serverLevel) {
        super.dropEquipment(serverLevel);
        for (ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty() && !EnchantmentHelper.has(itemstack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)) {
                this.spawnAtLocation(serverLevel, itemstack);
            }
        }
    }

    static {
        DATA_OWNER_UUID = SynchedEntityData.defineId(FrozenRemnantsEntity.class, EntityDataSerializers.OPTIONAL_UUID);
        DATA_OWNER_ID = SynchedEntityData.defineId(FrozenRemnantsEntity.class, EntityDataSerializers.INT);
        DATA_IS_ON_SCREEN = SynchedEntityData.defineId(FrozenRemnantsEntity.class, EntityDataSerializers.BOOLEAN);
        DATA_HEAD_PITCH = SynchedEntityData.defineId(FrozenRemnantsEntity.class, EntityDataSerializers.FLOAT);
        DATA_SHOULD_PLAY_SOUND = SynchedEntityData.defineId(FrozenRemnantsEntity.class, EntityDataSerializers.BOOLEAN);
    }
}
