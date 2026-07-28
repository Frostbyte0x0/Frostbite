package org.exodusstudio.frostbite.common.contracts;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.Utf8String;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.common.item.contract.ContractFragmentItem;
import org.exodusstudio.frostbite.common.registry.AttachmentRegistry;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.util.helpers.DataHelper;

import java.util.*;
import java.util.stream.Collectors;

import static org.exodusstudio.frostbite.common.contracts.Literacy.*;

public class ContractAttribute {
    public final String id;
    private final Optional<ContractRank> rank;
    private final Polarity polarity;
    private final ContractTarget target;
    private final boolean isScalable;
    private final List<Float> stats;
    private final int start;
    private final Optional<AttributeTemplateInfo> templateInfo;

    private ContractAttribute(
            String id,
            Optional<ContractRank> rank,
            Polarity polarity,
            ContractTarget target,
            boolean isScalable,
            List<Float> stats,
            int start,
            Optional<AttributeTemplateInfo> templateInfo
    ) {
        this.id = id;
        this.rank = rank;
        this.polarity = polarity;
        this.target = target;
        this.isScalable = isScalable;
        this.stats = stats;
        this.start = start;
        this.templateInfo = templateInfo;
    }

    public Component getExtraInfo(Player player, Either<ItemStack, Contract> stack, boolean withSpace) {
        if (isScalable) {
            return getExtraInfoScalable(player, stack, withSpace);
        } else {
            return getExtraInfoFixed(player, withSpace);
        }
    }

    public Component getExtraInfoFixed(Player player, boolean withSpace) {
        LivingContractInfo info = player.getData(AttachmentRegistry.LIVING_CONTRACT_INFO);
        Literacy r = LivingContractInfo.hasDiscoveredAttribute(player, this) || player.isCreative() ? LITERATE : info.literacyRank();
        MutableComponent c = Component.literal(withSpace ? "    " : "");
        if (r.ordinal() == PROFICIENT.ordinal()) {
            c.append(Component.translatable("contract.attribute." + id + "_desc"));
        } else if (r.ordinal() == LITERATE.ordinal()) {
            c.append(getCompleteDesc());
        } else {
            c.append(Component.literal("§kaaaa"));
        }
        return c.withStyle(getPolarity() == Polarity.POSITIVE ? ChatFormatting.DARK_GREEN : ChatFormatting.DARK_RED);
    }

