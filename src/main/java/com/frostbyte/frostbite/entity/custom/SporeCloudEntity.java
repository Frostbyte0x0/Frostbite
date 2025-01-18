//package com.frostbyte.frostbite.entity.custom;
//
//import com.frostbyte.frostbite.component.JarContentsData;
//import com.frostbyte.frostbite.component.ModDataComponentTypes;
//import com.frostbyte.frostbite.entity.ModEntities;
//import com.frostbyte.frostbite.item.ModItems;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import net.minecraft.core.Holder;
//import net.minecraft.core.particles.ColorParticleOption;
//import net.minecraft.core.particles.ParticleOptions;
//import net.minecraft.core.particles.ParticleTypes;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.syncher.EntityDataAccessor;
//import net.minecraft.network.syncher.EntityDataSerializers;
//import net.minecraft.network.syncher.SynchedEntityData;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.util.ARGB;
//import net.minecraft.util.Mth;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.InteractionResult;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.effect.MobEffect;
//import net.minecraft.world.effect.MobEffectInstance;
//import net.minecraft.world.entity.*;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.ItemUtils;
//import net.minecraft.world.item.alchemy.Potion;
//import net.minecraft.world.level.Level;
//
//import javax.annotation.Nullable;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.UUID;
//import java.util.stream.Stream;
//
//public class SporeCloudEntity extends Entity {
//    private static final EntityDataAccessor<Float> DATA_RADIUS;
//    private static final EntityDataAccessor<Boolean> DATA_WAITING;
//    private static final EntityDataAccessor<ParticleOptions> DATA_PARTICLE;
//    private JarContentsData jarContents;
//    private final Map<Entity, Integer> victims;
//    private int reapplicationDelay;
//    private int durationOnUse;
//    private float radiusOnUse;
//    private float radiusPerTick;
//    private int duration;
//    private int waitTime;
//    @Nullable
//    private LivingEntity owner;
//    @Nullable
//    private UUID ownerUUID;
//
//    public SporeCloudEntity(EntityType<?> entityType, Level level) {
//        super(ModEntities.SPORE_CLOUD.get(), level);
//        this.jarContents = JarContentsData.EMPTY;
//        this.victims = Maps.newHashMap();
//        this.duration = 600;
//        this.waitTime = 20;
//        this.reapplicationDelay = 20;
//        this.noPhysics = true;
//    }
//
//    public SporeCloudEntity(Level level, double x, double y, double z) {
//        this(ModEntities.SPORE_CLOUD.get(), level);
//        this.setPos(x, y, z);
//    }
//
//    @Override
//    public InteractionResult interact(Player player, InteractionHand hand) {
//        ItemStack itemstack = player.getItemInHand(hand);
//        if (itemstack.is(ModItems.EMPTY_JAR)) {
//            player.playSound(SoundEvents.BOTTLE_FILL, 1.0F, 1.0F);
//            ItemStack itemstack1 = ItemUtils.createFilledResult(itemstack, player, ModItems.JAR.toStack());
//            itemstack1.set(ModDataComponentTypes.JAR_CONTENTS.get(), this.jarContents);
//            player.setItemInHand(hand, itemstack1);
//            return InteractionResult.SUCCESS;
//        } else {
//            return super.interact(player, hand);
//        }
//    }
//
//    @Override
//    public void refreshDimensions() {
//        double d0 = this.getX();
//        double d1 = this.getY();
//        double d2 = this.getZ();
//        super.refreshDimensions();
//        this.setPos(d0, d1, d2);
//    }
//
//    public float getRadius() {
//        return (Float)this.getEntityData().get(DATA_RADIUS);
//    }
//
//    public void setJarContents(JarContentsData jarContents) {
//        this.jarContents = jarContents;
//        this.updateColor();
//    }
//
//    private void updateColor() {
//        ParticleOptions particleoptions = (ParticleOptions)this.entityData.get(DATA_PARTICLE);
//        if (particleoptions instanceof ColorParticleOption colorparticleoption) {
//            int i = this.jarContents.equals(JarContentsData.EMPTY) ? 0 : this.jarContents.getColor();
//            this.entityData.set(DATA_PARTICLE, ColorParticleOption.create(colorparticleoption.getType(), ARGB.opaque(i)));
//        }
//
//    }
//
//    public void addEffect(MobEffectInstance effectInstance) {
//        this.setJarContents(this.jarContents.withEffectAdded(effectInstance));
//    }
//
//    public ParticleOptions getParticle() {
//        return (ParticleOptions)this.getEntityData().get(DATA_PARTICLE);
//    }
//
//    public void setParticle(ParticleOptions particleOption) {
//        this.getEntityData().set(DATA_PARTICLE, particleOption);
//    }
//
//    protected void setWaiting(boolean waiting) {
//        this.getEntityData().set(DATA_WAITING, waiting);
//    }
//
//    public boolean isWaiting() {
//        return (Boolean)this.getEntityData().get(DATA_WAITING);
//    }
//
//    public int getDuration() {
//        return this.duration;
//    }
//
//    @Override
//    protected void defineSynchedData(SynchedEntityData.Builder builder) {
//        builder.define(DATA_RADIUS, 3.0F);
//        builder.define(DATA_WAITING, false);
//        builder.define(DATA_PARTICLE, ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, -1));
//    }
//
//    public void setRadius(float radius) {
//        if (!this.level().isClientSide) {
//            this.getEntityData().set(DATA_RADIUS, Mth.clamp(radius, 0.0F, 32.0F));
//        }
//
//    }
//
//    @Override
//    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
//        return false;
//    }
//
//    @Override
//    protected void readAdditionalSaveData(CompoundTag compoundTag) {
//
//    }
//
//    @Override
//    protected void addAdditionalSaveData(CompoundTag compoundTag) {
//
//    }
//
//    public void tick() {
//        super.tick();
//        Level var2 = this.level();
//        if (var2 instanceof ServerLevel serverlevel) {
//            this.serverTick(serverlevel);
//        } else {
//            this.clientTick();
//        }
//
//    }
//
//
//    private void clientTick() {
//        boolean flag = this.isWaiting();
//        float f = this.getRadius();
//        if (!flag || !this.random.nextBoolean()) {
//            ParticleOptions particleoptions = this.getParticle();
//            int i;
//            float f1;
//            if (flag) {
//                i = 2;
//                f1 = 0.2F;
//            } else {
//                i = Mth.ceil((float)Math.PI * f * f);
//                f1 = f;
//            }
//
//            for(int j = 0; j < i; ++j) {
//                float f2 = this.random.nextFloat() * ((float)Math.PI * 2F);
//                float f3 = Mth.sqrt(this.random.nextFloat()) * f1;
//                double d0 = this.getX() + (double)(Mth.cos(f2) * f3);
//                double d1 = this.getY();
//                double d2 = this.getZ() + (double)(Mth.sin(f2) * f3);
//                if (particleoptions.getType() == ParticleTypes.ENTITY_EFFECT) {
//                    if (flag && this.random.nextBoolean()) {
//                        this.level().addAlwaysVisibleParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, -1), d0, d1, d2, (double)0.0F, (double)0.0F, (double)0.0F);
//                    } else {
//                        this.level().addAlwaysVisibleParticle(particleoptions, d0, d1, d2, (double)0.0F, (double)0.0F, (double)0.0F);
//                    }
//                } else if (flag) {
//                    this.level().addAlwaysVisibleParticle(particleoptions, d0, d1, d2, (double)0.0F, (double)0.0F, (double)0.0F);
//                } else {
//                    this.level().addAlwaysVisibleParticle(particleoptions, d0, d1, d2, ((double)0.5F - this.random.nextDouble()) * 0.15, (double)0.01F, ((double)0.5F - this.random.nextDouble()) * 0.15);
//                }
//            }
//        }
//
//    }
//
//    private void serverTick(ServerLevel level) {
//        if (this.tickCount >= this.waitTime + this.duration) {
//            this.discard();
//        } else {
//            boolean flag = this.isWaiting();
//            boolean flag1 = this.tickCount < this.waitTime;
//            if (flag != flag1) {
//                this.setWaiting(flag1);
//            }
//
//            if (!flag1) {
//                float f = this.getRadius();
//                if (this.radiusPerTick != 0.0F) {
//                    f += this.radiusPerTick;
//                    if (f < 0.5F) {
//                        this.discard();
//                        return;
//                    }
//
//                    this.setRadius(f);
//                }
//
//                if (this.tickCount % 5 == 0) {
//                    this.victims.entrySet().removeIf((p_287380_) -> this.tickCount >= (Integer)p_287380_.getValue());
//                    if (!this.jarContents.hasEffects()) {
//                        this.victims.clear();
//                    } else {
//                        List<MobEffectInstance> list = Lists.newArrayList();
//                        if (this.jarContents.jar().isPresent()) {
//                            for(MobEffectInstance mobeffectinstance : ((Potion)((Holder)this.jarContents.jar().get()).value()).getEffects()) {
//                                list.add(new MobEffectInstance(mobeffectinstance.getEffect(), mobeffectinstance.mapDuration((p_267926_) -> p_267926_ / 4), mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(), mobeffectinstance.isVisible()));
//                            }
//                        }
//
//                        list.addAll(this.jarContents.customEffects());
//                        List<LivingEntity> list1 = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
//                        if (!list1.isEmpty()) {
//                            for(LivingEntity livingentity : list1) {
//                                if (!this.victims.containsKey(livingentity) && livingentity.isAffectedByPotions()) {
//                                    Stream var10000 = list.stream();
//                                    Objects.requireNonNull(livingentity);
//                                    if (!var10000.noneMatch(livingentity::canBeAffected)) {
//                                        double d0 = livingentity.getX() - this.getX();
//                                        double d1 = livingentity.getZ() - this.getZ();
//                                        double d2 = d0 * d0 + d1 * d1;
//                                        if (d2 <= (double)(f * f)) {
//                                            this.victims.put(livingentity, this.tickCount + this.reapplicationDelay);
//
//                                            for(MobEffectInstance mobeffectinstance1 : list) {
//                                                if (((MobEffect)mobeffectinstance1.getEffect().value()).isInstantenous()) {
//                                                    ((MobEffect)mobeffectinstance1.getEffect().value()).applyInstantenousEffect(level, this, this.getOwner(), livingentity, mobeffectinstance1.getAmplifier(), (double)0.5F);
//                                                } else {
//                                                    livingentity.addEffect(new MobEffectInstance(mobeffectinstance1), this);
//                                                }
//                                            }
//
//                                            if (this.radiusOnUse != 0.0F) {
//                                                f += this.radiusOnUse;
//                                                if (f < 0.5F) {
//                                                    this.discard();
//                                                    return;
//                                                }
//
//                                                this.setRadius(f);
//                                            }
//
//                                            if (this.durationOnUse != 0) {
//                                                this.duration += this.durationOnUse;
//                                                if (this.duration <= 0) {
//                                                    this.discard();
//                                                    return;
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//    }
//
//    public void setOwner(@Nullable LivingEntity owner) {
//        this.owner = owner;
//        this.ownerUUID = owner == null ? null : owner.getUUID();
//    }
//
//    @Nullable
//    public LivingEntity getOwner() {
//        if (this.owner != null && !this.owner.isRemoved()) {
//            return this.owner;
//        } else {
//            if (this.ownerUUID != null) {
//                Level level = this.level();
//                if (level instanceof ServerLevel serverLevel) {
//                    Entity var3 = serverLevel.getEntity(this.ownerUUID);
//                    LivingEntity var10001;
//                    if (var3 instanceof LivingEntity livingEntity) {
//                        var10001 = livingEntity;
//                    } else {
//                        var10001 = null;
//                    }
//
//                    this.owner = var10001;
//                }
//            }
//
//            return this.owner;
//        }
//    }
//
//    @Override
//    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
//        if (DATA_RADIUS.equals(key)) {
//            this.refreshDimensions();
//        }
//
//        super.onSyncedDataUpdated(key);
//    }
//
//    @Override
//    public EntityDimensions getDimensions(Pose pose) {
//        return EntityDimensions.scalable(this.getRadius() * 2.0F, 0.5F);
//    }
//
//    static {
//        DATA_RADIUS = SynchedEntityData.defineId(SporeCloudEntity.class, EntityDataSerializers.FLOAT);
//        DATA_WAITING = SynchedEntityData.defineId(SporeCloudEntity.class, EntityDataSerializers.BOOLEAN);
//        DATA_PARTICLE = SynchedEntityData.defineId(SporeCloudEntity.class, EntityDataSerializers.PARTICLE);
//    }
//}
