package org.exodusstudio.frostbite.common.entity.custom.elves;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.entity.custom.goals.SummonerElfSummonGoal;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;

public class SummonerElfEntity extends ElfEntity {
    public SummonerElfEntity(EntityType<? extends ElfEntity> ignored, Level level) {
        super(EntityRegistry.SUMMONER_ELF.get(), level, 100);
        setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.SUMMONING_STAFF.asItem()));
    }

    @Override
    public void tick() {
        if (!level().isClientSide && getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
            setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.SUMMONING_STAFF.asItem()));
        }
        super.tick();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new SummonerElfSummonGoal(this, 1, 100, 20));
    }
}
