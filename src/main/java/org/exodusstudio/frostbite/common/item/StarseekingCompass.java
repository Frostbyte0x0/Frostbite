package org.exodusstudio.frostbite.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.item.context.UseOnContext;
import org.exodusstudio.frostbite.common.registry.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class StarseekingCompass extends CompassItem {
    public StarseekingCompass(Properties p_40718_) {
        super(p_40718_);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot equipmentSlot) {
        if ((!stack.has(DataComponents.LODESTONE_TRACKER) || stack.get(DataComponents.LODESTONE_TRACKER).target().isEmpty()) &&
                level instanceof ServerLevel serverLevel) {
            BlockPos blockpos = serverLevel.findNearestMapStructure(Tags.STRUCTURE_OTF,
                    entity.blockPosition(), 100, false);

            if (blockpos != null) {
                LodestoneTracker lodestonetracker = new LodestoneTracker
                        (Optional.of(GlobalPos.of(level.dimension(), blockpos)), true);
                stack.set(DataComponents.LODESTONE_TRACKER, lodestonetracker);
            }
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.FAIL;
    }

    public Component getName(ItemStack stack) {
        return stack.getComponents().getOrDefault(DataComponents.ITEM_NAME, CommonComponents.EMPTY);
    }
}
