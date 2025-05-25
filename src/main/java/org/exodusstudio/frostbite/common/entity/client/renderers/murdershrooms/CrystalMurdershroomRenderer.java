package org.exodusstudio.frostbite.common.entity.client.renderers.murdershrooms;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.murdershrooms.CrystalMurdershroomModel;
import org.exodusstudio.frostbite.common.entity.custom.murdershrooms.CrystalMurdershroomEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CrystalMurdershroomRenderer extends MobRenderer<CrystalMurdershroomEntity, LivingEntityRenderState, CrystalMurdershroomModel> {
    public CrystalMurdershroomRenderer(EntityRendererProvider.Context context) {
        super(context, new CrystalMurdershroomModel(context.bakeLayer(ModModelLayers.CRYSTAL_MURDERSHROOM)), 0.45f);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntityRenderState crystalMurdershroomRenderState) {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/crystal_murdershroom/crystal_murdershroom.png");
    }
}