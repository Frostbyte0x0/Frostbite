package org.exodusstudio.frostbite.common.item.weapons;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.AttachementRegistry;
import org.exodusstudio.frostbite.common.rendering.RenderToolkit;

public class HuntersCatalyst extends Item {
    public HuntersCatalyst(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResult.PASS;
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity user) {
        return 72000;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
        return ItemUseAnimation.BOW;
    }


//    public static void renderBeamFromPlayer(Player player, PoseStack poseStack, RenderLevelStageEvent renderLevelStageEvent) {
//        Vec3 eye = player.getEyePosition();
//        Vec3 look = player.getLookAngle();
//
//        Vec3 start = eye.add(look.scale(0.5));
//
//        poseStack.pushPose();
//
//        poseStack.translate(start.x, start.y, start.z);
//
//        float yaw = (float)(Math.atan2(look.z, look.x) * (180F / Math.PI)) - 90f;
//        float pitch = (float)(-(Math.atan2(look.y, Math.sqrt(look.x * look.x + look.z * look.z)) * (180F / Math.PI)));
//
//        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
//        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
//
//        float time = player.level().getGameTime() + renderLevelStageEvent.getLevelRenderState().gameTime;
//
//        int length = 30;
//
//        BeaconRenderer.submitBeaconBeam(
//                poseStack,
//        submitNodeCollector,
//        BeaconRenderer.BEAM_LOCATION,
//        1.0f,
//        time,
//        0,
//        length,
//        0xff00ffff,
//        0.2f,
//        0.25f
//        );
//    }
}
