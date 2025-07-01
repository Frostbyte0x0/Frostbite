package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.client.gui.WeavingMenu;

import java.util.function.Supplier;

public class MenuTypeRegistry {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(BuiltInRegistries.MENU, Frostbite.MOD_ID);

    public static final Supplier<MenuType<WeavingMenu>> WEAVING_MENU =
            MENU_TYPES.register("weaving", () -> new MenuType<>(WeavingMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
