package org.exodusstudio.frostbite.common.item.contract;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.contracts.Contract;
import org.exodusstudio.frostbite.common.contracts.ContractTarget;
import org.exodusstudio.frostbite.common.contracts.LivingContractInfo;

public class ContractItem extends Item {
    public ContractItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        if (!player.isShiftKeyDown()) return InteractionResult.PASS;

        Contract c;
        if ((c = Contract.getContract(player.getItemInHand(usedHand))) == null) return InteractionResult.PASS;
        if (c.getStrictestTarget() != ContractTarget.PLAYER && c.getStrictestTarget() != ContractTarget.LIVING) return InteractionResult.PASS;

        LivingContractInfo.setContract(player, c);
        Frostbite.LOGGER.debug("Added contract {} to player {}", c, player.getName().getString());

        if (level.isClientSide()) {
            Minecraft mc = Minecraft.getInstance();
            level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.TRIDENT_THUNDER.value(), player.getSoundSource(), 1, 0.25f, false);
            if (player == mc.player) {
                mc.gameRenderer.displayItemActivation(player.getItemInHand(usedHand));
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }
}