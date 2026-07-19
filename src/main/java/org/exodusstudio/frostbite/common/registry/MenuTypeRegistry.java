package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.client.gui.WeavingMenu;
import org.exodusstudio.frostbite.client.gui.scribing.ApplyingMenu;
import org.exodusstudio.frostbite.client.gui.scribing.CombiningMenu;
import org.exodusstudio.frostbite.client.gui.scribing.DecipheringMenu;

import java.util.function.Supplier;

public class MenuTypeRegistry {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(BuiltInRegistries.MENU, Frostbite.MOD_ID);

    public static final Supplier<MenuType<WeavingMenu>> WEAVING_MENU =
            MENU_TYPES.register("weaving", () -> new MenuType<>(WeavingMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static final Supplier<MenuType<DecipheringMenu>> DECIPHERING_MENU =
            MENU_TYPES.register("deciphering", () -> new MenuType<>(DecipheringMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static final Supplier<MenuType<CombiningMenu>> COMBINING_MENU =
            MENU_TYPES.register("combining", () -> new MenuType<>(CombiningMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static final Supplier<MenuType<ApplyingMenu>> APPLYING_MENU =
            MENU_TYPES.register("applying", () -> new MenuType<>(ApplyingMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
