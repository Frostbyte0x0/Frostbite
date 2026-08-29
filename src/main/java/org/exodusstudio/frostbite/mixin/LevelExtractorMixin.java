package org.exodusstudio.frostbite.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.item.AdvancedThermalLensItem;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.exodusstudio.frostbite.common.mixinterfaces.TE;
import org.exodusstudio.frostbite.common.mixinterfaces.UUIDState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelExtractor.class)
public class LevelExtractorMixin {
    @Unique
    LevelExtractor frostbite$levelExtractor = (LevelExtractor) ((Object) this);
    @Unique
    Minecraft frostbite$mc = Minecraft.getInstance();

    @Inject(at = @At("HEAD"), method = "extractVisibleEntities", cancellable = true)
    private void extractVisibleEntities(Camera camera, Frustum frustum, DeltaTracker deltaTracker, LevelRenderState output, CallbackInfo ci) {
        setFrostbite$levelRenderer();
        Vec3 vec3 = camera.position();
        double d0 = vec3.x();
        double d1 = vec3.y();
        double d2 = vec3.z();
        TickRateManager tickratemanager = frostbite$mc.level.tickRateManager();
        boolean flag = frostbite$levelExtractor.shouldShowEntityOutlines(camera);
        Entity.setViewScale(Mth.clamp(frostbite$mc.options.getEffectiveRenderDistance() / 8f, 1, 2.5) * frostbite$mc.options.entityDistanceScaling().get());

        assert frostbite$levelExtractor.level != null;
        assert frostbite$mc.player != null;
        for (Entity entity : frostbite$levelExtractor.level.entitiesForRendering()) {
            if (frostbite$levelExtractor.levelRenderer.entityRenderDispatcher.shouldRender(entity, frustum, d0, d1, d2) || entity.hasIndirectPassenger(frostbite$mc.player)) {
                BlockPos blockpos = entity.blockPosition();
                if ((frostbite$levelExtractor.level.isOutsideBuildHeight(blockpos.getY()) || frostbite$levelExtractor.levelRenderer.isSectionCompiledAndVisible(blockpos)) && (entity != camera.entity() || camera.isDetached() || camera.entity() instanceof LivingEntity && ((LivingEntity)camera.entity()).isSleeping()) && (!(entity instanceof LocalPlayer) || camera.entity() == entity || entity == frostbite$mc.player && !frostbite$mc.player.isSpectator())) {
                    if (entity.tickCount == 0) {
                        entity.xOld = entity.getX();
                        entity.yOld = entity.getY();
                        entity.zOld = entity.getZ();
                    }

                    float f = deltaTracker.getGameTimeDeltaPartialTick(!tickratemanager.isEntityFrozen(entity));
                    EntityRenderState entityrenderstate = frostbite$levelExtractor.extractEntity(entity, f);
                    ((UUIDState) entityrenderstate).frostbite$setUUID(entity.getUUID());

                    if (frostbite$shouldShowEntityOutlines() && entity instanceof LivingEntity livingEntity &&
                            (frostbite$mc.player.isCreative() || frostbite$mc.player.distanceTo(entity) < 30)) {
                        int r = (int) Mth.lerp((((TE) livingEntity).getInnerTemp() + 60) / 80, 0, 255);
                        int b = 255 - r;
                        entityrenderstate.outlineColor = ARGB.color(128, r, 0, b);
                    } else if (flag && frostbite$mc.shouldEntityAppearGlowing(entity)) {
                        int i = entity.getTeamColor();
                        entityrenderstate.outlineColor = ARGB.color(255, ARGB.red(i), ARGB.green(i), ARGB.blue(i));
                    }

                    output.entityRenderStates.add(entityrenderstate);
                    if (entityrenderstate.appearsGlowing() && flag) {
                        output.shouldShowEntityOutlines = true;
                    }
                }
            }
        }
        ci.cancel();
    }

    @Unique
    public boolean frostbite$shouldShowEntityOutlines() {
        assert frostbite$mc.player != null;
        return (frostbite$mc.player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistry.THERMAL_LENS) ||
                frostbite$mc.player.getItemInHand(InteractionHand.OFF_HAND).is(ItemRegistry.THERMAL_LENS))
                && frostbite$mc.player.isUsingItem() && frostbite$mc.options.getCameraType().isFirstPerson();
    }

    @Unique
    public void setFrostbite$levelRenderer() {
        if (frostbite$levelExtractor == null) {
            frostbite$levelExtractor = (LevelExtractor) ((Object) this);
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
