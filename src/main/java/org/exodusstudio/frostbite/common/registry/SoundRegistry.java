package org.exodusstudio.frostbite.common.registry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.util.helpers.DataHelper;
import org.exodusstudio.frostbite.common.weather.WeatherInfo;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.exodusstudio.frostbite.common.util.Util.isFrostbite;

public class SoundRegistry {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Frostbite.MOD_ID);

    public static final Supplier<SoundEvent> STUNNING_BELL_RING = registerSoundEvent("stunning_bell_ring");
    public static final Supplier<SoundEvent> PARRY = registerSoundEvent("parry");

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

    public static final Supplier<SoundEvent> DIMENSION_AMBIENCE =  registerSoundEvent("dimension_ambience");
    public static final Supplier<SoundEvent> SNOW_WIND_AMBIENCE = registerSoundEvent("snow_wind_ambience");
    public static final Supplier<SoundEvent> CATALYST_CHARGE = registerSoundEvent("catalyst_charge");
    public static final Supplier<SoundEvent> CATALYST_BURST = registerSoundEvent("catalyst_burst");

    public static final Supplier<SoundEvent> SNOW_AMBIENCE = registerSoundEvent("snow_ambience");
    public static final Supplier<SoundEvent> BLIZZARD_AMBIENCE = registerSoundEvent("blizzard_ambience");
    public static final Supplier<SoundEvent> WHITEOUT_AMBIENCE = registerSoundEvent("whiteout_ambience");

    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static class Ambience {
        public static Level level =  Minecraft.getInstance().level;
        public static WeatherInfo weatherInfo = DataHelper.getWeatherInfo(level);

        public static final SoundRegistry.Ambience[] AMBIENCES = {
                new SoundRegistry.Ambience(46 * 20, 4 * 20, 1.5f, SoundRegistry.SNOW_WIND_AMBIENCE,
                        (level) -> level instanceof ClientLevel && isFrostbite(level))
        };
        public static final SoundRegistry.Ambience[] SNOW_AMBIENCE = new Ambience[]{
                new Ambience(42 * 20, 0, 1.0f, SoundRegistry.SNOW_AMBIENCE,
                        (level) -> level instanceof ClientLevel && isFrostbite(level))
        };
        public static final SoundRegistry.Ambience[] BLIZZARD_AMBIENCE = new Ambience[]{
                new Ambience(92 * 20, 0, 1.0f, SoundRegistry.BLIZZARD_AMBIENCE,
                        (level) -> level instanceof ClientLevel && isFrostbite(level))
        };
        public static final SoundRegistry.Ambience[] WHITEOUT_AMBIENCE = new Ambience[]{
                new Ambience(48 * 20, 0, 1.0f, SoundRegistry.WHITEOUT_AMBIENCE,
                        (level) -> level instanceof ClientLevel && isFrostbite(level))
        };


        public final float length;
        public final float fadeLength;
        public final float volume;
        public final Supplier<SoundEvent> soundEvent;
        public final Function<Level, Boolean> shouldPlay;
        public double startTime;

        public Ambience(float length, float fadeLength, float volume, Supplier<SoundEvent> soundEvent, Function<Level, Boolean> shouldPlay) {
            this.length = length;
            this.fadeLength = fadeLength;
            this.volume = volume;
            this.soundEvent = soundEvent;
            this.shouldPlay = shouldPlay;
        }
    }
}
