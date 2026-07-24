package org.exodusstudio.frostbite.common.contracts;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.Utf8String;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.common.item.contract.ContractFragmentItem;
import org.exodusstudio.frostbite.common.registry.AttachmentRegistry;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.exodusstudio.frostbite.common.contracts.Literacy.*;

public class ScalableContractAttribute extends ContractAttribute {
    private final List<Float> stats;
    private final int start;
    private final AttributeTemplateInfo templateInfo;

    public ScalableContractAttribute(
            String id,
            List<Float> stats,
            int start,
            Polarity polarity,
            ContractTarget target,
            AttributeTemplateInfo... templateInfo
    ) {
        super(id, null, polarity, target);
        this.stats = stats;
        this.start = start;
        if (templateInfo.length > 0) {
            this.templateInfo = templateInfo[0];
        } else {
            this.templateInfo = null;
        }
    }

    public Component getExtraInfo(Player player, Either<ItemStack, Contract> stack) {
        LivingContractInfo info = player.getData(AttachmentRegistry.LIVING_CONTRACT_INFO);
        Literacy r = LivingContractInfo.hasDiscoveredAttribute(player, this) || player.isCreative() ? LITERATE : info.literacyRank();
        MutableComponent c = Component.literal("    ");
        if (r.ordinal() == PROFICIENT.ordinal()) {
            c.append(Component.translatable("contract.attribute." + id + "_desc"));
        } else if (r.ordinal() == LITERATE.ordinal()) {
            if (stack.left().isPresent() && stack.left().get().has(DataComponentTypeRegistry.SCALABLE_CONTRACT_ATTRIBUTE)) {
                c.append(Component.translatable("contract.attribute." + id + "_desc_complete", ("" + getStat(stack.left().get(), this)).replace(".0", ""), "%"));
            } else if (stack.right().isPresent()) {
                c.append(Component.translatable("contract.attribute." + id + "_desc_complete", ("" + getStat(stack.right().get(), this)).replace(".0", ""), "%"));
            }
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
                .append(r.ordinal() >= BASIC.ordinal() ? Component.literal(" " + getNumeral(stack)) : Component.literal(" §ka"))
                .append(" (")
                .append(r.ordinal() >= BASIC.ordinal() ? Component.translatable("contract.target." + getTarget().name().toLowerCase()) : Component.literal("§kaaaa"))
                .append(")")
                .withStyle(getPolarity() == Polarity.POSITIVE ? ChatFormatting.GREEN : ChatFormatting.RED);
    }

    public int getStart() {
        return start;
    }

    @SuppressWarnings("DataFlowIssue")
    public static float getStat(ItemStack stack, ScalableContractAttribute scalableContractAttribute) {
        if (stack.has(DataComponentTypeRegistry.SCALABLE_CONTRACT_ATTRIBUTE)) {
            ScalableContractAttribute attribute = stack.get(DataComponentTypeRegistry.SCALABLE_CONTRACT_ATTRIBUTE).attribute();
            int level = getLevel(stack, attribute);
            if (level > 0 && level <= attribute.stats.size() && attribute.equals(scalableContractAttribute)) {
                return attribute.stats.get(level - 1);
            }
        } else if (stack.has(DataComponentTypeRegistry.CONTRACT)) {
            Contract contract = stack.get(DataComponentTypeRegistry.CONTRACT).contract();
            if (contract == null || !contract.allScalableAttributes().containsKey(scalableContractAttribute)) return 0;
            return getStat(contract, scalableContractAttribute);
        }

        return 0;
    }

    public static float getStat(Contract c, ScalableContractAttribute scalableContractAttribute) {
        return scalableContractAttribute.stats.get(c.allScalableAttributes().get(scalableContractAttribute) - 1);
    }

    public String getNumeral(Either<ItemStack, Contract> stack) {
        return switch(getLevel(stack, this)) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            default -> "";
        };
    }

