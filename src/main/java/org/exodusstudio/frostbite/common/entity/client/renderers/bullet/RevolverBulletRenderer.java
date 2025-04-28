package org.exodusstudio.frostbite.common.entity.client.renderers.bullet;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.ResourceLocation;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.bullet.RevolverBulletModel;
import org.exodusstudio.frostbite.common.entity.custom.bullets.RevolverBulletEntity;

public class RevolverBulletRenderer extends BulletRenderer<RevolverBulletEntity, ArrowRenderState> {
    public RevolverBulletRenderer(EntityRendererProvider.Context context) {
        super(context, new RevolverBulletModel(context.bakeLayer(ModModelLayers.REVOLVER_BULLET)));
    }

    @Override
    public ArrowRenderState createRenderState() {
        return new ArrowRenderState();
    }

    @Override
    protected ResourceLocation getTextureLocation(ArrowRenderState arrowRenderState) {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/projectiles/revolver_bullet.png");
    }
}