    public Component getExtraInfoScalable(Player player, Either<ItemStack, Contract> stack, boolean withSpace) {
        LivingContractInfo info = player.getData(AttachmentRegistry.LIVING_CONTRACT_INFO);
        Literacy r = LivingContractInfo.hasDiscoveredAttribute(player, this) || player.isCreative() ? LITERATE : info.literacyRank();
        MutableComponent c = Component.literal(withSpace ? "    " : "");
        if (r.ordinal() == PROFICIENT.ordinal()) {
            c.append(Component.translatable("contract.attribute." + id + "_desc"));
        } else if (r.ordinal() == LITERATE.ordinal()) {
            if (stack.left().isPresent() && (stack.left().get().has(DataComponentTypeRegistry.CONTRACT_ATTRIBUTE) || stack.left().get().has(DataComponentTypeRegistry.CONTRACT))) {
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

        MutableComponent c = Component.literal(getPolarity() == Polarity.POSITIVE ? " + " : " - ")
                .append(r.ordinal() >= BASIC.ordinal() ? Component.translatable("contract.attribute." + id) : Component.literal("§kaaaa"));

        if (isScalable) c.append(r.ordinal() >= BASIC.ordinal() ? Component.literal(" " + getNumeral(stack)) : Component.literal(" §ka"));

        return c.append(" (")
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

    public int getStart() {
        return start;
    }

    @SuppressWarnings("DataFlowIssue")
    public static float getStat(ItemStack stack, ContractAttribute att) {
        if (!att.isScalable()) return 0;

        if (stack.has(DataComponentTypeRegistry.CONTRACT_ATTRIBUTE)) {
            ContractAttribute attribute = stack.get(DataComponentTypeRegistry.CONTRACT_ATTRIBUTE).attribute();
            int level = getLevel(stack, attribute);
            if (level > 0 && level <= attribute.stats.size() && attribute.equals(att)) {
                return attribute.stats.get(level - 1);
            }
        } else if (stack.has(DataComponentTypeRegistry.CONTRACT)) {
            Contract contract = stack.get(DataComponentTypeRegistry.CONTRACT).contract();
            if (contract == null || !contract.allScalableAttributes().containsKey(att)) return 0;
            return getStat(contract, att);
        }

        return 0;
    }

    public static float getStat(Contract c, ContractAttribute attribute) {
        return attribute.stats.get(c.allScalableAttributes().get(attribute) - 1);
    }

    public List<Float> getStats() {
        return stats;
    }

    public String getNumeral(Either<ItemStack, Contract> stack) {
        return switch(getLevel(stack, this)) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            default -> "";
        };
    }

    public static int getLevel(Either<ItemStack, Contract> ic, ContractAttribute attribute) {
        if (ic.left().isPresent()) {
            ItemStack stack = ic.left().get();
            return getLevel(stack, attribute);
        } else if (ic.right().isPresent()) {
            Contract c = ic.right().get();
            return getLevel(c, attribute);
        }
        return 1;
    }

    @SuppressWarnings("DataFlowIssue")
    public static int getLevel(ItemStack stack, ContractAttribute attribute) {
        if (stack.has(DataComponentTypeRegistry.CONTRACT_ATTRIBUTE) && stack.get(DataComponentTypeRegistry.CONTRACT_ATTRIBUTE).attribute().equals(attribute)) {
            return DataHelper.getInt(stack, "level");
        }
        if (stack.has(DataComponentTypeRegistry.CONTRACT) && stack.get(DataComponentTypeRegistry.CONTRACT).contract() != null) {
            return stack.get(DataComponentTypeRegistry.CONTRACT).contract().allScalableAttributes().getOrDefault(attribute, 1);
        }
        return 1;
    }

    public static int getLevel(Contract contract, ContractAttribute attribute) {
        if (contract == null) return 1;
        return contract.allScalableAttributes().getOrDefault(attribute, 1);
    }

    public MobEffect.AttributeTemplate getAttributeTemplate(int level) {
        return templateInfo.map(info -> info.getAttributeTemplates().getOrDefault(level, null)).orElse(null);
    }

    public Holder<Attribute> getAIAttribute() {
        return templateInfo.map(AttributeTemplateInfo::getAttribute).orElse(null);
    }

    public boolean hasAttribute() {
        return templateInfo.isPresent();
    }

    public ContractRank getRank() {
        return rank.orElseThrow(() -> new IllegalStateException("ContractAttribute " + id + " does not have a rank (prob not scalable)"));
    }

    public Polarity getPolarity() {
        return polarity;
    }

    public ContractTarget getTarget() {
        return target;
    }

    public boolean isScalable() {
        return isScalable;
    }

    @SuppressWarnings("DataFlowIssue")
    public static ContractAttribute getAttribute(ItemStack stack) {
        ContractAttribute a;
        if (stack.getItem() instanceof ContractFragmentItem && stack.has(DataComponentTypeRegistry.CONTRACT_ATTRIBUTE) && (a = stack.get(DataComponentTypeRegistry.CONTRACT_ATTRIBUTE).attribute()) != null) {
            return a;
        }
        return null;
    }

    public static void addAttributeModifiers(AttributeMap map, Map<ContractAttribute, Integer> attributes) {
        for (Map.Entry<ContractAttribute, Integer> entry : attributes.entrySet()) {
            ContractAttribute attribute = entry.getKey();
            if (!attribute.hasAttribute()) continue;

            int level = entry.getValue();
            AttributeInstance attr = map.getInstance(attribute.getAIAttribute());
            if (attr != null) {
                attr.addPermanentModifier(attribute.getAttributeTemplate(level).create(1));
            }
        }
    }

    public static void removeAttributeModifiers(AttributeMap map, Map<ContractAttribute, Integer> attributes) {
        for (Map.Entry<ContractAttribute, Integer> entry : attributes.entrySet()) {
            ContractAttribute attribute = entry.getKey();
            if (!attribute.hasAttribute()) continue;

            int level = entry.getValue();
            AttributeInstance attr = map.getInstance(attribute.getAIAttribute());
            if (attr != null) {
                attr.removeModifier(attribute.getAttributeTemplate(level).id());
            }
        }
    }

    public static final Codec<ContractAttribute> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("id").forGetter(attribute -> attribute.id),
            ContractRank.CODEC.optionalFieldOf("rank").forGetter(attribute -> attribute.rank),
            Polarity.CODEC.fieldOf("polarity").forGetter(attribute -> attribute.polarity),
            ContractTarget.CODEC.fieldOf("target").forGetter(attribute -> attribute.target),
            Codec.BOOL.fieldOf("is_scalable").forGetter(attribute -> attribute.isScalable),
            Codec.FLOAT.listOf().fieldOf("stats").forGetter(attribute -> attribute.stats),
            Codec.INT.fieldOf("start").forGetter(attribute -> attribute.start),
            AttributeTemplateInfo.CODEC.optionalFieldOf("template_info").forGetter(attribute -> attribute.templateInfo)
    ).apply(instance, ContractAttribute::new));

