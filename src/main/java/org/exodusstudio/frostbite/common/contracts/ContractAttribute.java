package org.exodusstudio.frostbite.common.contracts;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.Utf8String;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.common.item.contract.ContractFragmentItem;
import org.exodusstudio.frostbite.common.registry.AttachmentRegistry;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;

import java.util.Map;

import static org.exodusstudio.frostbite.common.contracts.Literacy.*;

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

    public Component getExtraInfo(Player player, Either<ItemStack, Contract> stack) {
        LivingContractInfo info = player.getData(AttachmentRegistry.LIVING_CONTRACT_INFO);
        Literacy r = LivingContractInfo.hasDiscoveredAttribute(player, this) || player.isCreative() ? LITERATE : info.literacyRank();
        MutableComponent c = Component.literal("    ");
        if (r.ordinal() == PROFICIENT.ordinal()) {
            c.append(Component.translatable("contract.attribute." + id + "_desc"));
        } else if (r.ordinal() == LITERATE.ordinal()) {
            c.append(getCompleteDesc());
        } else {
            c.append(Component.literal("§kaaaa"));
        }
        return c.withStyle(getPolarity() == Polarity.POSITIVE ? ChatFormatting.DARK_GREEN : ChatFormatting.DARK_RED);
    }

    public Component getSmallInfo(Player player, Either<ItemStack, Contract> stack) {
        LivingContractInfo info = player.getData(AttachmentRegistry.LIVING_CONTRACT_INFO);
        Literacy r = LivingContractInfo.hasDiscoveredAttribute(player, this) || player.isCreative() ? LITERATE : info.literacyRank();

        return Component.literal(getPolarity() == Polarity.POSITIVE ? " + " : " - ")
                .append(r.ordinal() >= BASIC.ordinal() ? Component.translatable("contract.attribute." + id) : Component.literal("§kaaaa"))
                .append(" (")
                .append(r.ordinal() >= BASIC.ordinal() ? Component.translatable("contract.target." + target.name().toLowerCase()) : Component.literal("§kaaaa"))
                .append(")")
                .withStyle(getPolarity() == Polarity.POSITIVE ? ChatFormatting.GREEN : ChatFormatting.RED);
    }

    public Component getCompleteDesc() {
        if (Component.translatable("contract.attribute." + id + "_desc_complete").getString().equals(Component.literal("contract.attribute." + id + "_desc_complete").getString())) {
            return Component.translatable("contract.attribute." + id + "_desc");
        }
        return Component.translatable("contract.attribute." + id + "_desc_complete");
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

    public boolean isScalable() {
        return this instanceof ScalableContractAttribute;
    }

    @SuppressWarnings("DataFlowIssue")
    public static ContractAttribute getAttribute(ItemStack stack) {
        ContractAttribute a;
        if (stack.getItem() instanceof ContractFragmentItem && stack.has(DataComponentTypeRegistry.CONTRACT_ATTRIBUTE) && (a = stack.get(DataComponentTypeRegistry.CONTRACT_ATTRIBUTE).attribute()) != null) {
            return a;
        }
        return ScalableContractAttribute.getAttribute(stack);
    }

    public static void addAttributeModifiers(AttributeMap map, Map<ScalableContractAttribute, Integer> attributes) {
        for (Map.Entry<ScalableContractAttribute, Integer> entry : attributes.entrySet()) {
            ScalableContractAttribute attribute = entry.getKey();
            if (!attribute.hasAttribute()) continue;

            int level = entry.getValue();
            AttributeInstance attr = map.getInstance(attribute.getAttribute());
            if (attr != null) {
                attr.addPermanentModifier(attribute.getAttributeTemplate(level).create(1));
            }
        }
    }

    public static void removeAttributeModifiers(AttributeMap map, Map<ScalableContractAttribute, Integer> attributes) {
        for (Map.Entry<ScalableContractAttribute, Integer> entry : attributes.entrySet()) {
            ScalableContractAttribute attribute = entry.getKey();
            if (!attribute.hasAttribute()) continue;

            int level = entry.getValue();
            AttributeInstance attr = map.getInstance(attribute.getAttribute());
            if (attr != null) {
                attr.removeModifier(attribute.getAttributeTemplate(level).id());
            }
        }
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

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ContractAttribute attribute && attribute.id.equals(this.id);
    }
}
