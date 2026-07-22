package org.exodusstudio.frostbite.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.exodusstudio.frostbite.common.contracts.ScalableContractAttribute;

public record ScalableContractAttributeData(ScalableContractAttribute attribute, int level) {
    public static final Codec<ScalableContractAttributeData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ScalableContractAttribute.CODEC.fieldOf("attribute").forGetter(ScalableContractAttributeData::attribute),
            Codec.INT.fieldOf("level").forGetter(ScalableContractAttributeData::level)
    ).apply(instance, ScalableContractAttributeData::new));

    public static final StreamCodec<ByteBuf, ScalableContractAttributeData> STREAM_CODEC =
            StreamCodec.of(
                    (b, d) -> {
                        ScalableContractAttribute.toBuffer(b, d.attribute());
                        b.writeInt(d.level());
                    },
                    b -> new ScalableContractAttributeData(ScalableContractAttribute.fromBuffer(b), b.readInt())
            );
}
