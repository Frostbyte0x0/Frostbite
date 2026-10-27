package org.exodusstudio.frostbite.common.entity.custom.elves;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.entity.goals.HealerElfHealGoal;
import org.exodusstudio.frostbite.common.item.weapons.elf.ModeWeapon;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;

import java.util.ArrayList;
import java.util.List;

public class HealerElfEntity extends ElfEntity {
    public final List<ElfEntity> alliesToHeal = new ArrayList<>();

    public HealerElfEntity(EntityType<? extends ElfEntity> ignored, Level level) {
        super(EntityRegistry.HEALER_ELF.get(), level, 100);
        ItemStack staff = new ItemStack(ItemRegistry.HEALING_STAFF.asItem());
        ModeWeapon.loadToMax(staff);
        setItemSlot(EquipmentSlot.MAINHAND, staff);
    }

    @Override
    public void tick() {
        if (!level().isClientSide() && getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
            ItemStack staff = new ItemStack(ItemRegistry.HEALING_STAFF.asItem());
            ModeWeapon.loadToMax(staff);
            setItemSlot(EquipmentSlot.MAINHAND, staff);
        }
        super.tick();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new HealerElfHealGoal(this, 1, 100, 20));
    }
}
