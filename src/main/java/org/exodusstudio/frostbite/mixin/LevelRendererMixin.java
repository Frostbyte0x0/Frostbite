package org.exodusstudio.frostbite.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Unique
    LevelRenderer frostbite$levelRenderer = (LevelRenderer) ((Object) this);
    @Unique
    Minecraft frostbite$mc = Minecraft.getInstance();

    @Inject(at = @At("HEAD"), method = "extractVisibleEntities", cancellable = true)
    private void extractVisibleEntities(Camera camera, Frustum frustum, DeltaTracker deltaTracker, LevelRenderState renderState, CallbackInfo ci) {
        setFrostbite$levelRenderer();
        Vec3 vec3 = camera.position();
        double d0 = vec3.x();
        double d1 = vec3.y();
        double d2 = vec3.z();
        TickRateManager tickratemanager = frostbite$mc.level.tickRateManager();
        boolean flag = frostbite$levelRenderer.shouldShowEntityOutlines();
        Entity.setViewScale(Mth.clamp(frostbite$mc.options.getEffectiveRenderDistance() / 8f, 1, 2.5) * frostbite$mc.options.entityDistanceScaling().get());

        assert frostbite$levelRenderer.level != null;
        for (Entity entity : frostbite$levelRenderer.level.entitiesForRendering()) {
            if (frostbite$levelRenderer.entityRenderDispatcher.shouldRender(entity, frustum, d0, d1, d2) || entity.hasIndirectPassenger(frostbite$mc.player)) {
                BlockPos blockpos = entity.blockPosition();
                if ((frostbite$levelRenderer.level.isOutsideBuildHeight(blockpos.getY()) || frostbite$levelRenderer.isSectionCompiledAndVisible(blockpos)) && (entity != camera.entity() || camera.isDetached() || camera.entity() instanceof LivingEntity && ((LivingEntity)camera.entity()).isSleeping()) && (!(entity instanceof LocalPlayer) || camera.entity() == entity || entity == frostbite$mc.player && !frostbite$mc.player.isSpectator())) {
                    if (entity.tickCount == 0) {
                        entity.xOld = entity.getX();
                        entity.yOld = entity.getY();
                        entity.zOld = entity.getZ();
                    }

                    float f = deltaTracker.getGameTimeDeltaPartialTick(!tickratemanager.isEntityFrozen(entity));
                    EntityRenderState entityrenderstate = frostbite$levelRenderer.extractEntity(entity, f);


                    if (frostbite$shouldShowEntityOutlines() && entity instanceof LivingEntity livingEntity && frostbite$mc.player.distanceTo(entity) < 30) {
                        int r = (int) Mth.lerp((Frostbite.temperatureStorage.getTemperature(livingEntity, true) + 60) / 80, 0, 255);
                        int b = 255 - r;
                        entityrenderstate.outlineColor = ARGB.color(128, r, 0, b);
                    } else if (flag && frostbite$mc.shouldEntityAppearGlowing(entity)) {
                        int i = entity.getTeamColor();
                        entityrenderstate.outlineColor = ARGB.color(255, ARGB.red(i), ARGB.green(i), ARGB.blue(i));
                    }

                    renderState.entityRenderStates.add(entityrenderstate);
                    if (entityrenderstate.appearsGlowing() && flag) {
                        renderState.haveGlowingEntities = true;
                    } else if (flag && entity.hasCustomOutlineRendering(frostbite$mc.player)) {
                        renderState.haveGlowingEntities = true;
                    }
                }
            }
        }
        ci.cancel();
    }

    
//    @Inject(at = @At("HEAD"), method = "renderEntities", cancellable = true)
//    public void renderEntities(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, Camera camera, DeltaTracker deltaTracker, List<Entity> entities, CallbackInfo ci) {
//        assert frostbite$mc.level != null;
//
//        setFrostbite$levelRenderer();
//        Vec3 vec3 = camera.position();
//        double d0 = vec3.x();
//        double d1 = vec3.y();
//        double d2 = vec3.z();
//        TickRateManager tickratemanager = frostbite$mc.level.tickRateManager();
//        boolean flag = frostbite$levelRenderer.shouldShowEntityOutlines();
//
//        for (Entity entity : entities) {
////            if (!(entity instanceof Strider || entity instanceof Stray)) {
////                continue;
////            }
//
//            if (entity.tickCount == 0) {
//                entity.xOld = entity.getX();
//                entity.yOld = entity.getY();
//                entity.zOld = entity.getZ();
//            }
//
//            MultiBufferSource multibuffersource;
//            assert frostbite$mc.player != null;
//
//            if (frostbite$shouldShowEntityOutlines() && entity instanceof LivingEntity livingEntity && frostbite$mc.player.distanceTo(entity) < 30) {
//                //Frostbite.LOGGER.debug("Z");
//                OutlineBufferSource outlinebuffersource = frostbite$levelRenderer.renderBuffers.outlineBufferSource();
//                multibuffersource = outlinebuffersource;
//                int r = (int) Mth.lerp((Frostbite.temperatureStorage.getTemperature(livingEntity, true) + 60) / 80, 0, 255);
//                int b = 255 - r;
//                outlinebuffersource.setColor(ARGB.color(r, 0, b, 128));
//            } else {
//                //Frostbite.LOGGER.debug("A");
//                if ((flag && frostbite$mc.shouldEntityAppearGlowing(entity))) {
//                    //Frostbite.LOGGER.debug("B");
//                    OutlineBufferSource outlinebuffersource = frostbite$levelRenderer.renderBuffers.outlineBufferSource();
//                    multibuffersource = outlinebuffersource;
//                    int i = entity.getTeamColor();
//                    outlinebuffersource.setColor(ARGB.color(ARGB.red(i), ARGB.green(i), ARGB.blue(i), 255));
//                } else {
//                    //Frostbite.LOGGER.debug("C");
//                    multibuffersource = bufferSource;
//                }
//            }
//
//            float f = deltaTracker.getGameTimeDeltaPartialTick(!tickratemanager.isEntityFrozen(entity));
//            frostbite$levelRenderer.renderEntity(entity, d0, d1, d2, f, poseStack, multibuffersource);
//        }
//        ci.cancel();
//    }

    @Unique
    public boolean frostbite$shouldShowEntityOutlines() {
        assert frostbite$mc.player != null;
        return (frostbite$mc.player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistry.THERMAL_LENS) ||
                frostbite$mc.player.getItemInHand(InteractionHand.OFF_HAND).is(ItemRegistry.THERMAL_LENS))
                && frostbite$mc.player.isUsingItem() && frostbite$mc.options.getCameraType().isFirstPerson();
    }

    @Unique
    public void setFrostbite$levelRenderer() {
        if (frostbite$levelRenderer == null) {
            frostbite$levelRenderer = (LevelRenderer) ((Object) this);
        }
    }
}
