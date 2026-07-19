package org.exodusstudio.frostbite.common.contracts;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.Utf8String;
import net.minecraft.network.chat.Component;

public class ContractAttribute {
    public final String id;
    private final ContractRank rank;
    private final Polarity polarity;

    public ContractAttribute(String id, ContractRank rank, Polarity polarity) {
        this.id = id;
        this.rank = rank;
        this.polarity = polarity;
    }

    public Component getDisplayName() {
        return Component.translatable("contract.attribute." + id);
    }

    public ContractRank getRank() {
        return rank;
    }

    public Polarity getPolarity() {
        return polarity;
    }

    public static final Codec<ContractAttribute> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("id").forGetter(attribute -> attribute.id),
            ContractRank.CODEC.fieldOf("rank").forGetter(attribute -> attribute.rank),
            Polarity.CODEC.fieldOf("polarity").forGetter(attribute -> attribute.polarity)
    ).apply(instance, ContractAttribute::new));

    public static void toBuffer(final ByteBuf buffer, ContractAttribute attribute) {
        Utf8String.write(buffer, attribute.id, 32767);
        Utf8String.write(buffer, attribute.getRank().name(), 32767);
        Utf8String.write(buffer, attribute.getPolarity().name(), 32767);
    }

    public static ContractAttribute fromBuffer(ByteBuf buffer) {
        return new ContractAttribute(
                Utf8String.read(buffer, 32767),
                ContractRank.valueOf(Utf8String.read(buffer, 32767)),
                Polarity.valueOf(Utf8String.read(buffer, 32767))
        );
    }
}
