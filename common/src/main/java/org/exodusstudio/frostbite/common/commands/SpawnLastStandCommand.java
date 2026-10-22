package org.exodusstudio.frostbite.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.gameevent.GameEvent;
import org.exodusstudio.frostbite.common.entity.custom.misc.LastStandEntity;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

public class SpawnLastStandCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        dispatcher.register((Commands.literal("spawnLastStand").requires((p_138596_) -> p_138596_.permissions().hasPermission(Permissions.COMMANDS_ADMIN))).executes((context) -> {
            ServerLevel serverLevel = context.getSource().getLevel();
            LastStandEntity lastStandEntity = new LastStandEntity(EntityRegistry.LAST_STAND.get(), serverLevel);
            lastStandEntity.move(MoverType.SELF, context.getSource().getEntity().position());
            lastStandEntity.setReleaseTicks(200);
            lastStandEntity.setReleasing(true);
            serverLevel.addFreshEntityWithPassengers(lastStandEntity);
            serverLevel.gameEvent(GameEvent.ENTITY_PLACE, context.getSource().getEntity().position(), GameEvent.Context.of(context.getSource().getEntity()));
            return 0;
        }));
    }
}
