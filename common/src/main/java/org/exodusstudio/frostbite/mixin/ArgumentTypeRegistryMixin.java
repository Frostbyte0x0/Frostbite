package org.exodusstudio.frostbite.mixin;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Registry;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.commands.FragmentArgument;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArgumentTypeInfos.class)
public class ArgumentTypeRegistryMixin {
    @Inject(method = "bootstrap", at = @At("RETURN"))
    private static void registerArgument(Registry<ArgumentTypeInfo<?, ?>> registry, CallbackInfoReturnable<ArgumentTypeInfo<?, ?>> cir) {
        ArgumentTypeInfos.register(registry, Frostbite.MOD_ID + ":fragment_argument", FragmentArgument.class, SingletonArgumentInfo.contextFree(FragmentArgument::create));
    }
}
