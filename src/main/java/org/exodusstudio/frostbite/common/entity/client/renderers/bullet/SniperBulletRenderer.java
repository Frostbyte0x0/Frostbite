package org.exodusstudio.frostbite.common.entity.client.renderers.bullet;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.bullet.SniperBulletModel;
import org.exodusstudio.frostbite.common.entity.custom.bullets.SniperBulletEntity;

public class SniperBulletRenderer extends BulletRenderer<SniperBulletEntity, ArrowRenderState> {
    public SniperBulletRenderer(EntityRendererProvider.Context context) {
        super(context, new SniperBulletModel(context.bakeLayer(ModModelLayers.SNIPER_BULLET)));
    }

    @Override
    public ArrowRenderState createRenderState() {
        return new ArrowRenderState();
    }

    @Override
    protected Identifier getTextureLocation(ArrowRenderState arrowRenderState) {
        return Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/projectiles/sniper_bullet.png");
    }
}
