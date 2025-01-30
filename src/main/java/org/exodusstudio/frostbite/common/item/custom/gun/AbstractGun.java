package org.exodusstudio.frostbite.common.item.custom.gun;

import org.exodusstudio.frostbite.common.component.GunData;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.entity.custom.bullets.SniperBulletEntity;
import org.exodusstudio.frostbite.common.item.custom.gun.bullet.RevolverBulletItem;
import org.exodusstudio.frostbite.common.item.custom.gun.bullet.SniperBulletItem;
import net.minecraft.server.level.ServerLevel;
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
        playShootSound(player, level);

        switch (shootCondition(itemstack)) {
            case "noShoot":
                return InteractionResult.FAIL;
            case "canShoot":
                if (level instanceof ServerLevel serverlevel) {
                    shoot(serverlevel, player, hand, player.getItemInHand(hand), this.bulletType, this.bulletVelocity, this.bulletInaccuracy);
                }
                return InteractionResult.SUCCESS;
            case "shouldReload":
                reload(itemstack);
                return InteractionResult.SUCCESS;
            case "shouldChamber":
                chamberRound(itemstack);
                return InteractionResult.SUCCESS;
        }


        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResult.SUCCESS;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (isSelected && isReloading(stack)) {
            reduceReloadCooldown(1, stack);
        }
        if (getReloadCooldown(stack) <= 0) {
            setReloading(false, stack);
        }

        if (isSelected && isChambering(stack)) {
            reduceReloadCooldown(1, stack);
        }
        if (getChamberCooldown(stack) <= 0) {
            setChambering(false, stack);
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
            sniperBulletItem.createBullet(level);
        }
        else if (ammo.getItem() instanceof RevolverBulletItem revolverBulletItem) {
            return revolverBulletItem.createBullet(level);
        }

        return new SniperBulletEntity(EntityRegistry.SNIPER_BULLET_ENTITY.get(), level);
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

        if (isBulletInChamber(stack)) return "canShoot";

        if (getBulletsInMag(stack) == 0) return "shouldReload";

        return "shouldChamber";
    }

    public void playShootSound(Player player, Level level) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    public void playReloadSound(Player player, Level level) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    public void playChamberingSound(Player player, Level level) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
    }
}
