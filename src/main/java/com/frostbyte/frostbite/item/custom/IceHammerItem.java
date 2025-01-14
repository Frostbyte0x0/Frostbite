package com.frostbyte.frostbite.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class IceHammerItem extends Item {
    private final int size = 6;
    private boolean used = false;

    public IceHammerItem(Properties p_333796_) {
        super(p_333796_);
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 11.0F,
                        AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -3.4F,
                        AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }

    public static Tool createToolProperties() {
        return new Tool(List.of(), 1.0F, 2);
    }

    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (used) {
            context.getPlayer().displayClientMessage(Component.literal("Yes"), false);
            List<Entity> entities = context.getLevel().getEntitiesOfClass(Entity.class,
                    new AABB(new Vec3(context.getPlayer().getBlockX() - size, context.getPlayer().getBlockY() - size, context.getPlayer().getBlockZ() - size),
                            new Vec3(context.getPlayer().getBlockX() + size, context.getPlayer().getBlockY() + size, context.getPlayer().getBlockZ() + size)));

            for (Entity entity : entities) {
                context.getLevel().setBlock(entity.blockPosition(), Blocks.POWDER_SNOW.defaultBlockState(), 3);
                entity.addDeltaMovement(new Vec3(0, 5, 0));
                context.getPlayer().displayClientMessage(Component.literal((String.format("%s", entity.getName().getString()))), false);
            }
        }
        used = !used;
        return super.useOn(context);
    }
}
