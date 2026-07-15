package org.exodusstudio.frostbite.common.registry;

import org.exodusstudio.frostbite.common.item.weapons.HuntersCatalyst;
import org.exodusstudio.frostbite.common.item.weapons.goat.BlueJadeKatanaItem;
import org.exodusstudio.frostbite.common.util.Renderable;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class Renderables {
    public static Renderable BLUE_JADE_KATANA_CHARGE_ATTACK =
            of(BlueJadeKatanaItem::render, BlueJadeKatanaItem::shouldStopRendering,
            "blue_jade_katana_charge_attack");
    public static Renderable HUNTERS_CATALYST_CHARGE_ATTACK =
            of(HuntersCatalyst::render, HuntersCatalyst::shouldStopRendering,
            "hunters_catalyst_charge_attack");

    public static HashMap<String, Renderable> RENDERABLES = new HashMap<>() {
        {
            put("blue_jade_katana_charge_attack", BLUE_JADE_KATANA_CHARGE_ATTACK);
            put("hunters_catalyst_charge_attack", HUNTERS_CATALYST_CHARGE_ATTACK);
        }
    };

    public static Renderable of(
            Consumer<Renderable.RenderableContext> render,
            Function<Renderable.RenderableContext, Boolean> shouldStopRendering,
            String name
    ) {
        return new Renderable() {
            @Override
            public void render(RenderableContext context) {
                render.accept(context);
            }

            @Override
            public boolean shouldStopRendering(RenderableContext context) {
                return shouldStopRendering.apply(context);
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }
}