    public static void toBuffer(final ByteBuf buffer, ContractAttribute attribute) {
        buffer.writeInt(attribute.stats.size());
        attribute.stats.forEach(buffer::writeFloat);

        if (attribute.templateInfo.isPresent()) {
            buffer.writeBoolean(true);
            AttributeTemplateInfo.toBuffer(buffer, attribute.templateInfo.get());
        } else {
            buffer.writeBoolean(false);
        }

        if (attribute.rank.isPresent()) {
            buffer.writeBoolean(true);
            Utf8String.write(buffer, attribute.rank.get().name(), 32767);
        } else {
            buffer.writeBoolean(false);
        }

        Utf8String.write(buffer, attribute.id, 32767);
        Utf8String.write(buffer, attribute.getPolarity().name(), 32767);
        Utf8String.write(buffer, attribute.getTarget().name(), 32767);
        buffer.writeBoolean(attribute.isScalable);
        buffer.writeInt(attribute.start);
    }

    public static ContractAttribute fromBuffer(ByteBuf buffer) {
        int statsSize = buffer.readInt();
        List<Float> stats = new ArrayList<>();
        for (int i = 0; i < statsSize; i++) {
            stats.add(buffer.readFloat());
        }

        Optional<AttributeTemplateInfo> templateInfo = Optional.empty();
        if (buffer.readBoolean()) {
            templateInfo = Optional.of(AttributeTemplateInfo.fromBuffer(buffer));
        }

        Optional<ContractRank> rank = Optional.empty();
        if (buffer.readBoolean()) {
            rank = Optional.of(ContractRank.valueOf(Utf8String.read(buffer, 32767).toUpperCase()));
        }

        ContractAttribute a = new ContractAttribute(
                Utf8String.read(buffer, 32767),
                rank,
                Polarity.valueOf(Utf8String.read(buffer, 32767).toUpperCase()),
                ContractTarget.valueOf(Utf8String.read(buffer, 32767).toUpperCase()),
                buffer.readBoolean(),
                stats,
                buffer.readInt(),
                templateInfo
        );

        return a;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ContractAttribute attribute && attribute.id.equals(this.id);
    }


