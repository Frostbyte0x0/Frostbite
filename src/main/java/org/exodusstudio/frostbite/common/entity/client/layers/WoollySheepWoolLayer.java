package org.exodusstudio.frostbite.common.entity.client.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
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

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, SheepRenderState renderState, float v, float v1) {
        if (!renderState.isSheared) {
            EntityModel<SheepRenderState> entitymodel = renderState.isBaby ? this.babyModel : this.adultModel;
            if (renderState.isInvisible) {
                if (renderState.appearsGlowing()) {
                    entitymodel.setupAnim(renderState);
                    VertexConsumer vertexconsumer = p_361412_.getBuffer(RenderTypes.outline(SHEEP_FUR_LOCATION));
                    entitymodel.renderToBuffer(poseStack, vertexconsumer, p_361724_, LivingEntityRenderer.getOverlayCoords(renderState, 0.0F), -16777216);
                }
            } else {
                int a;
                if (renderState.isJebSheep) {
                    int j = 25;
                    int k = Mth.floor(renderState.ageInTicks);
                    int l = k / 25 + renderState.id;
                    int i1 = DyeColor.values().length;
                    int j1 = l % i1;
                    int k1 = (l + 1) % i1;
                    float f = ((float)(k % 25) + Mth.frac(renderState.ageInTicks)) / 25.0F;
                    a = ARGB.linearLerp(f, DyeColor.byId(j1).getFireworkColor(), DyeColor.byId(k1).getFireworkColor());
                } else {
                    a = renderState.woolColor.getFireworkColor();
                }

                coloredCutoutModelCopyLayerRender(entitymodel, SHEEP_FUR_LOCATION, poseStack, submitNodeCollector, i, renderState, a, -1);
            }
        }
    }
}
