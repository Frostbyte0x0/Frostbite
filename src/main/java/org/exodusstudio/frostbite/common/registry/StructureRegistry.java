package org.exodusstudio.frostbite.common.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.structures.FTOPortal;
import org.exodusstudio.frostbite.common.structures.OTFPortal;

public class StructureRegistry {
    public static final DeferredRegister<StructureType<?>> STRUCTURES =
            DeferredRegister.create(Registries.STRUCTURE_TYPE, Frostbite.MOD_ID);

    public static final DeferredHolder<StructureType<?>, StructureType<OTFPortal>> OTF_PORTAL =
            STRUCTURES.register("otf_portal", () -> explicitStructureTypeTyping(OTFPortal.CODEC));
    public static final DeferredHolder<StructureType<?>, StructureType<FTOPortal>> FTO_PORTAL =
            STRUCTURES.register("fto_portal", () -> explicitStructureTypeTyping(FTOPortal.CODEC));


    private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(MapCodec<T> structureCodec) {
        return () -> structureCodec;
    }
}
