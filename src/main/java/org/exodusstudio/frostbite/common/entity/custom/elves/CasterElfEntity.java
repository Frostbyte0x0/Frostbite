package org.exodusstudio.frostbite.common.entity.custom.elves;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.entity.custom.goals.CasterElfCastGoal;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;

public class CasterElfEntity extends ElfEntity {
    public CasterElfEntity(EntityType<? extends ElfEntity> ignored, Level level) {
        super(EntityRegistry.CASTER_ELF.get(), level, 100);
    }

    @Override
    public void tick() {
        if (!level().isClientSide && getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
            setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.CASTING_STAFF.asItem()));
        }

        super.tick();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new CasterElfCastGoal(this, 1, 100, 20));
    }
}
