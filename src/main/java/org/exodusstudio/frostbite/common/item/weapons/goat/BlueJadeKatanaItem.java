package org.exodusstudio.frostbite.common.item.weapons.goat;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.item.weapons.ChargeAttackWeapon;
import org.exodusstudio.frostbite.common.registry.AttachementRegistry;
import org.exodusstudio.frostbite.common.rendering.RenderToolkit;

public class BlueJadeKatanaItem extends ChargeAttackWeapon {
    public BlueJadeKatanaItem(Properties pProperties) {
        super(pProperties, "blue_jade_katana_charge_attack");
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        startChargeAttack(player);
        return super.use(level, player, usedHand);
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 6.0,
                        AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }

    public static void renderChargeAttack(LivingEntity user, PoseStack poseStack, LevelRenderState levelRenderState, SubmitNodeCollector output) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        float partialTicks = mc.getDeltaTracker().getGameTimeDeltaPartialTick(!mc.level.tickRateManager().isEntityFrozen(user));
        float t = (levelRenderState.gameTime + partialTicks - user.getData(AttachementRegistry.CHARGE_ATTACK_START)) / 20f;
        poseStack.pushPose();
        poseStack.translate(
                -mc.gameRenderer.mainCamera().position().x,
                -mc.gameRenderer.mainCamera().position().y,
                -mc.gameRenderer.mainCamera().position().z);
        poseStack.translate(
                user.getPosition(partialTicks).x,
                user.getPosition(partialTicks).y + 2.0f,
                user.getPosition(partialTicks).z);
        output.submitCustomGeometry(
                poseStack,
                RenderTypes.entityTranslucent(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/environement/celestial/space_blue.png")),
                (pose, buffer) -> RenderToolkit.renderSphere(pose, buffer, 3 * t - 0.001f, 25));
//        output.submitCustomGeometry(poseStack, RenderTypes.endPortal(),
//                (pose, buffer) -> RenderToolkit.renderSphere(pose, buffer, 3 * t, 25));
        poseStack.popPose();
        if (t > 4) {
            user.setData(AttachementRegistry.CURRENT_CHARGE_ATTACK, "");
        }
    }
}