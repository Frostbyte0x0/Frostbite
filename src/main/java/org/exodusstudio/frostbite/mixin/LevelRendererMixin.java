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
import org.exodusstudio.frostbite.common.item.AdvancedThermalLensItem;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.exodusstudio.frostbite.common.util.UUIDState;
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
        assert frostbite$mc.player != null;
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
                    ((UUIDState) entityrenderstate).frostbite$setUUID(entity.getUUID());

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
//
//    @Inject(at = @At("HEAD"), method = "addLateDebugPass")
//    private void addLateDebugPass(FrameGraphBuilder p_361973_, CameraRenderState p_455807_, GpuBufferSlice p_418393_, Matrix4f p_454977_, CallbackInfo ci) {
//        if (frostbite$shouldShowEntityTemps()) {
//            LevelRenderState renderState = frostbite$levelRenderer.levelRenderState;
//            Vec3 vec3 = renderState.cameraRenderState.pos;
//            double d0 = vec3.x();
//            double d1 = vec3.y();
//            double d2 = vec3.z();
//
//            for (EntityRenderState entityrenderstate : renderState.entityRenderStates) {
//                PoseStack poseStack = new PoseStack();
//
//                EntityRenderer<?, ? super EntityRenderState> entityrenderer = frostbite$levelRenderer.entityRenderDispatcher.getRenderer(entityrenderstate);
//
//                Vec3 vec31 = entityrenderer.getRenderOffset(entityrenderstate);
//                double d3 = d0 + vec31.x();
//                double d4 = d1 + 2;
//                double d5 = d2 + vec31.z();
//                poseStack.pushPose();
//                poseStack.translate(d3, d4, d5);
//
//                poseStack.pushPose();
//                poseStack.translate(0, -entityrenderstate.eyeHeight / 2, 0);
//                frostbite$levelRenderer.submitNodeStorage.submitNameTag(poseStack, entityrenderstate.nameTagAttachment, 0, Component.literal("Top"),
//                        !entityrenderstate.isDiscrete, entityrenderstate.lightCoords, entityrenderstate.distanceToCameraSq, renderState.cameraRenderState);
//                poseStack.translate(0, -0.3, 0);
//                frostbite$levelRenderer.submitNodeStorage.submitNameTag(poseStack, entityrenderstate.nameTagAttachment, 0,
//                        Component.literal("Frostbite.temperatureStorage.getTemperature(renderState.uuid)"),
//                        !entityrenderstate.isDiscrete, entityrenderstate.lightCoords, entityrenderstate.distanceToCameraSq, renderState.cameraRenderState);
//                poseStack.popPose();
//            }
//        }
//    }