    public record AttributeTemplateInfo(
            String attribute,
            Map<Integer, String> attributeTemplates
    ) {
        public static final Codec<MobEffect.AttributeTemplate> ATTRIBUTE_TEMPLATE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("id").forGetter(MobEffect.AttributeTemplate::id),
                Codec.DOUBLE.fieldOf("amount").forGetter(MobEffect.AttributeTemplate::amount),
                AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(MobEffect.AttributeTemplate::operation)
        ).apply(instance, MobEffect.AttributeTemplate::new));

        public static final Codec<AttributeTemplateInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("ai_attribute").forGetter(AttributeTemplateInfo::attribute),
                Codec.unboundedMap(Codec.INT, Codec.STRING).fieldOf("attribute_templates").forGetter(AttributeTemplateInfo::attributeTemplates)
        ).apply(instance, AttributeTemplateInfo::new));

        public static AttributeTemplateInfo create(Holder<Attribute> attribute, Map<Integer, MobEffect.AttributeTemplate> attributeTemplates) {
            return new AttributeTemplateInfo(BuiltInRegistries.ATTRIBUTE.getKey(attribute.value()).toString(), attributeTemplates.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue().id() + ";" + v.getValue().amount() + ";" + v.getValue().operation().name())));
        }

        public Map<Integer, MobEffect.AttributeTemplate> getAttributeTemplates() {
            Map<Integer, MobEffect.AttributeTemplate> templates = new HashMap<>();
            for (Map.Entry<Integer, String> entry : attributeTemplates.entrySet()) {
                String[] parts = entry.getValue().split(";");
                Identifier id = Identifier.parse(parts[0]);
                double amount = Double.parseDouble(parts[1]);
                AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(parts[2].toUpperCase());
                MobEffect.AttributeTemplate template = new MobEffect.AttributeTemplate(id, amount, operation);
                templates.put(entry.getKey(), template);
            }
            return templates;
        }

        public Holder<Attribute> getAttribute() {
            return BuiltInRegistries.ATTRIBUTE.get(Identifier.parse(attribute)).orElse(null);
        }

        public static void toBuffer(final ByteBuf buffer, AttributeTemplateInfo info) {
            Utf8String.write(buffer, info.attribute, 32767);
            buffer.writeInt(info.attributeTemplates().size());
            for (Map.Entry<Integer, String> entry : info.attributeTemplates().entrySet()) {
                buffer.writeInt(entry.getKey());
                Utf8String.write(buffer, entry.getValue(), 32767);
            }
        }

        public static AttributeTemplateInfo fromBuffer(ByteBuf buffer) {
            String attribute = Utf8String.read(buffer, 32767);
            int size = buffer.readInt();
            Map<Integer, String> attributeTemplates = new HashMap<>();
            for (int i = 0; i < size; i++) {
                int level = buffer.readInt();
                String parts = Utf8String.read(buffer, 32767);
                attributeTemplates.put(level, parts);
            }
            return new AttributeTemplateInfo(attribute, attributeTemplates);
        }
    }

    public static class Builder {
        private final String id;
        private final Polarity polarity;
        private final ContractTarget target;
        private Optional<ContractRank> rank = Optional.empty();
        private boolean isScalable = false;
        private List<Float> stats = List.of();
        private int start = 0;
        private Optional<AttributeTemplateInfo> templateInfo = Optional.empty();

        public Builder(String id, Polarity polarity, ContractTarget target) {
            this.id = id;
            this.polarity = polarity;
            this.target = target;
        }

        public ContractAttribute build() {
            return new ContractAttribute(id, rank, polarity, target, isScalable, stats, start, templateInfo);
        }

        public Builder rank(ContractRank rank) {
            this.rank = Optional.of(rank);
            return this;
        }

        public Builder scalable(List<Float> stats) {
            this.isScalable = true;
            this.stats = stats;
            return this;
        }

        public Builder start(int start) {
            this.start = start;
            return this;
        }

        public Builder templateInfo(Holder<Attribute> attribute, Map<Integer, MobEffect.AttributeTemplate> attributeTemplates) {
            this.templateInfo = Optional.of(AttributeTemplateInfo.create(attribute, attributeTemplates));
            return this;
        }
    }
}
