package org.exodusstudio.frostbite.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.exodusstudio.frostbite.common.component.ContractAttributeData;
import org.exodusstudio.frostbite.common.contracts.ContractAttribute;
import org.exodusstudio.frostbite.common.contracts.ContractAttributes;
import org.exodusstudio.frostbite.common.item.contract.ContractFragmentItem;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.util.helpers.DataHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootTable.class)
public class LootTableMixin {
    @Unique
    private static final RandomSource frostbite$random = RandomSource.create();

    @Inject(at = @At("TAIL"), method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;", cancellable = true)
    private void getRandomItems(LootContext context, CallbackInfoReturnable<ObjectArrayList<ItemStack>> cir) {
        ObjectArrayList<ItemStack> items = cir.getReturnValue();
        for (ItemStack stack : items) {
            if (stack.getItem() instanceof ContractFragmentItem) {
                ContractAttribute attribute = ContractAttributes.ATTRIBUTES.values().stream().toList()
                        .get(frostbite$random.nextIntBetweenInclusive(0, ContractAttributes.ATTRIBUTES.size() - 1));
                stack.set(DataComponentTypeRegistry.CONTRACT_ATTRIBUTE.get(), new ContractAttributeData(attribute));
                DataHelper.setData(stack, "level", frostbite$random.nextIntBetweenInclusive(1, 3));
            }
        }
        cir.setReturnValue(items);
    }
}
