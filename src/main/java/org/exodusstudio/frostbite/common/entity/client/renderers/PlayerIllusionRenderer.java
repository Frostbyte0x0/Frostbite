package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.exodusstudio.frostbite.common.entity.custom.misc.PlayerIllusionEntity;

public class PlayerIllusionRenderer extends EntityRenderer<PlayerIllusionEntity, LivingEntityRenderState> {
    public PlayerIllusionRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }
}
