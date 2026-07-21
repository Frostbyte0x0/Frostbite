package org.exodusstudio.frostbite.common.contracts;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.Utf8String;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.common.item.contract.ContractItem;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;

import java.util.*;
import java.util.stream.Collectors;

public record Contract(
        List<ContractAttribute> positiveAttributes,
        List<ContractAttribute> negativeAttributes,
        Map<ScalableContractAttribute, Integer> positiveScalableAttributes,
        Map<ScalableContractAttribute, Integer> negativeScalableAttributes,
        ContractRank rank
) {
    public static final Codec<Contract> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ContractAttribute.CODEC.listOf().fieldOf("positive_attributes").forGetter(Contract::positiveAttributes),
            ContractAttribute.CODEC.listOf().fieldOf("negative_attributes").forGetter(Contract::negativeAttributes),
            Codec.unboundedMap(ScalableContractAttribute.CODEC, Codec.INT).fieldOf("positive_scalable_attributes").forGetter(Contract::positiveScalableAttributes),
            Codec.unboundedMap(ScalableContractAttribute.CODEC, Codec.INT).fieldOf("negative_scalable_attributes").forGetter(Contract::negativeScalableAttributes),
            ContractRank.CODEC.fieldOf("rank").forGetter(Contract::rank)
    ).apply(instance, Contract::new));

    public static final StreamCodec<ByteBuf, Contract> STREAM_CODEC = StreamCodec.of(
            Contract::toBuffer,
            Contract::fromBuffer
    );

    public static final Contract EMPTY = new Contract(
            List.of(),
            List.of(),
            Map.of(),
            Map.of(),
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
               positiveScalableAttributes.entrySet().stream().allMatch(c -> ContractRank.fromNum(c.getValue()) == rank) &&
               negativeScalableAttributes.entrySet().stream().allMatch(c -> ContractRank.fromNum(c.getValue()) == rank);
    }

    public boolean noDuplicates() {
        return new HashSet<>(allAttributes()).size() == allAttributes().size();
    }

    public boolean allValidTargets() {
        ContractTarget strictestTarget = getStrictestTarget();
        if (strictestTarget != ContractTarget.PLAYER)
            return allAttributes().stream().allMatch(c -> c.getTarget() == strictestTarget);
        return allAttributes().stream().allMatch(c -> c.getTarget() == ContractTarget.LIVING || c.getTarget() == ContractTarget.PLAYER);
    }

    public ContractTarget getStrictestTarget() {
        List<ContractAttribute> att = allAttributes();
        ContractTarget strictestTarget = att.getFirst().getTarget();
        if (strictestTarget != ContractTarget.LIVING) return strictestTarget;

        for (ContractAttribute attribute : att) {
            if (attribute.getTarget() == ContractTarget.PLAYER) return ContractTarget.PLAYER;
        }
        return strictestTarget;
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
               positiveScalableAttributes.keySet().stream().anyMatch(c -> c.id.equals(id)) ||
               negativeScalableAttributes.keySet().stream().anyMatch(c -> c.id.equals(id));
    }

    public boolean hasAttribute(ContractAttribute attribute) {
        return hasAttribute(attribute.id);
    }

    public List<ContractAttribute> allAttributes() {
        List<ContractAttribute> allAttributes = new ArrayList<>();
        allAttributes.addAll(positiveAttributes);
        allAttributes.addAll(negativeAttributes);
        allAttributes.addAll(positiveScalableAttributes.keySet());
        allAttributes.addAll(negativeScalableAttributes.keySet());
        return allAttributes;
    }

    public Map<ScalableContractAttribute, Integer> allScalableAttributes() {
        Map<ScalableContractAttribute, Integer> allScalableAttributes = new HashMap<>();
        allScalableAttributes.putAll(positiveScalableAttributes);
        allScalableAttributes.putAll(negativeScalableAttributes);
        return allScalableAttributes;
    }

    @SuppressWarnings("DataFlowIssue")
    public static Contract getContract(ItemStack stack) {
        Contract a;
        if (stack.getItem() instanceof ContractItem && (a = stack.get(DataComponentTypeRegistry.CONTRACT).contract()) != null) {
            return a;
        }
        return null;
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
        for (Map.Entry<ScalableContractAttribute, Integer> attribute : contract.positiveScalableAttributes.entrySet()) {
            ScalableContractAttribute.toBuffer(buffer, attribute.getKey());
            buffer.writeInt(attribute.getValue());
        }

        int negativeScalableAttributesSize = contract.negativeScalableAttributes.size();
        buffer.writeInt(negativeScalableAttributesSize);
        for (Map.Entry<ScalableContractAttribute, Integer> attribute : contract.negativeScalableAttributes.entrySet()) {
            ScalableContractAttribute.toBuffer(buffer, attribute.getKey());
            buffer.writeInt(attribute.getValue());
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
        Map<ScalableContractAttribute, Integer> positiveScalableAttributes = new HashMap<>();
        for (int i = 0; i < positiveScalableAttributesSize; i++) {
            ScalableContractAttribute key = ScalableContractAttribute.fromBuffer(buffer);
            int value = buffer.readInt();
            positiveScalableAttributes.put(key, value);
        }

        int negativeScalableAttributesSize = buffer.readInt();
        Map<ScalableContractAttribute, Integer> negativeScalableAttributes = new HashMap<>();
        for (int i = 0; i < negativeScalableAttributesSize; i++) {
            ScalableContractAttribute key = ScalableContractAttribute.fromBuffer(buffer);
            int value = buffer.readInt();
            negativeScalableAttributes.put(key, value);
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