//    @Inject(at = @At("HEAD"), method = "renderLevel", cancellable = true)
//    public void renderLevel(
//            GraphicsResourceAllocator graphicsResourceAllocator,
//            DeltaTracker deltaTracker,
//            boolean renderBlockOutline,
//            Camera camera,
//            Matrix4f frustumMatrix,
//            Matrix4f projectionMatrix,
//            Matrix4f cullingProjectionMatrix,
//            GpuBufferSlice shaderFog,
//            Vector4f fogColor,
//            boolean renderSky,
//            CallbackInfo ci
//    ) {
//        float f = deltaTracker.getGameTimeDeltaPartialTick(false);
//        frostbite$levelRenderer.levelRenderState.gameTime = frostbite$levelRenderer.level.getGameTime();
//        frostbite$levelRenderer.blockEntityRenderDispatcher.prepare(camera);
//        frostbite$levelRenderer.entityRenderDispatcher.prepare(camera, frostbite$mc.crosshairPickEntity);
//        final ProfilerFiller profilerfiller = Profiler.get();
//        profilerfiller.push("populateLightUpdates");
//        frostbite$levelRenderer.level.pollLightUpdates();
//        profilerfiller.popPush("runLightUpdates");
//        frostbite$levelRenderer.level.getChunkSource().getLightEngine().runLightUpdates();
//        profilerfiller.popPush("prepareCullFrustum");
//        Vec3 vec3 = camera.position();
//        Frustum frustum = frostbite$levelRenderer.prepareCullFrustum(frustumMatrix, cullingProjectionMatrix, vec3);
//        profilerfiller.popPush("cullTerrain");
//        frostbite$levelRenderer.cullTerrain(camera, frustum, frostbite$mc.player.isSpectator());
//        profilerfiller.popPush("compileSections");
//        frostbite$levelRenderer.compileSections(camera);
//        profilerfiller.popPush("extract");
//        profilerfiller.push("entities");
//        frostbite$levelRenderer.extractVisibleEntities(camera, frustum, deltaTracker, frostbite$levelRenderer.levelRenderState);
//        profilerfiller.popPush("blockEntities");
//        frostbite$levelRenderer.extractVisibleBlockEntities(camera, f, frostbite$levelRenderer.levelRenderState, frustum);
//        profilerfiller.popPush("blockOutline");
//        frostbite$levelRenderer.extractBlockOutline(camera, frostbite$levelRenderer.levelRenderState);
//        profilerfiller.popPush("blockBreaking");
//        frostbite$levelRenderer.extractBlockDestroyAnimation(camera, frostbite$levelRenderer.levelRenderState);
//        profilerfiller.popPush("weather");
//        frostbite$levelRenderer.weatherEffectRenderer.extractRenderState(frostbite$levelRenderer.level, frostbite$levelRenderer.ticks, f, vec3, frostbite$levelRenderer.levelRenderState.weatherRenderState);
//        profilerfiller.popPush("sky");
//        frostbite$levelRenderer.skyRenderer.extractRenderState(frostbite$levelRenderer.level, f, camera, frostbite$levelRenderer.levelRenderState.skyRenderState);
//        profilerfiller.popPush("border");
//        frostbite$levelRenderer.worldBorderRenderer
//                .extract(
//                        frostbite$levelRenderer.level.getWorldBorder(), f, vec3, frostbite$mc.options.getEffectiveRenderDistance() * 16, frostbite$levelRenderer.levelRenderState.worldBorderRenderState
//                );
//        profilerfiller.popPush("neoforge_custom");
//        frostbite$levelRenderer.levelRenderState.customWeatherEffectRenderer = net.neoforged.neoforge.client.CustomEnvironmentEffectsRendererManager.getCustomWeatherEffectRenderer(frostbite$levelRenderer.level, camera.position());
//        frostbite$levelRenderer.levelRenderState.customSkyboxRenderer = net.neoforged.neoforge.client.CustomEnvironmentEffectsRendererManager.getCustomSkyboxRenderer(frostbite$levelRenderer.level, camera.position());
//        frostbite$levelRenderer.levelRenderState.customCloudsRenderer = net.neoforged.neoforge.client.CustomEnvironmentEffectsRendererManager.getCustomCloudsRenderer(frostbite$levelRenderer.level, camera.position());
//        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post(new net.neoforged.neoforge.client.event.ExtractLevelRenderStateEvent(
//                frostbite$levelRenderer, frostbite$levelRenderer.levelRenderState, frostbite$levelRenderer.level, camera, frustum, deltaTracker, frostbite$levelRenderer.ticks
//        ));
//        profilerfiller.pop();
//        profilerfiller.popPush("debug");
//        frostbite$levelRenderer.debugRenderer.emitGizmos(frustum, vec3.x, vec3.y, vec3.z, deltaTracker.getGameTimeDeltaPartialTick(false));
//        frostbite$levelRenderer.gameTestBlockHighlightRenderer.emitGizmos();
//        profilerfiller.popPush("setupFrameGraph");
//        Matrix4fStack matrix4fstack = RenderSystem.getModelViewStack();
//        matrix4fstack.pushMatrix();
//        matrix4fstack.mul(frustumMatrix);
//        FrameGraphBuilder framegraphbuilder = new FrameGraphBuilder();
//        frostbite$levelRenderer.targets.main = framegraphbuilder.importExternal("main", frostbite$mc.getMainRenderTarget());
//        int i = frostbite$mc.getMainRenderTarget().width;
//        int j = frostbite$mc.getMainRenderTarget().height;
//        RenderTargetDescriptor rendertargetdescriptor = new RenderTargetDescriptor(i, j, true, 0, frostbite$mc.getMainRenderTarget().useStencil);
//        PostChain postchain = frostbite$levelRenderer.getTransparencyChain();
//        if (postchain != null) {
//            frostbite$levelRenderer.targets.translucent = framegraphbuilder.createInternal("translucent", rendertargetdescriptor);
//            frostbite$levelRenderer.targets.itemEntity = framegraphbuilder.createInternal("item_entity", rendertargetdescriptor);
//            frostbite$levelRenderer.targets.particles = framegraphbuilder.createInternal("particles", rendertargetdescriptor);
//            frostbite$levelRenderer.targets.weather = framegraphbuilder.createInternal("weather", rendertargetdescriptor);
//            frostbite$levelRenderer.targets.clouds = framegraphbuilder.createInternal("clouds", rendertargetdescriptor);
//        }
//
//        if (frostbite$levelRenderer.entityOutlineTarget != null) {
//            frostbite$levelRenderer.targets.entityOutline = framegraphbuilder.importExternal("entity_outline", frostbite$levelRenderer.entityOutlineTarget);
//        }
//
//        var setupEvent = net.neoforged.neoforge.client.ClientHooks.fireFrameGraphSetup(framegraphbuilder, frostbite$levelRenderer.targets, rendertargetdescriptor, frustum, camera, frustumMatrix, projectionMatrix, deltaTracker, profilerfiller);
//        frostbite$levelRenderer.levelRenderState.haveGlowingEntities |= setupEvent.isOutlineProcessingEnabled();
//
//        FramePass framepass = framegraphbuilder.addPass("clear");
//        frostbite$levelRenderer.targets.main = framepass.readsAndWrites(frostbite$levelRenderer.targets.main);
//        framepass.executes(
//                () -> {
//                    RenderTarget rendertarget = frostbite$mc.getMainRenderTarget();
//                    RenderSystem.getDevice()
//                            .createCommandEncoder()
//                            .clearColorAndDepthTextures(
//                                    rendertarget.getColorTexture(), ARGB.colorFromFloat(0.0F, fogColor.x, fogColor.y, fogColor.z), rendertarget.getDepthTexture(), 1.0
//                            );
//                }
//        );
//        if (renderSky) {
//            frostbite$levelRenderer.addSkyPass(framegraphbuilder, camera, shaderFog, frustumMatrix);
//        }
//
//        frostbite$levelRenderer.addMainPass(framegraphbuilder, frustum, frustumMatrix, shaderFog, renderBlockOutline, frostbite$levelRenderer.levelRenderState, deltaTracker, profilerfiller);
//        PostChain postchain1 = frostbite$mc.getShaderManager().getPostChain(frostbite$levelRenderer.ENTITY_OUTLINE_POST_CHAIN_ID, LevelTargetBundle.OUTLINE_TARGETS);
//        if (frostbite$levelRenderer.levelRenderState.haveGlowingEntities && postchain1 != null) {
//            postchain1.addToFrame(framegraphbuilder, i, j, frostbite$levelRenderer.targets);
//        }
//
//        frostbite$mc.particleEngine.extract(frostbite$levelRenderer.particlesRenderState, new Frustum(frustum).offset(-3.0F), camera, f);
//        frostbite$levelRenderer.addParticlesPass(framegraphbuilder, shaderFog, frustumMatrix);
//        CloudStatus cloudstatus = frostbite$mc.options.getCloudsType();
//        if (cloudstatus != CloudStatus.OFF) {
//            int k = camera.attributeProbe().getValue(EnvironmentAttributes.CLOUD_COLOR, f);
//            if (ARGB.alpha(k) > 0) {
//                float f1 = camera.attributeProbe().getValue(EnvironmentAttributes.CLOUD_HEIGHT, f);
//                frostbite$levelRenderer.addCloudsPass(framegraphbuilder, cloudstatus, frostbite$levelRenderer.levelRenderState.cameraRenderState.pos, frostbite$levelRenderer.levelRenderState.gameTime, f, k, f1, frustumMatrix);
//            }
//        }
//
//        frostbite$levelRenderer.addWeatherPass(framegraphbuilder, shaderFog, frustumMatrix);
//        if (postchain != null) {
//            postchain.addToFrame(framegraphbuilder, i, j, frostbite$levelRenderer.targets);
//        }
//
//        frostbite$levelRenderer.addLateDebugPass(framegraphbuilder, frostbite$levelRenderer.levelRenderState.cameraRenderState, shaderFog, frustumMatrix);
//        profilerfiller.popPush("executeFrameGraph");
//        framegraphbuilder.execute(graphicsResourceAllocator, new FrameGraphBuilder.Inspector() {
//            @Override
//            public void beforeExecutePass(String p_363206_) {
//                profilerfiller.push(p_363206_);
//            }
//
//            @Override
//            public void afterExecutePass(String p_362054_) {
//                profilerfiller.pop();
//            }
//        });
//        frostbite$levelRenderer.targets.clear();
//        matrix4fstack.popMatrix();
//        profilerfiller.pop();
//        frostbite$levelRenderer.levelRenderState.reset();
//
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

    @Unique
    public boolean frostbite$shouldShowEntityTemps() {
        assert frostbite$mc.player != null;
        return (frostbite$mc.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof AdvancedThermalLensItem ||
                frostbite$mc.player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof AdvancedThermalLensItem)
                && frostbite$mc.player.isUsingItem() && frostbite$mc.options.getCameraType().isFirstPerson();
    }
}
