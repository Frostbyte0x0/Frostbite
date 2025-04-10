package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.component.JarContentsData;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;

public class SporeCloudEntity extends AreaEffectCloud {
    private PotionContents potionContents;

    public SporeCloudEntity(EntityType<?> entityType, Level level) {
        super(EntityRegistry.SPORE_CLOUD.get(), level);
        this.potionContents = PotionContents.EMPTY;
        this.noPhysics = true;
        this.setDuration(600);
        this.setDurationOnUse(600);
        this.setRadius(10f);
        this.setRadiusOnUse(10f);
    }

    public SporeCloudEntity(Level level, double x, double y, double z) {
        this(EntityRegistry.SPORE_CLOUD.get(), level);
        this.setPos(x, y, z);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.is(ItemRegistry.EMPTY_JAR)) {
            player.playSound(SoundEvents.BOTTLE_FILL, 1.0F, 1.0F);
            ItemStack itemstack1 = ItemUtils.createFilledResult(itemstack, player, ItemRegistry.JAR.toStack());
            itemstack1.set(DataComponentTypeRegistry.JAR_CONTENTS.get(), new JarContentsData(this.potionContents));
            player.setItemInHand(hand, itemstack1);
            return InteractionResult.SUCCESS;
        } else {
            return super.interact(player, hand);
        }
    }

    @Override
    public void tick() {
        if (this.getRadius() == 0) {
            this.setRadius(2.5f);
        }

        if (this.getDuration() == 0) {
            this.discard();
        }

        this.setDuration(this.getDuration() - 1);
        Frostbite.LOGGER.debug(String.valueOf(this.getDuration()));
    }

    @Override
    public void setPotionContents(PotionContents potionContents) {
        this.potionContents = potionContents;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, 0.5F);
    }
}
