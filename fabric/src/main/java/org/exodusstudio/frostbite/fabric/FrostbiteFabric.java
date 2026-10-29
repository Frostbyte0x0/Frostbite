package org.exodusstudio.frostbite.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.AttributeRegistry;
import org.exodusstudio.frostbite.fabric.common.registries.DataAttachmentRegistry;

public final class FrostbiteFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Frostbite.init();
        DataAttachmentRegistry.register();

        AttributeSupplier.Builder playerAttributes = Player.createAttributes();
        AttributeRegistry.ATTRIBUTES.getEntries().forEach(attribute -> playerAttributes.add(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute.value())));
        FabricDefaultAttributeRegistry.register(EntityTypes.PLAYER, playerAttributes);
    }
}
