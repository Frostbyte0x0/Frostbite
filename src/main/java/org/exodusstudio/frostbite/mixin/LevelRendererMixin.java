package org.exodusstudio.frostbite.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.Frostbite;
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
    @Unique
    Minecraft mc = Minecraft.getInstance();

    @Inject(at = @At("HEAD"), method = "doEntityOutline", cancellable = true)
    public void doEntityOutline(CallbackInfo ci) {
        setFrostbite$levelRenderer();
        // frostbite$shouldShowEntityOutlines() &&
        if (frostbite$levelRenderer != null) {
            if (frostbite$levelRenderer.shouldShowEntityOutlines()) {
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
                frostbite$levelRenderer.entityOutlineTarget.blitAndBlendToScreen(mc.getWindow().getWidth(), mc.getWindow().getHeight());
                RenderSystem.disableBlend();
                RenderSystem.defaultBlendFunc();
            }
        }
        ci.cancel();
    }
    
    @Inject(at = @At("HEAD"), method = "renderEntities", cancellable = true)
    public void renderEntities(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, Camera camera, DeltaTracker deltaTracker, List<Entity> entities, CallbackInfo ci) {
        setFrostbite$levelRenderer();
        Vec3 vec3 = camera.getPosition();
        double d0 = vec3.x();
        double d1 = vec3.y();
        double d2 = vec3.z();
        TickRateManager tickratemanager = mc.level.tickRateManager();
        boolean flag = frostbite$levelRenderer.shouldShowEntityOutlines();

        for (Entity entity : entities) {
//            if (!(entity instanceof Strider || entity instanceof Stray)) {
//                continue;
//            }

            if (entity.tickCount == 0) {
                entity.xOld = entity.getX();
                entity.yOld = entity.getY();
                entity.zOld = entity.getZ();
            }

            MultiBufferSource multibuffersource;
            assert mc.player != null;

            if (frostbite$shouldShowEntityOutlines() && entity instanceof LivingEntity livingEntity && mc.player.distanceTo(entity) < 30) {
                //Frostbite.LOGGER.debug("Z");
                OutlineBufferSource outlinebuffersource = frostbite$levelRenderer.renderBuffers.outlineBufferSource();
                multibuffersource = outlinebuffersource;
                int r = (int) Mth.lerp((Frostbite.temperatures.getTemperature(livingEntity, true) + 60) / 80, 0, 255);
                int b = 255 - r;
                outlinebuffersource.setColor(r, 0, b, 128);
            } else {
                //Frostbite.LOGGER.debug("A");
                if ((flag && mc.shouldEntityAppearGlowing(entity))) {
                    //Frostbite.LOGGER.debug("B");
                    OutlineBufferSource outlinebuffersource = frostbite$levelRenderer.renderBuffers.outlineBufferSource();
                    multibuffersource = outlinebuffersource;
                    int i = entity.getTeamColor();
                    outlinebuffersource.setColor(ARGB.red(i), ARGB.green(i), ARGB.blue(i), 255);
                } else {
                    //Frostbite.LOGGER.debug("C");
                    multibuffersource = bufferSource;
                }
            }

            float f = deltaTracker.getGameTimeDeltaPartialTick(!tickratemanager.isEntityFrozen(entity));
            frostbite$levelRenderer.renderEntity(entity, d0, d1, d2, f, poseStack, multibuffersource);
        }
        ci.cancel();
    }

    @Unique
    public boolean frostbite$shouldShowEntityOutlines() {
        Minecraft mc = Minecraft.getInstance();
        return (mc.player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistry.THERMAL_LENS) ||
                mc.player.getItemInHand(InteractionHand.OFF_HAND).is(ItemRegistry.THERMAL_LENS))
                && mc.player.isUsingItem() && mc.options.getCameraType().isFirstPerson();
    }

    @Unique
    public void setFrostbite$levelRenderer() {
        if (frostbite$levelRenderer == null) {
            frostbite$levelRenderer = (LevelRenderer) ((Object) this);
        }
    }
}
