package org.exodusstudio.frostbite.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.Frostbite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static org.exodusstudio.frostbite.common.util.Util.isFrostbite;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Unique
    ClientLevel frostbite$clientLevel = (ClientLevel) ((Object) this);

    @Inject(at = @At("HEAD"), method = "getSkyColor", cancellable = true)
    private void getSkyColor(Vec3 cameraPosition, float partialTick, CallbackInfoReturnable<Integer> cir) {
        if (isFrostbite(frostbite$clientLevel)) {
            float t = Frostbite.weatherInfo.getLerp();

            int red = (int) (Mth.lerp(t, Frostbite.weatherInfo.oRed, Frostbite.weatherInfo.red) * 255);
            int green = (int) (Mth.lerp(t, Frostbite.weatherInfo.oGreen, Frostbite.weatherInfo.green) * 255);
            int blue = (int) (Mth.lerp(t, Frostbite.weatherInfo.oBlue, Frostbite.weatherInfo.blue) * 255);

            cir.setReturnValue(ARGB.color(red, green, blue));
        }
    }
}
