package org.exodusstudio.frostbite.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.exodusstudio.frostbite.common.contracts.ContractAttribute;

public record ContractAttributeData(ContractAttribute attribute, int level) {
    public static final Codec<ContractAttributeData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    ContractAttribute.CODEC.fieldOf("attribute").forGetter(ContractAttributeData::attribute),
                    Codec.INT.fieldOf("level").forGetter(ContractAttributeData::level)
            ).apply(instance, ContractAttributeData::new));

    public static final StreamCodec<ByteBuf, ContractAttributeData> STREAM_CODEC =
            StreamCodec.of(
                    (b, d) -> {
                        ContractAttribute.toBuffer(b, d.attribute());
                        b.writeInt(d.level());
                    },
                    b -> new ContractAttributeData(ContractAttribute.fromBuffer(b), b.readInt())
            );
}
