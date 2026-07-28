package org.exodusstudio.frostbite.common.item.weapons;

import net.minecraft.world.item.Item;

public class IceHammerItem extends Item {
    public IceHammerItem(Properties p_333796_) {
        super(p_333796_);
    }
//
//    public static ItemAttributeModifiers createAttributes() {
//        return ItemAttributeModifiers.builder()
//                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 10.0F,
//                        AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
//                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -3.4F,
//                        AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
//    }
//
//    public static Tool createToolProperties() {
//        return new Tool(List.of(), 1.0F, 2, true);
//    }
//
//    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
//        return !player.isCreative();
//    }
//
//    @Override
//    public InteractionResult use(Level level, Player player, InteractionHand hand) {
//        ItemStack stack = player.getItemInHand(hand);
//        if (stack.get(DataComponentTypeRegistry.CHARGE).charge() <= 0) {
//            stack.set(DataComponentTypeRegistry.CHARGE, new ChargeData(60));
//            if (level instanceof ServerLevel serverLevel) {
//                if (!player.isShiftKeyDown()) {
//                    for (int i = 1; i < 5; i++) {
//                        for (int j = 0; j < i; j++) {
//                            float angle = (-player.getYRot() * Mth.PI / 180) - ((float) j / 3) + (float) i / 6;
//                            serverLevel.addFreshEntity(new IceSpikeEntity(level,
//                                    player.getX() + 1.5 * i * Mth.sin(angle),
//                                    Math.floor(player.getY()),
//                                    player.getZ() + 1.5 * i * Mth.cos(angle),
//                                    (player.getYRot() + ((float) j / 3) - (float) i / 6), 0,2 * i, player, true));
//                        }
//                    }
//                } else {
//                    for (int i = 1; i < 3; i++) {
//                        for (int j = 0; j < i*4; j++) {
//                            float angle = Mth.PI * j / (i * 2);
//                            serverLevel.addFreshEntity(new IceSpikeEntity(level,
//                                    player.getX() + i * Mth.sin(angle),
//                                    Math.floor(player.getY()),
//                                    player.getZ() + i * Mth.cos(angle),
//                                    -angle, 0, 2 * i, player, true));
//                        }
//                    }
//                }
//            }
//        }
//
//        return InteractionResult.SUCCESS;
//    }
//
//    @Override
//    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
//        if (stack.get(DataComponentTypeRegistry.CHARGE).charge() > 0) {
//            stack.set(DataComponentTypeRegistry.CHARGE, new ChargeData(stack.get(DataComponentTypeRegistry.CHARGE).charge() - 1));
//        }
//    }
}
