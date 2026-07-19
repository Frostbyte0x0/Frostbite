package org.exodusstudio.frostbite.common.contracts;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.Utf8String;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;

public record Contract(
        List<ContractAttribute> positiveAttributes,
        List<ContractAttribute> negativeAttributes,
        List<ScalableContractAttribute> positiveScalableAttributes,
        List<ScalableContractAttribute> negativeScalableAttributes,
        ContractRank rank
) {
    public static final Codec<Contract> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ContractAttribute.CODEC.listOf().fieldOf("positive_attributes").forGetter(Contract::positiveAttributes),
            ContractAttribute.CODEC.listOf().fieldOf("negative_attributes").forGetter(Contract::negativeAttributes),
            ScalableContractAttribute.CODEC.listOf().fieldOf("positive_scalable_attributes").forGetter(Contract::positiveScalableAttributes),
            ScalableContractAttribute.CODEC.listOf().fieldOf("negative_scalable_attributes").forGetter(Contract::negativeScalableAttributes),
            ContractRank.CODEC.fieldOf("rank").forGetter(Contract::rank)
    ).apply(instance, Contract::new));

    public static final StreamCodec<ByteBuf, Contract> STREAM_CODEC = StreamCodec.of(
            Contract::toBuffer,
            Contract::fromBuffer
    );

    public static final Contract EMPTY = new Contract(
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            ContractRank.WHITE);

    public boolean isEmpty() {
        return positiveAttributes.isEmpty() &&
               negativeAttributes.isEmpty() &&
               positiveScalableAttributes.isEmpty() &&
               negativeScalableAttributes.isEmpty();
    }

    public boolean balanced() {
        return positiveAttributes.size() + positiveScalableAttributes.size() ==
               negativeAttributes.size() + negativeScalableAttributes.size();
    }

    public boolean allSameRank() {
        return positiveAttributes.stream().allMatch(c -> c.getRank() == rank) &&
                negativeAttributes.stream().allMatch(c -> c.getRank() == rank) &&
                positiveScalableAttributes.stream().allMatch(c -> c.getRank() == rank) &&
                negativeScalableAttributes.stream().allMatch(c -> c.getRank() == rank);
    }

    public boolean isPartial() {
        return positiveAttributes.size() + positiveScalableAttributes.size() < 4 &&
               negativeAttributes.size() + negativeScalableAttributes.size() < 4;
    }

    public boolean isComplete() {
        return positiveAttributes.size() + positiveScalableAttributes.size() == 4 &&
               negativeAttributes.size() + negativeScalableAttributes.size() == 4;
    }

    public boolean hasAttribute(String id) {
        return positiveAttributes.stream().anyMatch(c -> c.id.equals(id)) ||
               negativeAttributes.stream().anyMatch(c -> c.id.equals(id)) ||
               positiveScalableAttributes.stream().anyMatch(c -> c.id.equals(id)) ||
               negativeScalableAttributes.stream().anyMatch(c -> c.id.equals(id));
    }

    public boolean hasAttribute(ContractAttribute attribute) {
        return hasAttribute(attribute.id);
    }

    public List<ContractAttribute> allAttributes() {
        List<ContractAttribute> allAttributes = new ArrayList<>();
        allAttributes.addAll(positiveAttributes);
        allAttributes.addAll(negativeAttributes);
        allAttributes.addAll(positiveScalableAttributes);
        allAttributes.addAll(negativeScalableAttributes);
        return allAttributes;
    }

    public static void toBuffer(final ByteBuf buffer, Contract contract) {
        int positiveAttributesSize = contract.positiveAttributes.size();
        buffer.writeInt(positiveAttributesSize);
        for (ContractAttribute attribute : contract.positiveAttributes) {
            ContractAttribute.toBuffer(buffer, attribute);
        }

        int negativeAttributesSize = contract.negativeAttributes.size();
        buffer.writeInt(negativeAttributesSize);
        for (ContractAttribute attribute : contract.negativeAttributes) {
            ContractAttribute.toBuffer(buffer, attribute);
        }

        int positiveScalableAttributesSize = contract.positiveScalableAttributes.size();
        buffer.writeInt(positiveScalableAttributesSize);
        for (ScalableContractAttribute attribute : contract.positiveScalableAttributes) {
            ScalableContractAttribute.toBuffer(buffer, attribute);
        }

        int negativeScalableAttributesSize = contract.negativeScalableAttributes.size();
        buffer.writeInt(negativeScalableAttributesSize);
        for (ScalableContractAttribute attribute : contract.negativeScalableAttributes) {
            ScalableContractAttribute.toBuffer(buffer, attribute);
        }

        Utf8String.write(buffer, contract.rank().name(), 32767);
    }

    public static Contract fromBuffer(ByteBuf buffer) {
        int positiveAttributesSize = buffer.readInt();
        List<ContractAttribute> positiveAttributes = new ArrayList<>();
        for (int i = 0; i < positiveAttributesSize; i++) {
            positiveAttributes.add(ContractAttribute.fromBuffer(buffer));
        }

        int negativeAttributesSize = buffer.readInt();
        List<ContractAttribute> negativeAttributes = new ArrayList<>();
        for (int i = 0; i < negativeAttributesSize; i++) {
            negativeAttributes.add(ContractAttribute.fromBuffer(buffer));
        }

        int positiveScalableAttributesSize = buffer.readInt();
        List<ScalableContractAttribute> positiveScalableAttributes = new ArrayList<>();
        for (int i = 0; i < positiveScalableAttributesSize; i++) {
            positiveScalableAttributes.add(ScalableContractAttribute.fromBuffer(buffer));
        }

        int negativeScalableAttributesSize = buffer.readInt();
        List<ScalableContractAttribute> negativeScalableAttributes = new ArrayList<>();
        for (int i = 0; i < negativeScalableAttributesSize; i++) {
            negativeScalableAttributes.add(ScalableContractAttribute.fromBuffer(buffer));
        }

        return new Contract(
                positiveAttributes,
                negativeAttributes,
                positiveScalableAttributes,
                negativeScalableAttributes,
                ContractRank.valueOf(Utf8String.read(buffer, 32767))
        );
    }
}
