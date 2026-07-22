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
    List<String> discoveredAttributes,
    Contract contract
) {
    public static final Codec<PlayerContractInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                PlayerLiteracy.CODEC.fieldOf("contract_attribute").forGetter(PlayerContractInfo::literacyRank),
                Codec.STRING.listOf().fieldOf("discovered_attributes").forGetter(PlayerContractInfo::discoveredAttributes),
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
        List<String> newAttributes = new ArrayList<>(old.discoveredAttributes());
        newAttributes.add(attribute.id);

        PlayerLiteracy literacy = old.literacyRank();
        if (literacy.hasNext() && newAttributes.size() >= literacy.next().discoveredNb) {
            literacy = literacy.next();
        }

        player.setData(AttachmentRegistry.PLAYER_CONTRACT_INFO.get(), new PlayerContractInfo(
                literacy,
                newAttributes,
                old.contract()
        ));
    }

    public static boolean hasDiscoveredAttribute(Player player, ContractAttribute attribute) {
        PlayerContractInfo info = player.getData(AttachmentRegistry.PLAYER_CONTRACT_INFO);
        return info.discoveredAttributes().stream().anyMatch(a -> a.equals(attribute.id));
    }

    public static void setContract(Player player, Contract contract) {
        PlayerContractInfo old = player.getData(AttachmentRegistry.PLAYER_CONTRACT_INFO);
        player.setData(AttachmentRegistry.PLAYER_CONTRACT_INFO.get(), new PlayerContractInfo(
                old.literacyRank(),
                old.discoveredAttributes(),
                contract
        ));
    }

    public static Contract getContract(Player player) {
        PlayerContractInfo info = player.getData(AttachmentRegistry.PLAYER_CONTRACT_INFO);
        return info.contract();
    }

    public static void toBuffer(final ByteBuf buffer, PlayerContractInfo contract) {
        buffer.writeInt(contract.discoveredAttributes().size());
        for (String attribute : contract.discoveredAttributes()) {
            Utf8String.write(buffer, attribute, 32767);
        }
        Utf8String.write(buffer, contract.literacyRank().toString(), 32767);
        Contract.toBuffer(buffer, contract.contract());
    }

    public static PlayerContractInfo fromBuffer(ByteBuf buffer) {
        int size = buffer.readInt();
        List<String> attributes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            attributes.add(Utf8String.read(buffer, 32767));
        }
        return new PlayerContractInfo(
                PlayerLiteracy.valueOf(Utf8String.read(buffer, 32767)),
                attributes,
                Contract.fromBuffer(buffer)
        );
    }
}
