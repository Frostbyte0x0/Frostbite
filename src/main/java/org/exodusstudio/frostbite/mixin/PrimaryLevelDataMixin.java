package org.exodusstudio.frostbite.mixin;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
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
import org.exodusstudio.frostbite.common.block.HeaterBlock;
import org.exodusstudio.frostbite.common.block.HeaterStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(PrimaryLevelData.class)
public class PrimaryLevelDataMixin {
    @Inject(at = @At("HEAD"), method = "setTagData")
    public void save(RegistryAccess registry, CompoundTag nbt, CompoundTag playerNBT, CallbackInfo ci) {
//        List<HeaterStorage> heaters = Frostbite.savedHeaters;
//        nbt.putInt("heater_count", heaters.size());
//        for (int i = 0; i < heaters.size(); ++i) {
//            HeaterStorage heater = heaters.get(i);
//            nbt.put("heater_block" + i, Block.CODEC.codec()
//                    .encode(heater.getBlock(), registry.createSerializationContext(NbtOps.INSTANCE), NbtOps.INSTANCE.empty()).getOrThrow());
//            nbt.put("heater_pos" + i, BlockPos.CODEC
//                    .encode(heater.getPos(), registry.createSerializationContext(NbtOps.INSTANCE), NbtOps.INSTANCE.empty()).getOrThrow());
//        }
    }


    @Inject(at = @At("HEAD"), method = "parse")
    private static <T> void parse(Dynamic<T> tag, LevelSettings levelSettings, PrimaryLevelData.SpecialWorldProperty specialWorldProperty, WorldOptions worldOptions, Lifecycle worldGenSettingsLifecycle, CallbackInfoReturnable<PrimaryLevelData> cir) {
        //ListTag listTag = tag.get("heaters").flatMap((dynamic -> dynamic.convert(NbtOps.INSTANCE).getValue()));

        // ListTag[] tags = new ListTag[1];

//        for (int i = 0; i < tag.get("heater_count").asInt(0); ++i) {
//            String heaterBlockKey = "heater_block" + i;
//            String heaterPosKey = "heater_pos" + i;
//
//            tag.get(heaterBlockKey).flatMap(CompoundTag.CODEC::parse).result().ifPresent(blockTag -> {
//                Block block = Block.CODEC.codec().parse(new Dynamic<>(NbtOps.INSTANCE, blockTag)).resultOrPartial((p_330102_) ->
//                        Frostbite.LOGGER.error("Tried to load invalid block: '{}'", p_330102_)).get();
//                tag.get(heaterPosKey).flatMap(BlockPos.CODEC::parse).resultOrPartial((p_330102_) ->
//                        Frostbite.LOGGER.error("Tried to load invalid block pos: '{}'", p_330102_)).ifPresent(blockPos -> {
//                    Frostbite.savedHeaters.add(new HeaterStorage(blockPos, (HeaterBlock) block));
//                });
//            });
//        }


        //CompoundTag var8 = (CompoundTag) tag.get("heater_count").flatMap(var10003::parse).result().orElse(null);

//        tag.get("heaters").flatMap((dynamic) -> {
//            Tag tag1 = dynamic.convert(NbtOps.INSTANCE).getValue();
//            tags[0] = (ListTag) tag1;
//            return DataResult.success(tag1, Lifecycle.stable());
//        });

        //ListTag listTag = tags[0];

//        for (int i = 0; i < listTag.size(); ++i) {
//            CompoundTag compoundtag = listTag.getCompound(i);
//
//            Block block = Block.CODEC.codec().parse(tag).resultOrPartial((p_330102_) ->
//                    Frostbite.LOGGER.error("Tried to load invalid block: '{}'", p_330102_)).get();
//
//            BlockPos blockPos = BlockPos.CODEC.parse(tag).resultOrPartial((p_330102_) ->
//                    Frostbite.LOGGER.error("Tried to load invalid block pos: '{}'", p_330102_)).get();
//
//            Frostbite.savedHeaters.add(new HeaterStorage(blockPos, (HeaterBlock) block));
//        }
    }
}
