package org.exodusstudio.frostbite.common.item.custom.gun;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.component.GunData;
import org.exodusstudio.frostbite.common.entity.custom.bullets.SniperBulletEntity;
import org.exodusstudio.frostbite.common.item.custom.gun.bullet.RevolverBulletItem;
import org.exodusstudio.frostbite.common.item.custom.gun.bullet.SniperBulletItem;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.registry.SoundRegistry;

public abstract class AbstractGun extends Item {
    private final float bulletVelocity;
    private final float bulletInaccuracy;
    private final int reloadTime;
    private final int chamberTime;
    private final int magSize;
    private final ItemStack bulletType;


    public AbstractGun(Properties properties, float bulletVelocity, float bulletInaccuracy, int reloadTime, int chamberTime, int magSize, ItemStack bulletType) {
        super(properties);
        this.bulletVelocity = bulletVelocity;
        this.bulletInaccuracy = bulletInaccuracy;
        this.reloadTime = reloadTime;
        this.chamberTime = chamberTime;
        this.magSize = magSize;
        this.bulletType = bulletType;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        Frostbite.LOGGER.debug("NEW SHOOT");
        Frostbite.LOGGER.debug(shootCondition(itemstack));
        Frostbite.LOGGER.debug(String.valueOf(isBulletInChamber(itemstack)));
        Frostbite.LOGGER.debug(String.valueOf(isChambering(itemstack)));
        Frostbite.LOGGER.debug(String.valueOf(isReloading(itemstack)));
        Frostbite.LOGGER.debug(String.valueOf(getBulletsInMag(itemstack)));
        Frostbite.LOGGER.debug(String.valueOf(getChamberCooldown(itemstack)));
        Frostbite.LOGGER.debug(String.valueOf(getReloadCooldown(itemstack)));

        SoundEvent sound = SoundEvents.ANVIL_USE;

        switch (shootCondition(itemstack)) {
            case "noShoot":
                sound = getFailSound(player, level);
                return InteractionResult.FAIL;
            case "canShoot":
                sound = getShootSound(player, level);
                if (level instanceof ServerLevel serverlevel) {
                    shoot(serverlevel, player, hand, player.getItemInHand(hand), this.bulletType, this.bulletVelocity, this.bulletInaccuracy);
                }
                break;
            case "lastShot":
                sound = getLastShotSound(player, level);
                if (level instanceof ServerLevel serverlevel) {
                    shoot(serverlevel, player, hand, player.getItemInHand(hand), this.bulletType, this.bulletVelocity, this.bulletInaccuracy);
                }
                break;
            case "shouldReload":
                reload(itemstack);
                sound = getReloadSound(player, level);
                break;
            case "shouldChamber":
                chamberRound(itemstack);
                sound = getChamberSound(player, level);
                break;
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(), sound,
                SoundSource.PLAYERS, 2F, 0.87F);
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResult.SUCCESS;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (isSelected && isReloading(stack)) {
            reduceReloadCooldown(1, stack);
        }
        if (isReloading(stack) && getReloadCooldown(stack) <= 0) {
            setReloading(false, stack);
            setBulletsInMag(this.magSize, stack);
        }

        if (isSelected && isChambering(stack)) {
            reduceChamberCooldown(1, stack);
        }
        if (isChambering(stack) && getChamberCooldown(stack) <= 0) {
            setChambering(false, stack);
            setBulletInChamber(true, stack);
        }
    }


    protected void shoot(
            ServerLevel level,
            LivingEntity shooter,
            InteractionHand hand,
            ItemStack weapon,
            ItemStack bullet,
            float velocity,
            float inaccuracy
    ) {
        if (!bullet.isEmpty()) {
            this.setBulletInChamber(false, weapon);
            Projectile.spawnProjectile(
                    this.createProjectile(level, shooter, weapon, bullet),
                    level,
                    bullet,
                    projectile -> this.shootProjectile(shooter, projectile, velocity, inaccuracy)
            );
            weapon.hurtAndBreak(1, shooter, LivingEntity.getSlotForHand(hand));
        }
    }

    protected void shootProjectile(LivingEntity livingEntity, Projectile projectile, float velocity, float inaccuracy) {
        projectile.shootFromRotation(livingEntity, livingEntity.getXRot(), livingEntity.getYRot(), 0.0F, velocity, inaccuracy);
    }

    protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo) {
        if (ammo.getItem() instanceof SniperBulletItem sniperBulletItem) {
            SniperBulletEntity sniperBullet = sniperBulletItem.createBullet(level);
            sniperBullet.moveTo(shooter.getX(), shooter.getY() + shooter.getEyeHeight(), shooter.getZ(), shooter.getYRot(), 0.0F);
            return sniperBullet;
        }
        else if (ammo.getItem() instanceof RevolverBulletItem revolverBulletItem) {
            SniperBulletEntity sniperBullet = revolverBulletItem.createBullet(level);
            sniperBullet.moveTo(shooter.getX(), shooter.getY(), shooter.getZ(), shooter.getYRot(), 0.0F);
            return sniperBullet;
        }

        return null;
    }


    public void reload(ItemStack stack) {
        setReloading(true, stack);
        setReloadCooldown(this.reloadTime, stack);
        setBulletsInMag(this.magSize, stack);
    }

    public void chamberRound(ItemStack stack) {
        setChambering(true, stack);
        setChamberCooldown(this.chamberTime, stack);
        reduceBulletsInMag(1, stack);
    }


    public boolean isBulletInChamber(ItemStack stack) {
        return stack.get(DataComponentTypeRegistry.GUN.get()).bulletInChamber();
    }

    public void setBulletInChamber(boolean bulletInChamber, ItemStack stack) {
        Frostbite.LOGGER.debug("set bullet in chamber to: " + bulletInChamber);
        stack.set(DataComponentTypeRegistry.GUN.get(), new GunData(
                bulletInChamber,
                stack.get(DataComponentTypeRegistry.GUN.get()).bulletsInMag(),
                stack.get(DataComponentTypeRegistry.GUN.get()).isReloading(),
                stack.get(DataComponentTypeRegistry.GUN.get()).reloadCooldown(),
                stack.get(DataComponentTypeRegistry.GUN.get()).isChambering(),
                stack.get(DataComponentTypeRegistry.GUN.get()).chamberCooldown()));
    }


    public int getBulletsInMag(ItemStack stack) {
        return stack.get(DataComponentTypeRegistry.GUN.get()).bulletsInMag();
    }

    public void setBulletsInMag(int bulletsInMag, ItemStack stack) {
        stack.set(DataComponentTypeRegistry.GUN.get(), new GunData(
                stack.get(DataComponentTypeRegistry.GUN.get()).bulletInChamber(),
                bulletsInMag,
                stack.get(DataComponentTypeRegistry.GUN.get()).isReloading(),
                stack.get(DataComponentTypeRegistry.GUN.get()).reloadCooldown(),
                stack.get(DataComponentTypeRegistry.GUN.get()).isChambering(),
                stack.get(DataComponentTypeRegistry.GUN.get()).chamberCooldown()));
    }

    public void reduceBulletsInMag(int amount, ItemStack stack) {
        stack.set(DataComponentTypeRegistry.GUN.get(), new GunData(
                stack.get(DataComponentTypeRegistry.GUN.get()).bulletInChamber(),
                stack.get(DataComponentTypeRegistry.GUN.get()).bulletsInMag() - amount,
                stack.get(DataComponentTypeRegistry.GUN.get()).isReloading(),
                stack.get(DataComponentTypeRegistry.GUN.get()).reloadCooldown(),
                stack.get(DataComponentTypeRegistry.GUN.get()).isChambering(),
                stack.get(DataComponentTypeRegistry.GUN.get()).chamberCooldown()));
    }


    public boolean isReloading(ItemStack stack) {
        return stack.get(DataComponentTypeRegistry.GUN.get()).isReloading();
    }

    public void setReloading(boolean reloading, ItemStack stack) {
        stack.set(DataComponentTypeRegistry.GUN.get(), new GunData(
                stack.get(DataComponentTypeRegistry.GUN.get()).bulletInChamber(),
                stack.get(DataComponentTypeRegistry.GUN.get()).bulletsInMag(),
                reloading,
                stack.get(DataComponentTypeRegistry.GUN.get()).reloadCooldown(),
                stack.get(DataComponentTypeRegistry.GUN.get()).isChambering(),
                stack.get(DataComponentTypeRegistry.GUN.get()).chamberCooldown()));
    }


    public int getReloadCooldown(ItemStack stack) {
        return stack.get(DataComponentTypeRegistry.GUN.get()).reloadCooldown();
    }

    public void setReloadCooldown(int reloadCooldown, ItemStack stack) {
        stack.set(DataComponentTypeRegistry.GUN.get(), new GunData(
                stack.get(DataComponentTypeRegistry.GUN.get()).bulletInChamber(),
                stack.get(DataComponentTypeRegistry.GUN.get()).bulletsInMag(),
                stack.get(DataComponentTypeRegistry.GUN.get()).isReloading(),
                reloadCooldown,
                stack.get(DataComponentTypeRegistry.GUN.get()).isChambering(),
                stack.get(DataComponentTypeRegistry.GUN.get()).chamberCooldown()));
    }

    public void reduceReloadCooldown(int amount, ItemStack stack) {
        stack.set(DataComponentTypeRegistry.GUN.get(), new GunData(
                stack.get(DataComponentTypeRegistry.GUN.get()).bulletInChamber(),
                stack.get(DataComponentTypeRegistry.GUN.get()).bulletsInMag(),
                stack.get(DataComponentTypeRegistry.GUN.get()).isReloading(),
                stack.get(DataComponentTypeRegistry.GUN.get()).reloadCooldown() - amount,
                stack.get(DataComponentTypeRegistry.GUN.get()).isChambering(),
                stack.get(DataComponentTypeRegistry.GUN.get()).chamberCooldown()));
    }


    public int getChamberCooldown(ItemStack stack) {
        return stack.get(DataComponentTypeRegistry.GUN.get()).chamberCooldown();
    }

    public void setChamberCooldown(int chamberCooldown, ItemStack stack) {
        stack.set(DataComponentTypeRegistry.GUN.get(), new GunData(
                stack.get(DataComponentTypeRegistry.GUN.get()).bulletInChamber(),
                stack.get(DataComponentTypeRegistry.GUN.get()).bulletsInMag(),
                stack.get(DataComponentTypeRegistry.GUN.get()).isReloading(),
                stack.get(DataComponentTypeRegistry.GUN.get()).reloadCooldown(),
                stack.get(DataComponentTypeRegistry.GUN.get()).isChambering(),
                chamberCooldown));
    }

    public void reduceChamberCooldown(int amount, ItemStack stack) {
        stack.set(DataComponentTypeRegistry.GUN.get(), new GunData(
                stack.get(DataComponentTypeRegistry.GUN.get()).bulletInChamber(),
                stack.get(DataComponentTypeRegistry.GUN.get()).bulletsInMag(),
                stack.get(DataComponentTypeRegistry.GUN.get()).isReloading(),
                stack.get(DataComponentTypeRegistry.GUN.get()).reloadCooldown(),
                stack.get(DataComponentTypeRegistry.GUN.get()).isChambering(),
                stack.get(DataComponentTypeRegistry.GUN.get()).chamberCooldown() - amount));
    }


    public boolean isChambering(ItemStack stack) {
        return stack.get(DataComponentTypeRegistry.GUN.get()).isChambering();
    }

    public void setChambering(boolean chambering, ItemStack stack) {
        stack.set(DataComponentTypeRegistry.GUN.get(), new GunData(
                stack.get(DataComponentTypeRegistry.GUN.get()).bulletInChamber(),
                stack.get(DataComponentTypeRegistry.GUN.get()).bulletsInMag(),
                stack.get(DataComponentTypeRegistry.GUN.get()).isReloading(),
                stack.get(DataComponentTypeRegistry.GUN.get()).reloadCooldown(),
                chambering,
                stack.get(DataComponentTypeRegistry.GUN.get()).chamberCooldown()));
    }


    public String shootCondition(ItemStack stack) {
        if (isReloading(stack) || isChambering(stack)) return "noShoot";

        if (getBulletsInMag(stack) == 0) return "shouldReload";

        if (isBulletInChamber(stack) && getBulletsInMag(stack) == 1) return "lastShot";

        if (isBulletInChamber(stack)) return "canShoot";

        return "shouldChamber";
    }

    public SoundEvent getFailSound(Player player, Level level) {
        return SoundEvents.ENCHANTMENT_TABLE_USE;
    }

    public SoundEvent getShootSound(Player player, Level level) {
        return SoundEvents.SNOWBALL_THROW;
    }

    public SoundEvent getReloadSound(Player player, Level level) {
        return SoundEvents.ALLAY_DEATH;
    }

    public SoundEvent getChamberSound(Player player, Level level) {
        return SoundEvents.ANVIL_USE;
    }

    public SoundEvent getLastShotSound(Player player, Level level) {
        return getShootSound(player, level);
    }
}
