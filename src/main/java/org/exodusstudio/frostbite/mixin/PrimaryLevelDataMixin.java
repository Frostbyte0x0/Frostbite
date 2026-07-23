package org.exodusstudio.frostbite.mixin;

import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.structures.FTOPortal;
import org.exodusstudio.frostbite.common.structures.OTFPortal;
import org.exodusstudio.frostbite.common.util.HeaterStorage;
import org.exodusstudio.frostbite.common.weather.WeatherInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Mixin(PrimaryLevelData.class)
public class PrimaryLevelDataMixin {
    @Inject(at = @At("HEAD"), method = "setTagData")
    public void save(CompoundTag tag, UUID singlePlayerUUID, CallbackInfo ci) {
        assert Minecraft.getInstance().level != null;
        List<HeaterStorage> heaters = Frostbite.heaterStorages;

        tag.putInt("heater_count", heaters.size());
        for (int i = 0; i < heaters.size(); i++) {
            HeaterStorage heater = heaters.get(i);
            tag.putInt("heater_posX" + i, (int) Vec3.atCenterOf(heater.getPos()).x);
            tag.putInt("heater_posY" + i, (int) Vec3.atCenterOf(heater.getPos()).y);
            tag.putInt("heater_posZ" + i, (int) Vec3.atCenterOf(heater.getPos()).z);
            tag.putString("heater_dimension" + i, heater.getDimensionName());
        }

        tag.putBoolean("canSpawnOTF", OTFPortal.canSpawn);
        tag.putBoolean("canSpawnFTO", FTOPortal.canSpawn);

        tag.putIntArray("frostbiteSpawnPoint", new int[]{
                Frostbite.frostbiteSpawnPoint.getX(),
                Frostbite.frostbiteSpawnPoint.getY(),
                Frostbite.frostbiteSpawnPoint.getZ()});
        tag.putIntArray("overworldSpawnPoint", new int[]{
                Frostbite.overworldSpawnPoint.getX(),
                Frostbite.overworldSpawnPoint.getY(),
                Frostbite.overworldSpawnPoint.getZ()});

//        tag.putInt("snowTime", Frostbite.weatherInfo.snowTime);
//        tag.putInt("blizzardTime", Frostbite.weatherInfo.blizzardTime);
//        tag.putInt("whiteoutTime", Frostbite.weatherInfo.whiteoutTime);
//        tag.putBoolean("isBlizzarding", Frostbite.weatherInfo.isBlizzarding);
//        tag.putBoolean("isWhiteouting", Frostbite.weatherInfo.isWhiteouting);
//        tag.putFloat("blizzardLevel", Frostbite.weatherInfo.blizzardLevel);
//        tag.putFloat("whiteoutLevel", Frostbite.weatherInfo.whiteoutLevel);
    }


    @Inject(at = @At("HEAD"), method = "parse")
    private static <T> void parse(Dynamic<T> input, LevelSettings settings, PrimaryLevelData.SpecialWorldProperty specialWorldProperty, Lifecycle worldGenSettingsLifecycle, CallbackInfoReturnable<PrimaryLevelData> cir) {
        for (int i = 0; i < input.get("heater_count").asInt(0); i++) {
            String heaterDimensionKey = "heater_dimension" + i;

            BlockPos blockPos = BlockPos.containing(input.get("heater_posX" + i).asInt(0),
                    input.get("heater_posY" + i).asInt(0),
                    input.get("heater_posZ" + i).asInt(0) - 1);

            Frostbite.heaterStorages.add(new HeaterStorage(blockPos, null, input.get(heaterDimensionKey).asString("")));
        }

        OTFPortal.canSpawn = input.get("canSpawnOTF").asBoolean(true);
        FTOPortal.canSpawn = input.get("canSpawnFTO").asBoolean(true);

        int[] pos = input.get("frostbiteSpawnPoint").asIntStream().toArray();
        int[] pos1 = input.get("overworldSpawnPoint").asIntStream().toArray();
        if (Arrays.stream(pos).findAny().isPresent() && Arrays.stream(pos1).findAny().isPresent()) {
            Frostbite.frostbiteSpawnPoint = new BlockPos(pos[0], pos[1], pos[2]);
            Frostbite.overworldSpawnPoint = new BlockPos(pos1[0], pos1[1], pos1[2]);
        }

//        boolean isBlizzarding = input.get("isBlizzarding").asBoolean(false);
//        boolean isWhiteouting = input.get("isWhiteouting").asBoolean(false);
//
//        Frostbite.weatherInfo = new WeatherInfo(
//                input.get("snowTime").asInt(0),
//                input.get("blizzardTime").asInt(0),
//                input.get("whiteoutTime").asInt(0),
//                isBlizzarding,
//                isWhiteouting,
//                input.get("blizzardLevel").asFloat(0),
//                input.get("whiteoutLevel").asFloat(0));
//
//        if (isWhiteouting) {
//            Frostbite.weatherInfo.setWhiteouting();
//        } else if (isBlizzarding) {
//            Frostbite.weatherInfo.setBlizzarding();
//        } else {
//            Frostbite.weatherInfo.setSnowing();
//        }
    }
}
