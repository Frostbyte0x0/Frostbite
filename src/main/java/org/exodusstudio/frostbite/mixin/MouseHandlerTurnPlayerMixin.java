package org.exodusstudio.frostbite.mixin;

import com.mojang.blaze3d.Blaze3D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.EffectRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Set;

@Mixin(MouseHandler.class)
public class MouseHandlerTurnPlayerMixin {

    @Shadow private double accumulatedDX;
    @Shadow private double accumulatedDY;
    @Unique
    double frostbite$time = 0;

    @Unique
    float frostbite$delay = (float) 7 / 60;

    @Unique
    double frostbite$timeIndex = 0;

    @Unique
    HashMap<Double, double[]> frostbite$mousePositionsDelayed = new HashMap<>();

    @Inject(at = @At("HEAD"), method = "handleAccumulatedMovement", cancellable = true)
    private void handleAccumulatedMovement(CallbackInfo ci) {
        // iChun wrote the line for when the game is paused, not me
        // https://github.com/iChun/MultiplayerServerPause/blob/1.21.4/common/src/main/java/me/ichun/mods/serverpause/client/core/EventHandlerClient.java#L33
        Minecraft mc = Minecraft.getInstance();
        boolean gamePaused = mc.getConnection() != null && mc.getConnection().getConnection().isConnected() && (mc.screen != null && mc.screen.isPauseScreen() || mc.getOverlay() != null && mc.getOverlay().isPauseScreen());

        if (mc.player != null && mc.player.hasEffect(EffectRegistry.FATIGUE) && !gamePaused) {
            frostbite$time = Blaze3D.getTime();
            frostbite$mousePositionsDelayed.put(frostbite$time + frostbite$delay, new double[]{this.accumulatedDX, this.accumulatedDY});

            frostbite$timeIndex = frostbite$findClosestFromSet(frostbite$time, frostbite$mousePositionsDelayed.keySet());

            this.accumulatedDX = frostbite$mousePositionsDelayed.get(frostbite$timeIndex)[0];
            this.accumulatedDY = frostbite$mousePositionsDelayed.get(frostbite$timeIndex)[1];
            if (frostbite$mousePositionsDelayed.size() >= mc.getFps() * frostbite$delay) {
                frostbite$mousePositionsDelayed.remove(frostbite$findClosestFromSet(0D, frostbite$mousePositionsDelayed.keySet()));
            }
            //ci.cancel();
        }
    }

    @Unique
    public Double frostbite$findClosestFromSet(double value, Set<Double> set) {
        double smallestDifference = (double) set.toArray()[0];
        double closestResult = (double) set.toArray()[0];
        for (double key : set) {
            if (Math.abs(key - value) < smallestDifference) {
                smallestDifference = Math.abs(key - value);
                closestResult = key;
            }
        }
        return closestResult;
    }
}
