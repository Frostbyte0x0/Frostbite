package org.exodusstudio.frostbite.common.contracts;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.Utf8String;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.common.item.contract.ContractFragmentItem;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;

public class ContractAttribute {
    public final String id;
    private final ContractRank rank;
    private final Polarity polarity;
    private final ContractTarget target;

    public ContractAttribute(String id, ContractRank rank, Polarity polarity, ContractTarget target) {
        this.id = id;
        this.rank = rank;
        this.polarity = polarity;
        this.target = target;
    }

    public Component getDisplayInfo(PlayerLiteracy literacy) {
        return Component.literal(getPolarity() == Polarity.POSITIVE ? " + " : " - ")
                .append(Component.translatable("contract.attribute." + id))
                .withStyle(getPolarity() == Polarity.POSITIVE ? ChatFormatting.GREEN : ChatFormatting.RED);
    }

    public ContractRank getRank() {
        return rank;
    }

    public Polarity getPolarity() {
        return polarity;
    }

    public ContractTarget getTarget() {
        return target;
    }

    @SuppressWarnings("DataFlowIssue")
    public static ContractAttribute getAttribute(ItemStack stack) {
        ContractAttribute a;
        if (stack.getItem() instanceof ContractFragmentItem && (a = stack.get(DataComponentTypeRegistry.CONTRACT_ATTRIBUTE).attribute()) != null) {
            return a;
        }
        return null;
    }

    public static final Codec<ContractAttribute> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("id").forGetter(attribute -> attribute.id),
            ContractRank.CODEC.fieldOf("rank").forGetter(attribute -> attribute.rank),
            Polarity.CODEC.fieldOf("polarity").forGetter(attribute -> attribute.polarity),
            ContractTarget.CODEC.fieldOf("target").forGetter(attribute -> attribute.target)
    ).apply(instance, ContractAttribute::new));

    public static void toBuffer(final ByteBuf buffer, ContractAttribute attribute) {
        Utf8String.write(buffer, attribute.id, 32767);
        Utf8String.write(buffer, attribute.getRank().name(), 32767);
        Utf8String.write(buffer, attribute.getPolarity().name(), 32767);
        Utf8String.write(buffer, attribute.getTarget().name(), 32767);
    }

    public static ContractAttribute fromBuffer(ByteBuf buffer) {
        return new ContractAttribute(
                Utf8String.read(buffer, 32767),
                ContractRank.valueOf(Utf8String.read(buffer, 32767)),
                Polarity.valueOf(Utf8String.read(buffer, 32767)),
                ContractTarget.valueOf(Utf8String.read(buffer, 32767))
        );
    }
}
