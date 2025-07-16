package org.exodusstudio.frostbite.common.item.custom.weapons.gun;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.exodusstudio.frostbite.common.registry.SoundRegistry;

public class SniperItem extends AbstractGun {
    public SniperItem(Properties properties) {
        super(properties, 6f, 0.5f, 60, 20, 8, ItemRegistry.SNIPER_BULLET.toStack());
    }

    @Override
    public SoundEvent getFailSound(Player player, Level level) {
        return SoundRegistry.SNIPER_FAIL.get();
    }

    @Override
    public SoundEvent getShootSound(Player player, Level level) {
        return SoundRegistry.SNIPER_SHOOT.get();
    }

    @Override
    public SoundEvent getReloadSound(Player player, Level level) {
        return SoundRegistry.SNIPER_RELOAD.get();
    }

    @Override
    public SoundEvent getChamberSound(Player player, Level level) {
        return SoundRegistry.SNIPER_CHAMBER.get();
    }

    @Override
    public SoundEvent getLastShotSound(Player player, Level level) {
        return SoundRegistry.SNIPER_PING.get();
    }
}
