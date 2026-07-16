package org.exodusstudio.frostbite.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;

public record CooldownData(int lastUsed) {
    public static CooldownData EMPTY = new CooldownData(0);

    public static final Codec<CooldownData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(Codec.INT.fieldOf("lastUsed").forGetter(CooldownData::lastUsed)).apply(instance, CooldownData::new));

    public static final StreamCodec<ByteBuf, CooldownData> STREAM_CODEC =
            StreamCodec.composite(ByteBufCodecs.INT, CooldownData::lastUsed, CooldownData::new);

    public static void setLastUsed(ItemStack stack) {
        assert Minecraft.getInstance().level != null;
        CooldownData cooldownData = new CooldownData((int) Minecraft.getInstance().level.getGameTime());
        stack.set(DataComponentTypeRegistry.COOLDOWN, cooldownData);
    }

    public static boolean canUse(ItemStack stack, long currentTime, float cooldownSeconds) {
        CooldownData cooldownData = stack.get(DataComponentTypeRegistry.COOLDOWN);
        if (cooldownData == null) {
            return false;
        }
        return secondsSinceLastUsed(stack, currentTime) >= cooldownSeconds;
    }

    public static int secondsSinceLastUsed(ItemStack stack, long currentTime) {
        CooldownData cooldownData = stack.get(DataComponentTypeRegistry.COOLDOWN);
        if (cooldownData == null) {
            return -1;
        }
        assert Minecraft.getInstance().level != null;
        return (int) (currentTime - cooldownData.lastUsed()) / 20;
    }
}
