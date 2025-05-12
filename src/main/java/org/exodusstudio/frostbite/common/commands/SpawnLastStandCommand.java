package org.exodusstudio.frostbite.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gameevent.GameEvent;
import org.exodusstudio.frostbite.common.entity.custom.LastStandEntity;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

public class SpawnLastStandCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("spawnLastStand").requires((p_138596_) -> p_138596_.hasPermission(2))).executes((p_288608_) -> {
            ServerLevel serverLevel = ((CommandSourceStack)p_288608_.getSource()).getLevel();
            LastStandEntity lastStandEntity = new LastStandEntity(EntityRegistry.LAST_STAND.get(), serverLevel);
            lastStandEntity.moveTo(((CommandSourceStack)p_288608_.getSource()).getEntity().position());
            lastStandEntity.setReleaseTicks(200);
            lastStandEntity.setReleasing(true);
            serverLevel.addFreshEntityWithPassengers(lastStandEntity);
            serverLevel.gameEvent(GameEvent.ENTITY_PLACE, ((CommandSourceStack)p_288608_.getSource()).getEntity().position(), GameEvent.Context.of(((CommandSourceStack)p_288608_.getSource()).getEntity()));
            return 0;
        }));
    }
}
