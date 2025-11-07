package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.CasterElfModel;
import org.exodusstudio.frostbite.common.entity.client.states.ElfRenderState;
import org.exodusstudio.frostbite.common.entity.custom.elves.CasterElfEntity;

public class CasterElfRenderer extends MobRenderer<CasterElfEntity, ElfRenderState, CasterElfModel> {
    public CasterElfRenderer(EntityRendererProvider.Context context) {
        super(context, new CasterElfModel(context.bakeLayer(ModModelLayers.CASTER_ELF)), 0.45f);
    }

    @Override
    public ElfRenderState createRenderState() {
        return new ElfRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(ElfRenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/caster_elf/caster_elf.png");
    }
}
