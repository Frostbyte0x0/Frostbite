package org.exodusstudio.frostbite.common.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.entity.custom.IceBlockEntity;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IceHammerItem extends Item {
    private final int size = 6;

    public IceHammerItem(Properties p_333796_) {
        super(p_333796_);
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 10.0F,
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
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level instanceof ServerLevel serverLevel) {
            if (!player.isShiftKeyDown()) {
                for (int i = 0; i < 10; i++) {
                    FallingBlockEntity.fall(serverLevel, player.blockPosition(), Blocks.POINTED_DRIPSTONE.defaultBlockState());
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (context.getLevel() instanceof ServerLevel serverLevel) {
            IceBlockEntity iceBlock = new IceBlockEntity(EntityRegistry.ICE_BLOCK.get(), serverLevel);
            iceBlock.moveTo(context.getClickLocation());
            iceBlock.addDeltaMovement(new Vec3(0, 0.5, 0));
            serverLevel.addFreshEntity(iceBlock);
        }
        return InteractionResult.SUCCESS;
    }
}
