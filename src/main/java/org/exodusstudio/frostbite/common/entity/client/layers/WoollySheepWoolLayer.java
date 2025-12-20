package org.exodusstudio.frostbite.common.entity.client.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.models.WoollySheepFurModel;
import org.exodusstudio.frostbite.common.entity.client.models.WoollySheepModel;

public class WoollySheepWoolLayer extends RenderLayer<SheepRenderState, WoollySheepModel> {
    private static final Identifier SHEEP_FUR_LOCATION = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/woolly_sheep/woolly_sheep_fur.png");
    private final EntityModel<SheepRenderState> adultModel;
    private final EntityModel<SheepRenderState> babyModel;

    public WoollySheepWoolLayer(RenderLayerParent<SheepRenderState, WoollySheepModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.adultModel = new WoollySheepFurModel(modelSet.bakeLayer(ModModelLayers.WOOLLY_SHEEP_FUR));
        this.babyModel = new WoollySheepFurModel(modelSet.bakeLayer(ModModelLayers.WOOLLY_SHEEP_BABY_FUR));
    }

    public void submit(PoseStack p_434319_, SubmitNodeCollector p_434903_, int p_434733_, SheepRenderState p_432970_, float p_432913_, float p_435103_) {
        if (!p_432970_.isSheared) {
            EntityModel<SheepRenderState> entitymodel = p_432970_.isBaby ? this.babyModel : this.adultModel;
            if (p_432970_.isInvisible) {
                if (p_432970_.appearsGlowing()) {
                    p_434903_.submitModel(
                            entitymodel,
                            p_432970_,
                            p_434319_,
                            RenderTypes.outline(SHEEP_FUR_LOCATION),
                            p_434733_,
                            LivingEntityRenderer.getOverlayCoords(p_432970_, 0.0F),
                            -16777216,
                            null,
                            p_432970_.outlineColor,
                            null
                    );
                }
            } else {
                coloredCutoutModelCopyLayerRender(entitymodel, SHEEP_FUR_LOCATION, p_434319_, p_434903_, p_434733_, p_432970_, p_432970_.getWoolColor(), 0);
            }
        }
    }
}
