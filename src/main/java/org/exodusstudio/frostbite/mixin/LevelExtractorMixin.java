package org.exodusstudio.frostbite.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.exodusstudio.frostbite.common.item.AdvancedThermalLensItem;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.exodusstudio.frostbite.common.mixinterfaces.TE;
import org.exodusstudio.frostbite.common.mixinterfaces.UUIDState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelExtractor.class)
public class LevelExtractorMixin {
    @Unique
    Minecraft frostbite$mc = Minecraft.getInstance();

    @Redirect(method = "extractVisibleEntities", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/extract/LevelExtractor;extractEntity(Lnet/minecraft/world/entity/Entity;F)Lnet/minecraft/client/renderer/entity/state/EntityRenderState;"))
    private EntityRenderState extractVisibleEntities(LevelExtractor instance, Entity entity, float partialTicks) {
        EntityRenderState entityrenderstate = instance.extractEntity(entity, partialTicks);
        ((UUIDState) entityrenderstate).frostbite$setUUID(entity.getUUID());

        assert frostbite$mc.player != null;
        if (frostbite$shouldShowEntityOutlines() && entity instanceof LivingEntity livingEntity &&
                (frostbite$mc.player.isCreative() || frostbite$mc.player.distanceTo(entity) < 30)) {
            int r = (int) Mth.lerp((((TE) livingEntity).getInnerTemp() + 60) / 80, 0, 255);
            int b = 255 - r;
            entityrenderstate.outlineColor = ARGB.color(128, r, 0, b);
        }

        return entityrenderstate;
    }

    @Unique
    public boolean frostbite$shouldShowEntityOutlines() {
        assert frostbite$mc.player != null;
        return (frostbite$mc.player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistry.THERMAL_LENS) ||
                frostbite$mc.player.getItemInHand(InteractionHand.OFF_HAND).is(ItemRegistry.THERMAL_LENS))
                && frostbite$mc.player.isUsingItem() && frostbite$mc.options.getCameraType().isFirstPerson();
    }

    @Unique
    public boolean frostbite$shouldShowEntityTemps() {
        assert frostbite$mc.player != null;
        return (frostbite$mc.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof AdvancedThermalLensItem ||
                frostbite$mc.player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof AdvancedThermalLensItem)
                && frostbite$mc.player.isUsingItem() && frostbite$mc.options.getCameraType().isFirstPerson();
    }
}
