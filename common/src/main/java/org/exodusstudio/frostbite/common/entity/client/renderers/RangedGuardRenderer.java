package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CrossbowItem;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.RangedGuardModel;
import org.exodusstudio.frostbite.common.entity.client.states.RangedGuardRenderState;
import org.exodusstudio.frostbite.common.entity.custom.guards.RangedGuardEntity;

public class RangedGuardRenderer extends StateMonsterRenderer<RangedGuardEntity, RangedGuardRenderState, RangedGuardModel> {
    public RangedGuardRenderer(EntityRendererProvider.Context c) {
        super(c, new RangedGuardModel(c.bakeLayer(ModModelLayers.RANGED_GUARD)), 0.45f,
                Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/ranged_guard/ranged_guard.png"));
        this.addLayer(new ItemInHandLayer<>(this));
    }

    @Override
    public RangedGuardRenderState createRenderState() {
        return new RangedGuardRenderState();
    }

    @Override
    public void extractRenderState(RangedGuardEntity rangedGuardEntity, RangedGuardRenderState state, float partialTicks) {
        super.extractRenderState(rangedGuardEntity, state, partialTicks);
        state.armPose = rangedGuardEntity.getArmPose();
        state.maxCrossbowChargeDuration = CrossbowItem.getChargeDuration(rangedGuardEntity.getUseItem(), rangedGuardEntity);
        state.ticksUsingItem = rangedGuardEntity.getTicksUsingItem(partialTicks);
        ArmedEntityRenderState.extractArmedEntityRenderState(rangedGuardEntity, state, itemModelResolver, partialTicks);
    }
}
