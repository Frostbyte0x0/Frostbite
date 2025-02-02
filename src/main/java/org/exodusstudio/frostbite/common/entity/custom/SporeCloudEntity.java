package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.component.JarContentsData;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;

public class SporeCloudEntity extends AreaEffectCloud {
    private static final EntityDataAccessor<Float> DATA_RADIUS;
    private static final EntityDataAccessor<Boolean> DATA_WAITING;
    private static final EntityDataAccessor<ParticleOptions> DATA_PARTICLE;
    private PotionContents potionContents;


    public SporeCloudEntity(EntityType<?> entityType, Level level) {
        super(EntityRegistry.SPORE_CLOUD.get(), level);
        this.potionContents = PotionContents.EMPTY;
        this.noPhysics = true;
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
        } else {
            return super.interact(player, hand);
        }
    }

    public ParticleOptions getParticle() {
        return (ParticleOptions)this.getEntityData().get(DATA_PARTICLE);
    }

    @Override
    public void setParticle(ParticleOptions particleOption) {
        this.getEntityData().set(DATA_PARTICLE, particleOption);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_RADIUS, 3.0F);
        builder.define(DATA_WAITING, false);
        builder.define(DATA_PARTICLE, ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, -1));
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (DATA_RADIUS.equals(key)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(key);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, 0.5F);
    }

    static {
        DATA_RADIUS = SynchedEntityData.defineId(SporeCloudEntity.class, EntityDataSerializers.FLOAT);
        DATA_WAITING = SynchedEntityData.defineId(SporeCloudEntity.class, EntityDataSerializers.BOOLEAN);
        DATA_PARTICLE = SynchedEntityData.defineId(SporeCloudEntity.class, EntityDataSerializers.PARTICLE);
    }
}
