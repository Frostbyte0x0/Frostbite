package org.exodusstudio.frostbite.common.registry;

import org.exodusstudio.frostbite.Frostbite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Frostbite.MOD_ID);

    public static final Supplier<SoundEvent> STUNNING_BELL_RING = registerSoundEvent("stunning_bell_ring");

    public static final Supplier<SoundEvent> CHAINCICLE_SWIPE = registerSoundEvent("chaincicle_swipe");
    //public static final Supplier<SoundEvent> CHAINCICLE_LAUNCHED_LOOP = registerSoundEvent("chaincicle_launched_loop");
    public static final Supplier<SoundEvent> CHAINCICLE_HOOK_LAUNCH = registerSoundEvent("chaincicle_hook_launch");
    //public static final Supplier<SoundEvent> CHAINCICLE_HOOK_HIT = registerSoundEvent("chaincicle_hook_hit");
    public static final Supplier<SoundEvent> CHAINCICLE_GRAPPLE_LAUNCH = registerSoundEvent("chaincicle_grapple_launch");
    //public static final Supplier<SoundEvent> CHAINCICLE_GRAPPLE_HIT = registerSoundEvent("chaincicle_grapple_hit");

    public static final Supplier<SoundEvent> SNIPER_RELOAD = registerSoundEvent("sniper_reload");
    public static final Supplier<SoundEvent> SNIPER_CHAMBER = registerSoundEvent("sniper_chamber");
    public static final Supplier<SoundEvent> SNIPER_SHOOT = registerSoundEvent("sniper_shoot");
    public static final Supplier<SoundEvent> SNIPER_PING = registerSoundEvent("sniper_ping");
    public static final Supplier<SoundEvent> SNIPER_FAIL = registerSoundEvent("sniper_fail");

    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }
}
