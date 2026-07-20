package org.exodusstudio.frostbite.common.contracts;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.Utf8String;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.common.registry.AttachmentRegistry;

import java.util.ArrayList;
import java.util.List;

public record PlayerContractInfo(
    PlayerLiteracy literacyRank,
    List<ContractAttribute> discoveredAttributes,
    Contract contract
) {
    public static final int BASIC = 3;
    public static final int PROFICIENT = 6;
    public static final int LITERATE = 9;

    public static final Codec<PlayerContractInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                PlayerLiteracy.CODEC.fieldOf("contract_attribute").forGetter(PlayerContractInfo::literacyRank),
                ContractAttribute.CODEC.listOf().fieldOf("discovered_attributes").forGetter(PlayerContractInfo::discoveredAttributes),
                Contract.CODEC.fieldOf("contract").forGetter(PlayerContractInfo::contract)
            ).apply(instance, PlayerContractInfo::new));

    public static final PlayerContractInfo EMPTY = new PlayerContractInfo(
            PlayerLiteracy.ILLITERATE,
            new ArrayList<>(),
            Contract.EMPTY
    );

    public int getDiscoveredNb() {
        return discoveredAttributes.size();
    }

    public static void addDiscoveredAttribute(Player player, ContractAttribute attribute) {
        if (hasDiscoveredAttribute(player, attribute)) return;

        PlayerContractInfo old = player.getData(AttachmentRegistry.PLAYER_CONTRACT_INFO);
        List<ContractAttribute> newAttributes = new ArrayList<>(old.discoveredAttributes());
        newAttributes.add(attribute);

        PlayerLiteracy literacy = old.literacyRank();
        if (newAttributes.size() >= BASIC && literacy == PlayerLiteracy.ILLITERATE) {
            literacy = PlayerLiteracy.BASIC;
        }
        if (newAttributes.size() >= PROFICIENT && literacy == PlayerLiteracy.BASIC) {
            literacy = PlayerLiteracy.PROFICIENT;
        }
        if (newAttributes.size() >= LITERATE && literacy == PlayerLiteracy.PROFICIENT) {
            literacy = PlayerLiteracy.LITERATE;
        }

        player.setData(AttachmentRegistry.PLAYER_CONTRACT_INFO.get(), new PlayerContractInfo(
                literacy,
                newAttributes,
                old.contract()
        ));
    }

    public static boolean hasDiscoveredAttribute(Player player, ContractAttribute attribute) {
        PlayerContractInfo info = player.getData(AttachmentRegistry.PLAYER_CONTRACT_INFO);
        return info.discoveredAttributes().contains(attribute);
    }

    public static void setContract(Player player, Contract contract) {
        PlayerContractInfo old = player.getData(AttachmentRegistry.PLAYER_CONTRACT_INFO);
        player.setData(AttachmentRegistry.PLAYER_CONTRACT_INFO.get(), new PlayerContractInfo(
                old.literacyRank(),
                old.discoveredAttributes(),
                contract
        ));
    }

    public static void toBuffer(final ByteBuf buffer, PlayerContractInfo contract) {
        buffer.writeInt(contract.discoveredAttributes().size());
        for (ContractAttribute attribute : contract.discoveredAttributes()) {
            ContractAttribute.toBuffer(buffer, attribute);
        }
        Utf8String.write(buffer, contract.literacyRank().toString(), 32767);
        Contract.toBuffer(buffer, contract.contract());
    }

    public static PlayerContractInfo fromBuffer(ByteBuf buffer) {
        int size = buffer.readInt();
        List<ContractAttribute> attributes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            attributes.add(ContractAttribute.fromBuffer(buffer));
        }
        return new PlayerContractInfo(
                PlayerLiteracy.valueOf(Utf8String.read(buffer, 32767)),
                attributes,
                Contract.fromBuffer(buffer)
        );
    }
}
