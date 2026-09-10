package org.exodusstudio.frostbite.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.Utf8String;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.item.armour.ArmourCleanliness;
import org.exodusstudio.frostbite.common.item.armour.ArmourSet;
import org.exodusstudio.frostbite.common.registry.AttributeRegistry;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.util.Util;
import org.exodusstudio.frostbite.common.util.helpers.DataHelper;
import oshi.util.tuples.Pair;

import java.util.*;

public record ArmourStatsData(
        AttributesInfo attributes,
        ArmourCleanliness cleanliness
) {
    // Random stats (can't be same as main or secondary stat):
    //  - Lifesteal
    //  - Temperature Steal
    //  - Cold Defence
    //  - Defence
    //  - Speed
    //  - Poison
    //  - Extra jumps
    //  - Spell damage
    //  - Melee damage
    //  - Attack speed
    //
    // Rusty, Regular, Sparkling, levels of cleanliness for stat modifiers
    static final Map<Holder<Attribute>, Pair<Float[], AttributeModifier.Operation>> POSITIVE_STATS = new HashMap<>(){{
        put(AttributeRegistry.LIFE_STEAL, null);
        put(AttributeRegistry.TEMPERATURE_STEAL, null);
//        put(AttributeRegistry.COLD_DEFENCE, null);
        put(AttributeRegistry.DEFENCE, null);
        put(Attributes.MOVEMENT_SPEED, new Pair<>(null, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        put(AttributeRegistry.POISON, new Pair<>(new Float[]{3f, 6f, 9f}, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        put(AttributeRegistry.JUMPS, new Pair<>(new Float[]{1f, 1f, 1f}, AttributeModifier.Operation.ADD_VALUE));
        put(AttributeRegistry.SPELL_DAMAGE, null);
        put(AttributeRegistry.MELEE_DAMAGE, null);
        put(Attributes.ATTACK_SPEED, new Pair<>(null, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }};
    static final Map<Holder<Attribute>, Pair<Float[], AttributeModifier.Operation>> NEGATIVE_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, new Pair<>(new Float[]{-2f, -2f, -2f}, AttributeModifier.Operation.ADD_VALUE));
        put(AttributeRegistry.LIFE_STEAL, null);
        put(AttributeRegistry.TEMPERATURE_STEAL, null);
//        put(AttributeRegistry.COLD_DEFENCE, null);
        put(AttributeRegistry.DEFENCE, null);
        put(Attributes.MOVEMENT_SPEED, new Pair<>(null, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        put(AttributeRegistry.SPELL_DAMAGE, null);
        put(AttributeRegistry.MELEE_DAMAGE, null);
        put(Attributes.ATTACK_SPEED, new Pair<>(null, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }};

    public static final Codec<ArmourStatsData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            AttributesInfo.CODEC.fieldOf("attributes").forGetter(ArmourStatsData::attributes),
            ArmourCleanliness.CODEC.fieldOf("cleanliness").forGetter(ArmourStatsData::cleanliness)
    ).apply(instance, ArmourStatsData::new));

    public static final StreamCodec<ByteBuf, ArmourStatsData> STREAM_CODEC =
            StreamCodec.of(
                    (b, d) -> {
                        AttributesInfo.toBuffer(b, d.attributes());
                        Utf8String.write(b, d.cleanliness().name(), 32767);
                    },
                    b -> new ArmourStatsData(AttributesInfo.fromBuffer(b), ArmourCleanliness.valueOf(Utf8String.read(b, 32767).toUpperCase()))
            );

    public Map<Holder<Attribute>, MobEffect.AttributeTemplate> getAttributes() {
        return attributes.getAttributesInfo();
    }

    public static ArmourStatsData addRandomStats(ArmourSet set, ItemStack stack) {
        ArmourCleanliness cleanliness = ArmourCleanliness.random();

        String equipmentName = stack.get(DataComponents.EQUIPPABLE).slot().getName().toLowerCase();

        Map<Holder<Attribute>, MobEffect.AttributeTemplate> attributes = new LinkedHashMap<>();
        Set<Holder<Attribute>> except = new HashSet<>(){{
            addAll(set.attributes().keySet());
        }};

        Map.Entry<Holder<Attribute>, Pair<Float[], AttributeModifier.Operation>> posEntry1 = Util.chooseRandomEntryExcept(POSITIVE_STATS, except);
        attributes.put(posEntry1.getKey(), template(posEntry1.getValue(), cleanliness.ordinal(), equipmentName, false));
        except.add(posEntry1.getKey());

        switch (cleanliness) {
            case RUSTY -> {
                Map.Entry<Holder<Attribute>, Pair<Float[], AttributeModifier.Operation>> negativeEntry = Util.chooseRandomEntryExcept(NEGATIVE_STATS, except);
                attributes.put(negativeEntry.getKey(), template(negativeEntry.getValue(), 0, equipmentName, true));
            }
            case REGULAR -> {
                Map.Entry<Holder<Attribute>, Pair<Float[], AttributeModifier.Operation>> posEntry2 = Util.chooseRandomEntryExcept(POSITIVE_STATS, except);
                attributes.put(posEntry2.getKey(), template(posEntry2.getValue(), 1, equipmentName, false));
                Map.Entry<Holder<Attribute>, Pair<Float[], AttributeModifier.Operation>> negativeEntry = Util.chooseRandomEntryExcept(NEGATIVE_STATS, except);
                attributes.put(negativeEntry.getKey(), template(negativeEntry.getValue(), 1, equipmentName, true));
            }
            case SPARKLING -> {
                Map.Entry<Holder<Attribute>, Pair<Float[], AttributeModifier.Operation>> posEntry2 = Util.chooseRandomEntryExcept(POSITIVE_STATS, except);
                attributes.put(posEntry2.getKey(), template(posEntry2.getValue(), 2, equipmentName, false));
                except.add(posEntry2.getKey());
                Map.Entry<Holder<Attribute>, Pair<Float[], AttributeModifier.Operation>> posEntry3 = Util.chooseRandomEntryExcept(POSITIVE_STATS, except);
                attributes.put(posEntry3.getKey(), template(posEntry3.getValue(), 2, equipmentName, false));
            }
        }

        ArmourStatsData data = new ArmourStatsData(AttributesInfo.create(attributes), cleanliness);
        stack.set(DataComponentTypeRegistry.ARMOUR_STATS, data);
        return data;
    }

    public static MobEffect.AttributeTemplate template(Pair<Float[], AttributeModifier.Operation> info, int ordinal, String equipmentName, boolean n) {
        float amount = info != null && info.getA() != null ? info.getA()[ordinal] : (ordinal + 1) * 3 / 100f * (n ? -1 : 1);
        AttributeModifier.Operation operation = info != null && info.getB() != null ? info.getB() : AttributeModifier.Operation.ADD_VALUE;
        return new MobEffect.AttributeTemplate(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "armour_stats_" + equipmentName),
                amount, operation);
    }

    public static void addStatBonuses(LivingEntity entity, String equipmentName, ArmourStatsData stats) {
        for (Map.Entry<Holder<Attribute>, MobEffect.AttributeTemplate> entry : stats.getAttributes().entrySet()) {
            Holder<Attribute> attribute = entry.getKey();

            AttributeInstance attr = entity.getAttribute(attribute);
            if (attr != null) {
                try {
                    attr.addPermanentModifier(entry.getValue().create(0));
                    DataHelper.setData(entity, "armour_" + equipmentName + "_stats",
                            DataHelper.getString(entity, "armour_" + equipmentName + "_stats") +
                                    BuiltInRegistries.ATTRIBUTE.getKey(entry.getKey().value()).toString() + "#" + entry.getValue().id() + ";");
                } catch (IllegalArgumentException _) {}
            }
        }
    }

    public static void tryRemoveStatBonuses(LivingEntity entity, String equipmentName) {
        String s = DataHelper.getString(entity, "armour_" + equipmentName + "_stats");
        if (s.isEmpty()) return;

        for (String info : s.split(";")) {
            String[] parts = info.split("#");
            Identifier att = Identifier.parse(parts[0]);
            Holder<Attribute> attribute = BuiltInRegistries.ATTRIBUTE.get(att).orElseThrow();

            AttributeInstance attr = entity.getAttributes().getInstance(attribute);
            if (attr != null) {
                Identifier mod = Identifier.parse(parts[1]);
                attr.removeModifier(mod);
            }
        }
    }

    public record AttributesInfo(
            Map<String, String> attributesInfo
    ) {
        public static final Codec<AttributesInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("attributes_info").forGetter(AttributesInfo::attributesInfo)
        ).apply(instance, AttributesInfo::new));

        public static AttributesInfo create(Map<Holder<Attribute>, MobEffect.AttributeTemplate> attributes) {
            Map<String, String> attributeMap = new LinkedHashMap<>();
            for (Map.Entry<Holder<Attribute>, MobEffect.AttributeTemplate> attribute : attributes.entrySet()) {
                MobEffect.AttributeTemplate v = attribute.getValue();
                attributeMap.put(BuiltInRegistries.ATTRIBUTE.getKey(attribute.getKey().value()).toString(),
                        v.id() + ";" + v.amount() + ";" + v.operation().name());
            }

            return new AttributesInfo(attributeMap);
        }

        public Map<Holder<Attribute>, MobEffect.AttributeTemplate> getAttributesInfo() {
            Map<Holder<Attribute>, MobEffect.AttributeTemplate> templates = new LinkedHashMap<>();
            for (Map.Entry<String, String> entry : attributesInfo.entrySet()) {
                String[] parts = entry.getValue().split(";");
                Identifier id = Identifier.parse(parts[0]);
                double amount = Double.parseDouble(parts[1]);
                AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(parts[2].toUpperCase());
                MobEffect.AttributeTemplate template = new MobEffect.AttributeTemplate(id, amount, operation);
                templates.put(BuiltInRegistries.ATTRIBUTE.get(Identifier.parse(entry.getKey())).orElse(null), template);
            }
            return templates;
        }

        public static void toBuffer(final ByteBuf buffer, AttributesInfo info) {
            buffer.writeInt(info.attributesInfo().size());
            for (Map.Entry<String, String> entry : info.attributesInfo().entrySet()) {
                Utf8String.write(buffer, entry.getKey(), 32767);
                Utf8String.write(buffer, entry.getValue(), 32767);
            }
        }

        public static AttributesInfo fromBuffer(ByteBuf buffer) {
            int size = buffer.readInt();
            Map<String, String> attributeTemplates = new LinkedHashMap<>();
            for (int i = 0; i < size; i++) {
                String key = Utf8String.read(buffer, 32767);
                String parts = Utf8String.read(buffer, 32767);
                attributeTemplates.put(key, parts);
            }
            return new AttributesInfo(attributeTemplates);
        }
    }
}
