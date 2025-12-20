package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.SummonerElfModel;
import org.exodusstudio.frostbite.common.entity.client.states.ElfRenderState;
import org.exodusstudio.frostbite.common.entity.custom.elves.SummonerElfEntity;

public class SummonerElfRenderer extends MobRenderer<SummonerElfEntity, ElfRenderState, SummonerElfModel> {
    public SummonerElfRenderer(EntityRendererProvider.Context context) {
        super(context, new SummonerElfModel(context.bakeLayer(ModModelLayers.SUMMONER_ELF)), 0.45f);
    }

    @Override
    public ElfRenderState createRenderState() {
        return new ElfRenderState();
    }

    @Override
    public void extractRenderState(SummonerElfEntity elf, ElfRenderState state, float partialTick) {
        super.extractRenderState(elf, state, partialTick);
        state.isAttacking = elf.isAttacking();
        state.attackTicks = elf.getAttackTicks(partialTick);
    }

    @Override
    public Identifier getTextureLocation(ElfRenderState renderState) {
        return Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/summoner_elf/summoner_elf.png");
    }
}
