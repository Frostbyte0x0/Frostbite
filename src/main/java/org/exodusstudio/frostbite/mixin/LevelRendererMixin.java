package org.exodusstudio.frostbite.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.util.ARGB;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Unique
    LevelRenderer frostbite$levelRenderer = (LevelRenderer) ((Object) this);
    
    @Inject(at = @At("HEAD"), method = "renderEntities", cancellable = true)
    public void renderEntities(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, Camera camera, DeltaTracker deltaTracker, List<Entity> entities, CallbackInfo ci) {
        Vec3 vec3 = camera.getPosition();
        double d0 = vec3.x();
        double d1 = vec3.y();
        double d2 = vec3.z();
        Minecraft mc = Minecraft.getInstance();
        assert mc.level != null;
        TickRateManager tickratemanager = mc.level.tickRateManager();
        boolean flag = frostbite$levelRenderer.shouldShowEntityOutlines();

        for (Entity entity : entities) {
            if (entity.tickCount == 0) {
                entity.xOld = entity.getX();
                entity.yOld = entity.getY();
                entity.zOld = entity.getZ();
            }

            MultiBufferSource multibuffersource;
            assert mc.player != null;
            if (mc.player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistry.THERMAL_LENS) || (flag && mc.shouldEntityAppearGlowing(entity))) {
                OutlineBufferSource outlinebuffersource = frostbite$levelRenderer.renderBuffers.outlineBufferSource();
                multibuffersource = outlinebuffersource;
                int i = entity.getTeamColor();
                outlinebuffersource.setColor(ARGB.red(i), ARGB.green(i), ARGB.blue(i), 255);
            } else {
                multibuffersource = bufferSource;
            }

            float f = deltaTracker.getGameTimeDeltaPartialTick(!tickratemanager.isEntityFrozen(entity));
            frostbite$levelRenderer.renderEntity(entity, d0, d1, d2, f, poseStack, multibuffersource);
        }
        ci.cancel();
    }
}
