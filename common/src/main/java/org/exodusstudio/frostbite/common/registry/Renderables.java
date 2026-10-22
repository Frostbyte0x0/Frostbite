package org.exodusstudio.frostbite.common.registry;

import org.exodusstudio.frostbite.common.item.weapons.HuntersCatalyst;
import org.exodusstudio.frostbite.common.item.weapons.goat.Boltsplitter;
import org.exodusstudio.frostbite.common.util.Renderable;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class Renderables {
    public static HashMap<String, Renderable> RENDERABLES = new HashMap<>();


    public static Renderable BOLTSPLITTER_CHARGE_ATTACK =
            of(Boltsplitter::render, Boltsplitter::shouldStopRendering,
            "boltsplitter_charge_attack");
    public static Renderable HUNTERS_CATALYST_CHARGE_ATTACK =
            of(HuntersCatalyst::render, HuntersCatalyst::shouldStopRendering,
            "hunters_catalyst_charge_attack");

    public static Renderable of(
            Consumer<Renderable.RenderableContext> render,
            Function<Renderable.RenderableContext, Boolean> shouldStopRendering,
            String name
    ) {
        Renderable r = new Renderable() {
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
        RENDERABLES.put(name, r);

        return r;
    }
}
