package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.component.JarContentsData;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SporeCloudEntity extends AreaEffectCloud {
    private PotionContents potionContents;
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;

    public SporeCloudEntity(EntityType<?> entityType, Level level) {
        super(EntityRegistry.SPORE_CLOUD.get(), level);
        this.potionContents = PotionContents.EMPTY;
        this.noPhysics = true;
        this.setDuration(600);
        this.setDurationOnUse(600);
        this.setRadius(10f);
        this.setRadiusOnUse(10f);
    }

    public SporeCloudEntity(Level level, double x, double y, double z) {
        this(EntityRegistry.SPORE_CLOUD.get(), level);
        this.setPos(x, y, z);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.is(ItemRegistry.EMPTY_JAR)) {
            player.playSound(SoundEvents.BOTTLE_FILL, 1.0F, 1.0F);
            ItemStack itemstack1 = ItemUtils.createFilledResult(itemstack, player, ItemRegistry.JAR.toStack());
            itemstack1.set(DataComponentTypeRegistry.JAR_CONTENTS.get(), new JarContentsData(this.potionContents));
            player.setItemInHand(hand, itemstack1);
            return InteractionResult.SUCCESS;
        }
        return super.interact(player, hand);
    }

    @Override
    public void tick() {
        if (this.getRadius() == 0) {
            this.setRadius(2f);
        }

        if (this.getDuration() == 0) {
            this.discard();
        }

        this.setDuration(this.getDuration() - 1);
        float f = this.getRadius();

        if (this.level().isClientSide) {
            ParticleOptions particleoptions = this.getParticle();

            for (int j = 0; j < Mth.ceil((float) Math.PI * f * f * 2.8f); ++j) {
                float f2 = this.random.nextFloat() * ((float)Math.PI * 2F);
                float f3 = Mth.sqrt(this.random.nextFloat()) * f;
                double d0 = this.getX() + (double)(Mth.cos(f2) * f3);
                double d1 = this.getY() + this.random.nextDouble();
                double d2 = this.getZ() + (double)(Mth.sin(f2) * f3);
                if (particleoptions.getType() == ParticleTypes.ENTITY_EFFECT) {
                    this.level().addAlwaysVisibleParticle(particleoptions, d0, d1, d2, 0.0F, 0.0F, 0.0F);
                } else {
                    this.level().addAlwaysVisibleParticle(particleoptions, d0, d1, d2, (0.5D - this.random.nextDouble()) * 0.15, 0.05f * this.random.nextDouble() - 0.02D, (0.5D - this.random.nextDouble()) * 0.15);
                }
            }
        }

        if (this.tickCount % 21 == 0) {
            if (this.level() instanceof ServerLevel) {
                List<LivingEntity> list1 = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
                if (list1.isEmpty()) return;
                for (LivingEntity livingentity : list1) {
                    if (livingentity.isAffectedByPotions()) {
                        Objects.requireNonNull(livingentity);
                        MobEffectInstance mobEffectInstance = this.potionContents.customEffects().getFirst();
                        if (livingentity.canBeAffected(this.potionContents.customEffects().getFirst())) {
                            if (livingentity.distanceTo(this) <= (double)(f * f)) {
                                livingentity.addEffect(mobEffectInstance, this);
                            }
                        }
                    }
                }
            }
        }
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
        this.ownerUUID = owner == null ? null : owner.getUUID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null || this.owner.isRemoved()) {
            if (this.ownerUUID != null) {
                if (this.level() instanceof ServerLevel serverLevel) {
                    Entity var3 = serverLevel.getEntity(this.ownerUUID);
                    LivingEntity livingEntity;
                    if (var3 instanceof LivingEntity) {
                        livingEntity = (LivingEntity) var3;
                    } else livingEntity = null;

                    this.owner = livingEntity;
                }
            }
        }
        return this.owner;
    }

    @Override
    public void addEffect(@NotNull MobEffectInstance effectInstance) {
        this.setPotionContents(this.potionContents.withEffectAdded(effectInstance));
    }

    @Override
    public void setPotionContents(@NotNull PotionContents potionContents) {
        this.potionContents = potionContents;
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, 1F);
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("Age", this.tickCount);
        compound.putFloat("Radius", this.getRadius());
        RegistryOps<Tag> registryops = this.registryAccess().createSerializationContext(NbtOps.INSTANCE);
        compound.put("Particle", ParticleTypes.CODEC.encodeStart(registryops, this.getParticle()).getOrThrow());
        if (this.ownerUUID != null) {
            compound.putUUID("Owner", this.ownerUUID);
        }

        if (!this.potionContents.equals(PotionContents.EMPTY)) {
            Tag tag = PotionContents.CODEC.encodeStart(registryops, this.potionContents).getOrThrow();
            compound.put("potion_contents", tag);
        }
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        this.tickCount = compound.getInt("Age");
        this.setRadius(compound.getFloat("Radius"));
        if (compound.hasUUID("Owner")) {
            this.ownerUUID = compound.getUUID("Owner");
        }

        RegistryOps<Tag> registryops = this.registryAccess().createSerializationContext(NbtOps.INSTANCE);
        if (compound.contains("Particle", 10)) {
            ParticleTypes.CODEC.parse(registryops, compound.get("Particle")).resultOrPartial((p_329991_) -> Frostbite.LOGGER.warn("Failed to parse area effect cloud particle options: '{}'", p_329991_)).ifPresent(this::setParticle);
        }

        if (compound.contains("potion_contents")) {
            PotionContents.CODEC.parse(registryops, compound.get("potion_contents")).resultOrPartial((p_340707_) -> Frostbite.LOGGER.warn("Failed to parse area effect cloud potions: '{}'", p_340707_)).ifPresent(this::setPotionContents);
        }
    }
}
