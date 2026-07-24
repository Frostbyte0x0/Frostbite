package org.exodusstudio.frostbite.common.contracts;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.Utf8String;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.exodusstudio.frostbite.common.registry.AttachmentRegistry;

import java.util.ArrayList;
import java.util.List;

public record LivingContractInfo(
    Literacy literacyRank,
    List<String> discoveredAttributes,
    Contract contract
) {
    public static final Codec<LivingContractInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Literacy.CODEC.fieldOf("contract_attribute").forGetter(LivingContractInfo::literacyRank),
                Codec.STRING.listOf().fieldOf("discovered_attributes").forGetter(LivingContractInfo::discoveredAttributes),
                Contract.CODEC.fieldOf("contract").forGetter(LivingContractInfo::contract)
            ).apply(instance, LivingContractInfo::new));

    public static final LivingContractInfo EMPTY = new LivingContractInfo(
            Literacy.ILLITERATE,
            new ArrayList<>(),
            Contract.EMPTY
    );

    public int getDiscoveredNb() {
        return discoveredAttributes.size();
    }

    public static void addDiscoveredAttribute(LivingEntity entity, ContractAttribute attribute) {
        if (hasDiscoveredAttribute(entity, attribute)) return;

        LivingContractInfo old = entity.getData(AttachmentRegistry.LIVING_CONTRACT_INFO);
        List<String> newAttributes = new ArrayList<>(old.discoveredAttributes());
        newAttributes.add(attribute.id);

        Literacy literacy = old.literacyRank();
        if (literacy.hasNext() && newAttributes.size() >= literacy.next().discoveredNb) {
            literacy = literacy.next();
        }

        entity.setData(AttachmentRegistry.LIVING_CONTRACT_INFO.get(), new LivingContractInfo(
                literacy,
                newAttributes,
                old.contract()
        ));
    }

    public static boolean hasDiscoveredAttribute(LivingEntity entity, ContractAttribute attribute) {
        LivingContractInfo info = entity.getData(AttachmentRegistry.LIVING_CONTRACT_INFO);
        return info.discoveredAttributes().stream().anyMatch(a -> a.equals(attribute.id));
    }

    public static boolean hasAppliedAttribute(LivingEntity entity, ContractAttribute attribute) {
        if (entity instanceof ServerPlayer player && player.connection == null) return false;
        LivingContractInfo info = entity.getData(AttachmentRegistry.LIVING_CONTRACT_INFO);
        return info.contract.allAttributes().stream().anyMatch(a -> a.id.equals(attribute.id));
    }

    public static int getAppliedAttributeLevel(LivingEntity entity, ScalableContractAttribute attribute) {
        LivingContractInfo info = entity.getData(AttachmentRegistry.LIVING_CONTRACT_INFO);
        return info.contract.allScalableAttributes().get(attribute) != null ? info.contract.allScalableAttributes().get(attribute) : 1;
    }

    public static float getAppliedAttributeStat(LivingEntity entity, ScalableContractAttribute attribute) {
        return ScalableContractAttribute.getStat(getContract(entity), attribute);
    }

    public static void setContract(LivingEntity entity, Contract contract) {
        LivingContractInfo old = entity.getData(AttachmentRegistry.LIVING_CONTRACT_INFO);
        entity.setData(AttachmentRegistry.LIVING_CONTRACT_INFO.get(), new LivingContractInfo(
                old.literacyRank(),
                old.discoveredAttributes(),
                contract
        ));
        ContractAttribute.removeAttributeModifiers(entity.getAttributes(), old.contract().allScalableAttributes());
        ContractAttribute.addAttributeModifiers(entity.getAttributes(), contract.allScalableAttributes());
    }

    public static Contract getContract(LivingEntity entity) {
        LivingContractInfo info = entity.getData(AttachmentRegistry.LIVING_CONTRACT_INFO);
        return info.contract();
    }

    public static void toBuffer(final ByteBuf buffer, LivingContractInfo contract) {
        buffer.writeInt(contract.discoveredAttributes().size());
        for (String attribute : contract.discoveredAttributes()) {
            Utf8String.write(buffer, attribute, 32767);
        }
        Utf8String.write(buffer, contract.literacyRank().toString(), 32767);
        Contract.toBuffer(buffer, contract.contract());
    }

    public static LivingContractInfo fromBuffer(ByteBuf buffer) {
        int size = buffer.readInt();
        List<String> attributes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            attributes.add(Utf8String.read(buffer, 32767));
        }
        return new LivingContractInfo(
                Literacy.valueOf(Utf8String.read(buffer, 32767)),
                attributes,
                Contract.fromBuffer(buffer)
        );
    }
}
