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
import org.exodusstudio.frostbite.common.block.HeaterBlock;
import org.exodusstudio.frostbite.common.block.HeaterStorage;
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
    }
}
