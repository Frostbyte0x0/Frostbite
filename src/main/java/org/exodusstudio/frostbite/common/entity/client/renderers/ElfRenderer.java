package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.models.ElfModel;
import org.exodusstudio.frostbite.common.entity.client.states.ElfRenderState;
import org.exodusstudio.frostbite.common.entity.custom.elves.ElfEntity;

public class ElfRenderer extends MobRenderer<ElfEntity, ElfRenderState, ElfModel> {
    final Identifier texture;

    public ElfRenderer(EntityRendererProvider.Context context, ElfModel model, float v, String texture) {
        super(context, model, v);
        this.texture = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/" + texture + "/" + texture + ".png");
    }

    @Override
    public ElfRenderState createRenderState() {
        return new ElfRenderState();
    }

    @Override
    public void extractRenderState(ElfEntity elf, ElfRenderState state, float p_361157_) {
        super.extractRenderState(elf, state, p_361157_);
        state.currentAnimationState.copyFrom(elf.currentAnimationState);
        state.lastAnimationState.copyFrom(elf.lastAnimationState);
        state.ticksSinceLastChange = elf.getTicksSinceLastChange();
        state.currentState = elf.getCurrentState();
        state.lastState = elf.getLastState();
        state.jumpingAnimationState.copyFrom(elf.jumpingAnimationState);
        state.isJumping = elf.isJumping();
        state.walkAnimationSpeed = 0;
    }

    @Override
    public Identifier getTextureLocation(ElfRenderState elfRenderState) {
        return texture;
    }
}
