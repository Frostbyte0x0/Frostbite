package org.exodusstudio.frostbite.common.item.custom.weapons;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.exodusstudio.frostbite.common.entity.custom.WindCircleEntity;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

public class GaleFanItem extends Item {
    public GaleFanItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        if (level instanceof ServerLevel serverLevel) {
            WindCircleEntity entity = new WindCircleEntity(EntityRegistry.WIND_CIRCLE.get(), serverLevel);
            entity.moveTo(player.position(), 0.0F, 0.0F);
            serverLevel.addFreshEntityWithPassengers(entity);
            serverLevel.gameEvent(GameEvent.ENTITY_PLACE, player.position(), GameEvent.Context.of(player));
        }
        return InteractionResult.SUCCESS;
    }
}