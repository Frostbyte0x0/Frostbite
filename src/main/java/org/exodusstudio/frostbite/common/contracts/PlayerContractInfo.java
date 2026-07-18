package org.exodusstudio.frostbite.common.contracts;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.Utf8String;

public record PlayerContractInfo(
    PlayerLiteracy literacyRank,
    Contract contract
) {
    public static final Codec<PlayerContractInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                PlayerLiteracy.CODEC.fieldOf("contract_attribute").forGetter(PlayerContractInfo::literacyRank),
                Contract.CODEC.fieldOf("contract").forGetter(PlayerContractInfo::contract)
            ).apply(instance, PlayerContractInfo::new));

    public static final PlayerContractInfo EMPTY = new PlayerContractInfo(
            PlayerLiteracy.ILLITERATE,
            Contract.EMPTY
    );

    public static void toBuffer(final ByteBuf buffer, PlayerContractInfo contract) {
        Utf8String.write(buffer, contract.literacyRank().toString(), 32767);
        Contract.toBuffer(buffer, contract.contract());
    }

    public static PlayerContractInfo fromBuffer(ByteBuf buffer) {
        return new PlayerContractInfo(
                PlayerLiteracy.valueOf(Utf8String.read(buffer, 32767)),
                Contract.fromBuffer(buffer)
        );
    }
}
