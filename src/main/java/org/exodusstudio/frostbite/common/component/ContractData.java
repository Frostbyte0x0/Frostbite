package org.exodusstudio.frostbite.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.exodusstudio.frostbite.common.contracts.Contract;

public record ContractData(Contract contract) {
    public static ContractData EMPTY = new ContractData(Contract.EMPTY);

    public static final Codec<ContractData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(Contract.CODEC.fieldOf("contract").forGetter(ContractData::contract)).apply(instance, ContractData::new));

    public static final StreamCodec<ByteBuf, ContractData> STREAM_CODEC =
            StreamCodec.of(
                    (b, d) -> Contract.toBuffer(b, d.contract()),
                    b -> new ContractData(Contract.fromBuffer(b))
            );
}
