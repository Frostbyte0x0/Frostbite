package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.HealerElfModel;
import org.exodusstudio.frostbite.common.entity.client.states.ElfRenderState;
import org.exodusstudio.frostbite.common.entity.custom.elves.HealerElfEntity;

public class HealerElfRenderer extends MobRenderer<HealerElfEntity, ElfRenderState, HealerElfModel> {
    public HealerElfRenderer(EntityRendererProvider.Context context) {
        super(context, new HealerElfModel(context.bakeLayer(ModModelLayers.HEALER_ELF)), 0.45f);
    }

    @Override
    public ElfRenderState createRenderState() {
        return new ElfRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(ElfRenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/healer_elf/healer_elf.png");
    }
}
