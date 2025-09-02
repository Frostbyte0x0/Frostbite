package org.exodusstudio.frostbite.mixin;

import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.block.HeaterStorage;
import org.exodusstudio.frostbite.common.structures.FTOPortal;
import org.exodusstudio.frostbite.common.structures.OTFPortal;
import org.exodusstudio.frostbite.common.weather.WeatherInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PrimaryLevelData.class)
public class PrimaryLevelDataMixin {
    @Inject(at = @At("HEAD"), method = "setTagData")
    public void save(RegistryAccess registry, CompoundTag nbt, CompoundTag playerNBT, CallbackInfo ci) {
        assert Minecraft.getInstance().level != null;
        List<HeaterStorage> heaters = Frostbite.savedHeaters;

        nbt.putInt("heater_count", heaters.size());
        for (int i = 0; i < heaters.size(); i++) {
            HeaterStorage heater = heaters.get(i);
            nbt.putInt("heater_posX" + i, (int) heater.getPos().getCenter().x);
            nbt.putInt("heater_posY" + i, (int) heater.getPos().getCenter().y);
            nbt.putInt("heater_posZ" + i, (int) heater.getPos().getCenter().z);
            nbt.putString("heater_dimension" + i, heater.getDimensionName());
        }

        nbt.putBoolean("canSpawnOTF", OTFPortal.canSpawn);
        nbt.putBoolean("canSpawnFTO", FTOPortal.canSpawn);

        nbt.putIntArray("frostbiteSpawnPoint", new int[]{
                Frostbite.frostbiteSpawnPoint.getX(),
                Frostbite.frostbiteSpawnPoint.getY(),
                Frostbite.frostbiteSpawnPoint.getZ()});
        nbt.putIntArray("overworldSpawnPoint", new int[]{
                Frostbite.overworldSpawnPoint.getX(),
                Frostbite.overworldSpawnPoint.getY(),
                Frostbite.overworldSpawnPoint.getZ()});

        nbt.putInt("snowTime", Frostbite.weatherInfo.snowTime);
        nbt.putInt("blizzardTime", Frostbite.weatherInfo.blizzardTime);
        nbt.putInt("whiteoutTime", Frostbite.weatherInfo.whiteoutTime);
        nbt.putBoolean("isBlizzarding", Frostbite.weatherInfo.isBlizzarding);
        nbt.putBoolean("isWhiteouting", Frostbite.weatherInfo.isWhiteouting);
        nbt.putFloat("blizzardLevel", Frostbite.weatherInfo.blizzardLevel);
        nbt.putFloat("whiteoutLevel", Frostbite.weatherInfo.whiteoutLevel);
    }


    @Inject(at = @At("HEAD"), method = "parse")
    private static <T> void parse(Dynamic<T> tag, LevelSettings levelSettings, PrimaryLevelData.SpecialWorldProperty specialWorldProperty, WorldOptions worldOptions, Lifecycle worldGenSettingsLifecycle, CallbackInfoReturnable<PrimaryLevelData> cir) {
        for (int i = 0; i < tag.get("heater_count").asInt(0); i++) {
            String heaterDimensionKey = "heater_dimension" + i;

            BlockPos blockPos = BlockPos.containing(tag.get("heater_posX" + i).asInt(0),
                    tag.get("heater_posY" + i).asInt(0),
                    tag.get("heater_posZ" + i).asInt(0) - 1);

            Frostbite.savedHeaters.add(new HeaterStorage(blockPos, null, tag.get(heaterDimensionKey).asString("")));
        }

        OTFPortal.canSpawn = tag.get("canSpawnOTF").asBoolean().getOrThrow();
        FTOPortal.canSpawn = tag.get("canSpawnFTO").asBoolean().getOrThrow();

        int[] pos = tag.get("frostbiteSpawnPoint").asIntStream().toArray();
        int[] pos1 = tag.get("overworldSpawnPoint").asIntStream().toArray();
        Frostbite.frostbiteSpawnPoint = new BlockPos(pos[0], pos[1], pos[2]);
        Frostbite.overworldSpawnPoint = new BlockPos(pos1[0], pos1[1], pos1[2]);

        Boolean isBlizzarding = tag.get("isBlizzarding").asBoolean().getOrThrow();
        Boolean isWhiteouting = tag.get("isWhiteouting").asBoolean().getOrThrow();

        Frostbite.weatherInfo = new WeatherInfo(
                tag.get("snowTime").asInt(0),
                tag.get("blizzardTime").asInt(0),
                tag.get("whiteoutTime").asInt(0),
                isBlizzarding,
                isWhiteouting,
                tag.get("blizzardLevel").asFloat(0),
                tag.get("whiteoutLevel").asFloat(0));

        if (isWhiteouting) {
            Frostbite.weatherInfo.setWhiteouting(0);
        } else if (isBlizzarding) {
            Frostbite.weatherInfo.setBlizzarding(0);
        } else {
            Frostbite.weatherInfo.setSnowing(0);
        }
    }
}
