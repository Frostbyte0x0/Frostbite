package org.exodusstudio.frostbite.common.item.custom.weapons.elf_weapons;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.ArrayUtils;
import org.exodusstudio.frostbite.common.component.ChargeData;
import org.exodusstudio.frostbite.common.component.ModeData;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;

import java.util.List;
import java.util.Optional;

public class DrainingStaffItem extends Item {
    private final int max_charge = 100;
    private final String[] modes = {"drain", "blast", "ray", "shield", "heal"};
    private final Integer[] costs = {0, 80, 100, 50, 70};

    public DrainingStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        ItemUtils.startUsingInstantly(level, player, interactionHand);
        checkComponentDataForNull(player.getItemInHand(interactionHand));

        int modeIndex = ArrayUtils.indexOf(modes, player.getItemInHand(interactionHand).get(DataComponentTypeRegistry.MODE.get()).mode());
        int charge = player.getItemInHand(interactionHand).get(DataComponentTypeRegistry.CHARGE.get()).charge();

        if (player.isShiftKeyDown()) {
            modeIndex += 1;
            if (modeIndex == modes.length) modeIndex = 0;
            player.getItemInHand(interactionHand).set(DataComponentTypeRegistry.MODE.get(),
                    new ModeData(modes[modeIndex]));
        } else {
            switch (player.getItemInHand(interactionHand).get(DataComponentTypeRegistry.MODE.get()).mode()) {
                case "drain":
                    if (charge < max_charge) reduceCharge(player, interactionHand, -10);
                    break;
                case "blast":
                    if (charge >= costs[1]) {
                        blastOption(player);
                        reduceCharge(player, interactionHand, costs[1]);
                    } else {
                        notifyPlayerNotEnoughCharge(player);
                    }
                    break;
                case "ray":
                    if (charge >= costs[2]) {
                        rayOption(player);
                        reduceCharge(player, interactionHand, costs[2]);
                    } else {
                        notifyPlayerNotEnoughCharge(player);
                    }
                    break;
                case "shield":
                    if (charge >= costs[3]) {
                        shieldOption(player);
                        reduceCharge(player, interactionHand, costs[3]);
                    } else {
                        notifyPlayerNotEnoughCharge(player);
                    }
                    break;
                case "heal":
                    if (charge >= costs[4]) {
                        healOption(player);
                        reduceCharge(player, interactionHand, costs[4]);
                    } else {
                        notifyPlayerNotEnoughCharge(player);
                    }
                    break;
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof Player player) {
            if ((player.isUsingItem() && isSelected) || player.isScoping()) {
                player.displayClientMessage(Component.literal("Wow cool"), true);
            }
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }


    public void blastOption(Player player) {
        player.displayClientMessage(Component.literal("Blast away!"), true);
    }

    public void rayOption(Player player) {
        final int range = 10;
        Vec3 vec3 = player.position().add(player.getAttachments().get(EntityAttachment.WARDEN_CHEST, 0, player.getYRot()));
        Vec3 vec31 = player.getLookAngle();
        Vec3 vec32 = vec31.normalize();
        
        for (int j = 1; j < range; j++) {
            Vec3 vec33 = vec3.add(vec32.scale(j));
            player.level().addAlwaysVisibleParticle(ParticleTypes.SONIC_BOOM, vec33.x, vec33.y, vec33.z, 1, 0.0, 0.0);
        }

        player.playSound(SoundEvents.WARDEN_SONIC_BOOM, 3.0F, 1.0F);

        if (player.level() instanceof ServerLevel serverLevel) {
            Vec3 end = vec3.add(vec32.scale(range));

            for (Entity entity1 : player.level().getEntities(player, new AABB(vec3, end).inflate(1.0))) {
                AABB aabb = entity1.getBoundingBox().inflate(1);
                Optional<Vec3> optional1 = aabb.clip(vec3, end);
                if (optional1.isPresent()) {
                    double d1 = 0.5 * (1.0 - player.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                    double d0 = 2.5 * (1.0 - player.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                    entity1.hurtServer(serverLevel, player.damageSources().generic(), 10f);
                    entity1.push(vec32.x() * d0, vec32.y() * d1, vec32.z() * d0);
                }
            }
        }
    }

    public void shieldOption(Player player) {
        player.displayClientMessage(Component.literal("Shielding"), true);
    }

    public void healOption(Player player) {
        player.heal(7);
        player.displayClientMessage(Component.literal("Healing"), true);
    }


    public void reduceCharge(Player player, InteractionHand hand, int amount) {
        if (amount > 0 && player.isCreative()) {
            return;
        }
        player.getItemInHand(hand).set(DataComponentTypeRegistry.CHARGE.get(),
                new ChargeData(player.getItemInHand(hand).get(DataComponentTypeRegistry.CHARGE.get()).charge() - amount));
    }

    public void checkComponentDataForNull(ItemStack stack) {
        if (!stack.has(DataComponentTypeRegistry.MODE)) {
            stack.set(DataComponentTypeRegistry.MODE.get(), new ModeData("drain"));
        }
        if (!stack.has(DataComponentTypeRegistry.CHARGE)) {
            stack.set(DataComponentTypeRegistry.CHARGE.get(), new ChargeData(0));
        }
    }

    public void notifyPlayerNotEnoughCharge(Player player) {
        player.playSound(SoundEvents.THORNS_HIT);
        player.displayClientMessage(Component.translatable("tooltip.frostbite.draining_staff.not_enough_charge"), true);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        checkComponentDataForNull(stack);
        tooltipComponents.add(Component.literal(String.format("Charge: %s  Mode: %s",
                stack.get(DataComponentTypeRegistry.CHARGE.get()).charge(), stack.get(DataComponentTypeRegistry.MODE.get()).mode())).withStyle(ChatFormatting.GRAY));
    }
}
