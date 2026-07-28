package org.exodusstudio.frostbite.mixin;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import org.exodusstudio.frostbite.common.contracts.ContractAttributes;
import org.exodusstudio.frostbite.common.contracts.LivingContractInfo;
import org.exodusstudio.frostbite.common.util.Util;
import org.exodusstudio.frostbite.common.util.helpers.DataHelper;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(CraftingMenu.class)
public class CraftingMenuMixin {
    @Inject(at = @At("TAIL"), method = "removed")
    private void removed(Player player, CallbackInfo ci) {
        DataHelper.setData(player, "tried_craft_results", "");
    }

    @Inject(at = @At("HEAD"), method = "slotChangedCraftingGrid", cancellable = true)
    private static void slotChangedCraftingGrid(AbstractContainerMenu menu, ServerLevel level, Player player, CraftingContainer container, ResultContainer resultSlots, @Nullable RecipeHolder<CraftingRecipe> recipeHint, CallbackInfo ci) {
        if (LivingContractInfo.hasAppliedAttribute(player, ContractAttributes.DEXTEROUS) &&
                Util.random.nextFloat() < LivingContractInfo.getStat(player, ContractAttributes.DEXTEROUS) / 100) {
            CraftingInput input = container.asCraftInput();
            ServerPlayer serverPlayer = (ServerPlayer)player;
            ItemStack result = ItemStack.EMPTY;
            Optional<RecipeHolder<CraftingRecipe>> maybeRecipe = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, level, recipeHint);
            boolean valid = true;
            if (maybeRecipe.isPresent()) {
                RecipeHolder<CraftingRecipe> recipeHolder = maybeRecipe.get();
                CraftingRecipe craftingRecipe = recipeHolder.value();
                if (resultSlots.setRecipeUsed(serverPlayer, recipeHolder)) {
                    ItemStack recipeResult = craftingRecipe.assemble(input);
                    if (recipeResult.isItemEnabled(level.enabledFeatures())) {
                        result = recipeResult;
                        valid = !Util.isLoopableRecipe(level, result, craftingRecipe);
                    }
                }
            }

            if (DataHelper.getString(player, "tried_craft_results").contains(result.getItemName().getString())) return;

            DataHelper.setData(player, "tried_craft_results",
                    DataHelper.getString(player, "tried_craft_results") + result.getItemName().getString() + ";");

            if (valid) result.setCount(Math.min(result.getItem().getDefaultMaxStackSize(), result.count()) + 1);
            resultSlots.setItem(0, result);
            menu.setRemoteSlot(0, result);
            serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, result));
            ci.cancel();
        }
    }
}
