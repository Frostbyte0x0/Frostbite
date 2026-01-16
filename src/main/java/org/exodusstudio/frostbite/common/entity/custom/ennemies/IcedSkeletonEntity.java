package org.exodusstudio.frostbite.common.entity.custom.ennemies;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.CommonHooks;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.exodusstudio.frostbite.common.util.TemperatureEntity;

import java.util.function.Predicate;

public class IcedSkeletonEntity extends AbstractSkeleton implements TemperatureEntity {
    public IcedSkeletonEntity(EntityType<? extends IcedSkeletonEntity> ignored, Level level) {
        super(EntityRegistry.ICED_SKELETON.get(), level);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.FOLLOW_RANGE, 15)
                .add(Attributes.MOVEMENT_SPEED, 0.225);
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }

    @Override
    public ItemStack getProjectile(ItemStack shootable) {
        if (shootable.getItem() instanceof ProjectileWeaponItem) {
            Predicate<ItemStack> predicate = ((ProjectileWeaponItem)shootable.getItem()).getSupportedHeldProjectiles(shootable);
            ItemStack itemstack = ProjectileWeaponItem.getHeldProjectile(this, predicate);
            return CommonHooks.getProjectile(this, shootable, itemstack.isEmpty() ? new ItemStack(ItemRegistry.FROZEN_ARROW.asItem()) : itemstack);
        } else {
            return CommonHooks.getProjectile(this, shootable, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public LivingEntity getInstance() {
        return this;
    }
}
