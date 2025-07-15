package org.exodusstudio.frostbite.mixin;

import com.google.common.collect.Sets;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.block.HeaterStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(PrimaryLevelData.class)
public class PrimaryLevelDataMixin {
    @Unique
    PrimaryLevelData frostbite$levelData = (PrimaryLevelData) ((Object) this);

    @Inject(at = @At("HEAD"), method = "setTagData")
    public void save(RegistryAccess registry, CompoundTag nbt, CompoundTag playerNBT, CallbackInfo ci) {
        ListTag listTag = new ListTag();
        List<HeaterStorage> heaters = Frostbite.savedHeaters;
        for (HeaterStorage heater : heaters) {
            ListTag heaterTag = new ListTag();
            CompoundTag compoundtag = new CompoundTag();
            heaterTag.add(Block.CODEC.codec()
                    .encode(heater.getBlock(), registry.createSerializationContext(NbtOps.INSTANCE), compoundtag).getOrThrow());
            CompoundTag compoundtag1 = new CompoundTag();
            heaterTag.add(BlockPos.CODEC
                    .encode(heater.getPos(), registry.createSerializationContext(NbtOps.INSTANCE), compoundtag1).getOrThrow());
            listTag.add(heaterTag);
        }

        nbt.put("heaters", listTag);
    }


    @Inject(at = @At("HEAD"), method = "parse")
    private static <T> void parse(Dynamic<T> tag, LevelSettings levelSettings, PrimaryLevelData.SpecialWorldProperty specialWorldProperty, WorldOptions worldOptions, Lifecycle worldGenSettingsLifecycle, CallbackInfoReturnable<PrimaryLevelData> cir) {
        ListTag listTag = tag.get("heaters").flatMap((dynamic -> dynamic.convert(NbtOps.INSTANCE).getValue()));

        //ListTag[] tags = new

        ListTag listTag = tag.get("heaters").flatMap((dynamic) -> {
            Tag tag1 = dynamic.convert(NbtOps.INSTANCE).getValue();

            DataResult<Object> var10000;
            if (tag1 instanceof CompoundTag compoundtag) {
                var10000 = DataResult.success(compoundtag == dynamic.getValue() ? compoundtag.copy() : compoundtag);
            } else {
                var10000 = DataResult.error(() -> "Not a compound tag: " + String.valueOf(tag1));
            }

            return var10000;
        }).result().stream().toList();

        for (int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundtag = listTag.getCompound(i);

            Block block = Block.CODEC.codec().parse(tag).resultOrPartial((p_330102_) ->
                    Frostbite.LOGGER.error("Tried to load invalid block: '{}'", p_330102_)).get();

            Frostbite.savedHeaters.add(new HeaterStorage(, block));
        }
    }
}
