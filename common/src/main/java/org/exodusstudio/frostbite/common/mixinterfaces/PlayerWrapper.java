package org.exodusstudio.frostbite.common.mixinterfaces;

import net.minecraft.server.level.ServerLevel;

public interface PlayerWrapper {
    void frostbite$startAccumulatingDamage(ServerLevel serverLevel);
    void frostbite$addDamage(float amount);
    int frostbite$getLiningLevel();
}
