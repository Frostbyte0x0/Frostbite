package org.exodusstudio.frostbite.common.contracts;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.Utf8String;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class ScalableContractAttribute extends ContractAttribute {
    private final List<Float> stats;

    public ScalableContractAttribute(String id, List<Float> stats, Polarity polarity, ContractTarget target) {
        super(id, null, polarity, target);
        this.stats = stats;
    }

    public Component getDisplayInfo(PlayerLiteracy literacy) {
        return Component.literal(getPolarity() == Polarity.POSITIVE ? " + " : " - ")
                .append(Component.translatable("contract.attribute." + id))
                .withStyle(getPolarity() == Polarity.POSITIVE ? ChatFormatting.GREEN : ChatFormatting.RED)
                .append(getNumeral());
    }

    public float getStat() {
        return stats.get(getRankNb() - 1);
    }

    public String getNumeral() {
        return switch(getRankNb()) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            default -> "";
        };
    }

    public int getRankNb() {
        char lastChar = id.charAt(id.length() - 1);
        try {
            return Integer.parseInt(String.valueOf(lastChar));
        } catch (NumberFormatException _) {}
        return -1;
    }

    public ContractRank getRank() {
        if (getRankNb() != -1) {
            return ContractRank.fromNum(getRankNb());
        }
        return super.getRank();
    }

    public static final Codec<ScalableContractAttribute> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("id").forGetter(attribute -> attribute.id),
            Codec.FLOAT.listOf().fieldOf("stats").forGetter(attribute -> attribute.stats),
            Polarity.CODEC.fieldOf("polarity").forGetter(ScalableContractAttribute::getPolarity),
            ContractTarget.CODEC.fieldOf("target").forGetter(ScalableContractAttribute::getTarget)
    ).apply(instance, ScalableContractAttribute::new));

    public static void toBuffer(final ByteBuf buffer, ScalableContractAttribute attribute) {
        buffer.writeInt(attribute.stats.size());
        for (Float stat : attribute.stats) {
            buffer.writeFloat(stat);
        }
        Utf8String.write(buffer, attribute.id, 32767);
        Utf8String.write(buffer, attribute.getPolarity().name(), 32767);
        Utf8String.write(buffer, attribute.getTarget().name(), 32767);
    }

    public static ScalableContractAttribute fromBuffer(ByteBuf buffer) {
        int statCount = buffer.readInt();
        List<Float> stats = new ArrayList<>();
        for (int i = 0; i < statCount; i++) {
            stats.add(buffer.readFloat());
        }

        return new ScalableContractAttribute(
                Utf8String.read(buffer, 32767),
                stats,
                Polarity.valueOf(Utf8String.read(buffer, 32767)),
                ContractTarget.valueOf(Utf8String.read(buffer, 32767))
        );
    }
}
