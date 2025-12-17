package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.model.monster.zombie.ZombieModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.IcedZombieEntity;

public class IcedZombieRenderer extends MobRenderer<IcedZombieEntity, ZombieRenderState, ZombieModel<ZombieRenderState>> {
    public IcedZombieRenderer(EntityRendererProvider.Context context) {
        super(context, new ZombieModel<>(context.bakeLayer(ModModelLayers.ICED_ZOMBIE)), 0.45f);
    }

    @Override
    public ZombieRenderState createRenderState() {
        return new ZombieRenderState();
    }

    @Override
    public Identifier getTextureLocation(ZombieRenderState renderState) {
        return Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/iced_zombie/iced_zombie.png");
    }
}
