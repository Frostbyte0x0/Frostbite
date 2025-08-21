package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.block.block_entities.LodestarBlockEntity;

import java.util.Set;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Frostbite.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LodestarBlockEntity>> LODESTAR =
            BLOCK_ENTITY_TYPES.register("lodestar", () -> new BlockEntityType
                            (LodestarBlockEntity::new, Set.of(BlockRegistry.LODESTAR.get())));


}
