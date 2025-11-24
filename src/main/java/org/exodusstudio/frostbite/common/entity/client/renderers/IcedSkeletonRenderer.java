package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AbstractSkeletonRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
import net.minecraft.resources.ResourceLocation;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.IcedSkeletonEntity;

public class IcedSkeletonRenderer extends AbstractSkeletonRenderer<IcedSkeletonEntity, SkeletonRenderState> {
    public IcedSkeletonRenderer(EntityRendererProvider.Context p_174380_) {
        super(p_174380_, ModelLayers.SKELETON, ModelLayers.SKELETON_INNER_ARMOR, ModelLayers.SKELETON_OUTER_ARMOR);
    }

    @Override
    public SkeletonRenderState createRenderState() {
        return new SkeletonRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(SkeletonRenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/iced_skeleton/iced_skeleton.png");
    }
}
