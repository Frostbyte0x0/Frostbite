//package com.frostbyte.frostbite.item.custom.alchemy;
//
//import com.frostbyte.frostbite.Frostbite;
//import com.frostbyte.frostbite.effect.ModEffects;
//import cpw.mods.jarhandling.impl.Jar;
//import net.minecraft.core.Holder;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.effect.MobEffectInstance;
//import net.neoforged.bus.api.IEventBus;
//import net.neoforged.neoforge.registries.DeferredRegister;
//
//public class Jars {
//    public static final DeferredRegister<Jar> JARS =
//            DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "jars"), Frostbite.MOD_ID);
//
//    public static final Holder<Jar> WATER = register("water",
//            new Jar("water"));
//
//    public static final Holder<Jar> FLORAL = register("floral",
//            new Jar("floral", new MobEffectInstance(ModEffects.IRRITATION, 3600))); // infinite duration
//
//    public static final Holder<Jar> PINE = register("pine",
//            new Jar("pine", new MobEffectInstance(ModEffects.PARANOIA, 3600)));
//
//    public static final Holder<Jar> DECAYING = register("decaying",
//            new Jar("decaying", new MobEffectInstance(ModEffects.DECAY, 3600)));
//
//    public static final Holder<Jar> LIGHT = register("light",
//            new Jar("light", new MobEffectInstance(ModEffects.PARALYSIS, 3600)));
//
//    public static final Holder<Jar> CRYSTAL = register("crystal",
//            new Jar("crystal", new MobEffectInstance(ModEffects.PETRIFICATION, 3600)));
//
//    public static final Holder<Jar> MOSSY = register("mossy",
//            new Jar("mossy", new MobEffectInstance(ModEffects.MOLD, 3600)));
//
//
//    private static Holder<Jar> register(String name, Jar jar) {
//        return JARS.register(name, () -> jar); // ?
//    }
//
//    public static void register(IEventBus eventBus) {
//        JARS.register(eventBus);
//    }
//}
