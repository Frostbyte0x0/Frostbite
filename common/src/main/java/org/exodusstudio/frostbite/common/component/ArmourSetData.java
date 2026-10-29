package org.exodusstudio.frostbite.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.exodusstudio.frostbite.common.item.armour.ArmourSet;

public record ArmourSetData(
    ArmourSet set
) {
    public static final Codec<ArmourSetData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ArmourSet.CODEC.fieldOf("attribute").forGetter(ArmourSetData::set)
    ).apply(instance, ArmourSetData::new));

    public static final StreamCodec<ByteBuf, ArmourSetData> STREAM_CODEC =
            StreamCodec.of(
                    (b, d) -> ArmourSet.toBuffer(b, d.set()),
                    b -> new ArmourSetData(ArmourSet.fromBuffer(b))
            );
}
