package org.exodusstudio.frostbite.common.contracts;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.Utf8String;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;

import java.util.*;
import java.util.stream.Collectors;

public record Contract(
        List<String> positiveAttributes,
        List<String> negativeAttributes,
        Map<String, Integer> positiveScalableAttributes,
        Map<String, Integer> negativeScalableAttributes,
        ContractRank rank
) {
    public static Contract create(
            List<ContractAttribute> positiveAttributes,
            List<ContractAttribute> negativeAttributes,
            Map<ContractAttribute, Integer> positiveScalableAttributes,
            Map<ContractAttribute, Integer> negativeScalableAttributes,
            ContractRank rank
    ) {
        return new Contract(
                positiveAttributes.stream().map(a -> a.id).collect(Collectors.toList()),
                negativeAttributes.stream().map(a -> a.id).collect(Collectors.toList()),
                positiveScalableAttributes.entrySet().stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().id, Map.Entry::getValue)),
                negativeScalableAttributes.entrySet().stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().id, Map.Entry::getValue)),
                rank);
    }

    public static final Codec<Contract> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.listOf().fieldOf("positive_attributes").forGetter(Contract::positiveAttributes),
            Codec.STRING.listOf().fieldOf("negative_attributes").forGetter(Contract::negativeAttributes),
            Codec.unboundedMap(Codec.STRING, Codec.INT).fieldOf("positive_scalable_attributes").forGetter(Contract::positiveScalableAttributes),
            Codec.unboundedMap(Codec.STRING, Codec.INT).fieldOf("negative_scalable_attributes").forGetter(Contract::negativeScalableAttributes),
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
        return positiveAttributes.stream().allMatch(c -> a(c).getRank() == rank) &&
               negativeAttributes.stream().allMatch(c -> a(c).getRank() == rank) &&
               positiveScalableAttributes.entrySet().stream().allMatch(c -> ContractRank.fromNum(c.getValue()) == rank) &&
               negativeScalableAttributes.entrySet().stream().allMatch(c -> ContractRank.fromNum(c.getValue()) == rank);
    }

    public boolean noDuplicates() {
        return allAttributes().stream().map(a -> a.id).collect(Collectors.toSet()).size() == allAttributes().size();
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
        return positiveAttributes.stream().anyMatch(c -> a(c).id.equals(id)) ||
               negativeAttributes.stream().anyMatch(c -> a(c).id.equals(id)) ||
               positiveScalableAttributes.keySet().stream().anyMatch(c -> a(c).id.equals(id)) ||
               negativeScalableAttributes.keySet().stream().anyMatch(c -> a(c).id.equals(id));
    }

    public boolean hasAttribute(ContractAttribute attribute) {
        return hasAttribute(attribute.id);
    }

    public List<ContractAttribute> allAttributes() {
        List<String> allAttributes = new ArrayList<>();
        allAttributes.addAll(positiveAttributes);
        allAttributes.addAll(positiveScalableAttributes.keySet());
        allAttributes.addAll(negativeAttributes);
        allAttributes.addAll(negativeScalableAttributes.keySet());
        return allAttributes.stream().map(Contract::a).collect(Collectors.toList());
    }

    public Map<ContractAttribute, Integer> allScalableAttributes() {
        Map<String, Integer> allScalableAttributes = new HashMap<>();
        allScalableAttributes.putAll(positiveScalableAttributes);
        allScalableAttributes.putAll(negativeScalableAttributes);
        return allScalableAttributes.entrySet().stream().collect(Collectors.toMap(e -> a(e.getKey()), Map.Entry::getValue));
    }

    @SuppressWarnings("DataFlowIssue")
    public static Contract getContract(ItemStack stack) {
        Contract a;
        if (stack.has(DataComponentTypeRegistry.CONTRACT)) {
            if ((a = stack.get(DataComponentTypeRegistry.CONTRACT).contract()) != null) {
                return a;
            }
        }
        return null;
    }

    public static ContractAttribute a(String id) {
        ContractAttribute a = ContractAttributes.ATTRIBUTES.get(id);
        return a == null ? ContractAttributes.BERSERK : a;
    }

    public static void toBuffer(final ByteBuf buffer, Contract contract) {
        int positiveAttributesSize = contract.positiveAttributes.size();
        buffer.writeInt(positiveAttributesSize);
        for (String attribute : contract.positiveAttributes) {
            Utf8String.write(buffer, attribute, 32767);
        }

        int negativeAttributesSize = contract.negativeAttributes.size();
        buffer.writeInt(negativeAttributesSize);
        for (String attribute : contract.negativeAttributes) {
            Utf8String.write(buffer, attribute, 32767);
        }

        int positiveScalableAttributesSize = contract.positiveScalableAttributes.size();
        buffer.writeInt(positiveScalableAttributesSize);
        for (Map.Entry<String, Integer> attribute : contract.positiveScalableAttributes.entrySet()) {
            Utf8String.write(buffer, attribute.getKey(), 32767);
            buffer.writeInt(attribute.getValue());
        }

        int negativeScalableAttributesSize = contract.negativeScalableAttributes.size();
        buffer.writeInt(negativeScalableAttributesSize);
        for (Map.Entry<String, Integer> attribute : contract.negativeScalableAttributes.entrySet()) {
            Utf8String.write(buffer, attribute.getKey(), 32767);
            buffer.writeInt(attribute.getValue());
        }

        Utf8String.write(buffer, contract.rank().name(), 32767);
    }

    public static Contract fromBuffer(ByteBuf buffer) {
        int positiveAttributesSize = buffer.readInt();
        List<String> positiveAttributes = new ArrayList<>();
        for (int i = 0; i < positiveAttributesSize; i++) {
            positiveAttributes.add(Utf8String.read(buffer, 32767));
        }

        int negativeAttributesSize = buffer.readInt();
        List<String> negativeAttributes = new ArrayList<>();
        for (int i = 0; i < negativeAttributesSize; i++) {
            negativeAttributes.add(Utf8String.read(buffer, 32767));
        }

        int positiveScalableAttributesSize = buffer.readInt();
        Map<String, Integer> positiveScalableAttributes = new HashMap<>();
        for (int i = 0; i < positiveScalableAttributesSize; i++) {
            String key = Utf8String.read(buffer, 32767);
            int value = buffer.readInt();
            positiveScalableAttributes.put(key, value);
        }

        int negativeScalableAttributesSize = buffer.readInt();
        Map<String, Integer> negativeScalableAttributes = new HashMap<>();
        for (int i = 0; i < negativeScalableAttributesSize; i++) {
            String key = Utf8String.read(buffer, 32767);
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
