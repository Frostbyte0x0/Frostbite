package org.exodusstudio.frostbite.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.Frostbite;

import java.util.Optional;

public class StarseekingCompass extends CompassItem {
    public StarseekingCompass(Properties p_40718_) {
        super(p_40718_);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int itemSlot, boolean isSelected) {
        if ((!stack.has(DataComponents.LODESTONE_TRACKER) || stack.get(DataComponents.LODESTONE_TRACKER).target().isEmpty()) &&
                level instanceof ServerLevel serverLevel) {
            BlockPos blockpos = Frostbite.OTFPortalPos;
            if (blockpos != null) {
                LodestoneTracker lodestonetracker = new LodestoneTracker
                        (Optional.of(GlobalPos.of(level.dimension(), blockpos)), true);
                stack.set(DataComponents.LODESTONE_TRACKER, lodestonetracker);
            }
        }
        //super.inventoryTick(stack, level, entity, itemSlot, isSelected);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.FAIL;
    }

    public Component getName(ItemStack p_371723_) {
        return super.getName(new ItemStack(Items.AIR));
    }
}
