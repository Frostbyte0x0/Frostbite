package org.exodusstudio.frostbite.mixin;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.exodusstudio.frostbite.common.entity.custom.misc.FrozenRemnantsEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(at = @At("HEAD"), method = "shouldRender", cancellable = true)
    private <E extends Entity> void shouldRender(E entity, Frustum frustum, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof FrozenRemnantsEntity) {
            cir.setReturnValue(true);
        }
    }
}
