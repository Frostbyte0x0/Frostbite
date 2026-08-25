package org.exodusstudio.frostbite.common.block.states;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

public class RuneRenderState extends BlockEntityRenderState {
    public float health;
    public float maxHealth;
    public int alliesDetected;
    public ChatFormatting healthColour;
}
