package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.resources.ResourceLocation;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.layers.WoollySheepWoolLayer;
import org.exodusstudio.frostbite.common.entity.client.models.WoollySheepModel;
import org.exodusstudio.frostbite.common.entity.custom.animals.WoollySheepEntity;

public class WoollySheepRenderer extends AgeableMobRenderer<WoollySheepEntity, SheepRenderState, WoollySheepModel> {
    private static final ResourceLocation SHEEP_LOCATION = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/woolly_sheep/woolly_sheep.png");

    public WoollySheepRenderer(EntityRendererProvider.Context p_174366_) {
        super(p_174366_, new WoollySheepModel(p_174366_.bakeLayer(ModModelLayers.WOOLLY_SHEEP)), new WoollySheepModel(p_174366_.bakeLayer(ModModelLayers.WOOLLY_SHEEP_BABY)), 0.7F);
        this.addLayer(new WoollySheepWoolLayer(this, p_174366_.getModelSet()));
    }

    public ResourceLocation getTextureLocation(SheepRenderState p_360570_) {
        return SHEEP_LOCATION;
    }

    public SheepRenderState createRenderState() {
        return new SheepRenderState();
    }

    public void extractRenderState(WoollySheepEntity p_362573_, SheepRenderState p_362333_, float p_360543_) {
        super.extractRenderState(p_362573_, p_362333_, p_360543_);
        p_362333_.headEatAngleScale = p_362573_.getHeadEatAngleScale(p_360543_);
        p_362333_.headEatPositionScale = p_362573_.getHeadEatPositionScale(p_360543_);
        p_362333_.isSheared = p_362573_.isSheared();
        p_362333_.woolColor = p_362573_.getColor();
        p_362333_.id = p_362573_.getId();
    }
}
