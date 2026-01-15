package org.exodusstudio.frostbite.mixin;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.exodusstudio.frostbite.common.util.UUIDState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements UUIDState {
    @Unique
    public UUID frostbite$uuid;

    @Override
    public UUID frostbite$getUUID() {
        return frostbite$uuid;
    }

    @Override
    public void frostbite$setUUID(UUID uuid) {
        this.frostbite$uuid = uuid;
    }
}
