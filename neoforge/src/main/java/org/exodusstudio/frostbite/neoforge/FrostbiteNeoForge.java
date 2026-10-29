package org.exodusstudio.frostbite.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.neoforge.common.registries.DataAttachmentRegistry;

@Mod(Frostbite.MOD_ID)
public final class FrostbiteNeoForge {
    public FrostbiteNeoForge(IEventBus bus) {
        Frostbite.init();
        DataAttachmentRegistry.register(bus);
    }
}
