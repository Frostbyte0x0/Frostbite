package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
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
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.component.JarContentsData;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;

public class SporeCloudEntity extends AreaEffectCloud {
    private PotionContents potionContents;

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
        } else {
            return super.interact(player, hand);
        }
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
            int i;
            float f1;

            i = Mth.ceil((float)Math.PI * f * f * 2.8f);
            f1 = f;

            for(int j = 0; j < i; ++j) {
                float f2 = this.random.nextFloat() * ((float)Math.PI * 2F);
                float f3 = Mth.sqrt(this.random.nextFloat()) * f1;
                double d0 = this.getX() + (double)(Mth.cos(f2) * f3);
                double d1 = this.getY() + this.random.nextDouble();
                double d2 = this.getZ() + (double)(Mth.sin(f2) * f3);
                if (particleoptions.getType() == ParticleTypes.ENTITY_EFFECT) {
                    this.level().addAlwaysVisibleParticle(particleoptions, d0, d1, d2, (double)0.0F, (double)0.0F, (double)0.0F);
                } else {
                    this.level().addAlwaysVisibleParticle(particleoptions, d0, d1, d2, (0.5D - this.random.nextDouble()) * 0.15, 0.05f * this.random.nextDouble() - 0.02D, (0.5D - this.random.nextDouble()) * 0.15);
                }
            }
        }
    }

    @Override
    public void setPotionContents(PotionContents potionContents) {
        this.potionContents = potionContents;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, 1F);
    }
}