    public static int getLevel(Either<ItemStack, Contract> ic, ScalableContractAttribute scalableContractAttribute) {
        if (ic.left().isPresent()) {
            ItemStack stack = ic.left().get();
            return getLevel(stack, scalableContractAttribute);
        } else if (ic.right().isPresent()) {
            Contract c = ic.right().get();
            return getLevel(c, scalableContractAttribute);
        }
        return 0;
    }

    @SuppressWarnings("DataFlowIssue")
    public static int getLevel(ItemStack stack, ScalableContractAttribute attribute) {
        if (stack.has(DataComponentTypeRegistry.SCALABLE_CONTRACT_ATTRIBUTE) && stack.get(DataComponentTypeRegistry.SCALABLE_CONTRACT_ATTRIBUTE).attribute().equals(attribute)) {
            return stack.get(DataComponentTypeRegistry.SCALABLE_CONTRACT_ATTRIBUTE).level();
        }
        if (stack.has(DataComponentTypeRegistry.CONTRACT) && stack.get(DataComponentTypeRegistry.CONTRACT).contract() != null) {
            return stack.get(DataComponentTypeRegistry.CONTRACT).contract().allScalableAttributes().getOrDefault(attribute, 1);
        }
        return 0;
    }

    public static int getLevel(Contract contract, ScalableContractAttribute attribute) {
        if (contract == null) return 0;
        return contract.allScalableAttributes().getOrDefault(attribute, 1);
    }

    @SuppressWarnings("DataFlowIssue")
    public static ScalableContractAttribute getAttribute(ItemStack stack) {
        ScalableContractAttribute a;
        if (stack.getItem() instanceof ContractFragmentItem && stack.has(DataComponentTypeRegistry.SCALABLE_CONTRACT_ATTRIBUTE) && (a = stack.get(DataComponentTypeRegistry.SCALABLE_CONTRACT_ATTRIBUTE).attribute()) != null) {
            return a;
        }
        return null;
    }

    public MobEffect.AttributeTemplate getAttributeTemplate(int level) {
        return templateInfo.attributeTemplates.getOrDefault(level, null);
    }

    public Holder<Attribute> getAttribute() {
        return templateInfo.attribute;
    }

    public boolean hasAttribute() {
        return templateInfo != null;
    }

    public static final Codec<ScalableContractAttribute> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("id").forGetter(attribute -> attribute.id),
            Codec.FLOAT.listOf().fieldOf("stats").forGetter(attribute -> attribute.stats),
            Codec.INT.fieldOf("start").forGetter(attribute -> attribute.start),
            Polarity.CODEC.fieldOf("polarity").forGetter(ScalableContractAttribute::getPolarity),
            ContractTarget.CODEC.fieldOf("target").forGetter(ScalableContractAttribute::getTarget)
    ).apply(instance, ScalableContractAttribute::new));

    public static void toBuffer(final ByteBuf buffer, ScalableContractAttribute attribute) {
        buffer.writeInt(attribute.stats.size());
        attribute.stats.forEach(buffer::writeFloat);

        Utf8String.write(buffer, attribute.id, 32767);
        buffer.writeInt(attribute.start);
        Utf8String.write(buffer, attribute.getPolarity().name(), 32767);
        Utf8String.write(buffer, attribute.getTarget().name(), 32767);
    }

    public static ScalableContractAttribute fromBuffer(ByteBuf buffer) {
        int statsSize = buffer.readInt();
        List<Float> stats = new ArrayList<>();
        for (int i = 0; i < statsSize; i++) {
            stats.add(buffer.readFloat());
        }

        return new ScalableContractAttribute(
                Utf8String.read(buffer, 32767),
                stats,
                buffer.readInt(),
                Polarity.valueOf(Utf8String.read(buffer, 32767)),
                ContractTarget.valueOf(Utf8String.read(buffer, 32767))
        );
    }

    public record AttributeTemplateInfo(
            Holder<Attribute> attribute,
            Map<Integer, MobEffect.AttributeTemplate> attributeTemplates
    ) {}
}
