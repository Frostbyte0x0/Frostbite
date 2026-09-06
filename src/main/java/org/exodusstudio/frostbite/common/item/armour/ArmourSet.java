package org.exodusstudio.frostbite.common.item.armour;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.network.Utf8String;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;

import java.util.Map;

public record ArmourSet(
        String id,
        Map<Holder<Attribute>, MobEffect.AttributeTemplate> attributes
) {
    public static final Codec<ArmourSet> CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<ArmourSet, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getStringValue(input).flatMap(s -> {
                if (!ArmourSets.SETS.containsKey(s)) throw new IllegalArgumentException("Invalid armour set: " + s);
                return DataResult.success(Pair.of(ArmourSets.SETS.get(s), input));
            });
        }

        @Override
        public <T> DataResult<T> encode(ArmourSet set, DynamicOps<T> ops, T prefix) {
            return ops.mergeToPrimitive(prefix, ops.createString(set.id));
        }
    };

    public static final StreamCodec<ByteBuf, ArmourSet> STREAM_CODEC =
            StreamCodec.of(
                    (b, d) -> Utf8String.write(b, d.id(), 32767),
                    b -> {
                        String s = Utf8String.read(b, 32767);
                        if (!ArmourSets.SETS.containsKey(s)) throw new IllegalArgumentException("Invalid armour set: " + s);
                        return ArmourSets.SETS.get(s);
                    }
            );

    public void addSetBonuses(AttributeMap map) {
        for (Map.Entry<Holder<Attribute>, MobEffect.AttributeTemplate> entry : attributes.entrySet()) {
            Holder<Attribute> attribute = entry.getKey();

            AttributeInstance attr = map.getInstance(attribute);
            if (attr != null) {
                try {
                    attr.addPermanentModifier(entry.getValue().create(0));
                } catch (IllegalArgumentException _) {}
            }
        }
    }

    public void removeSetBonuses(AttributeMap map) {
        for (Map.Entry<Holder<Attribute>, MobEffect.AttributeTemplate> entry : attributes.entrySet()) {
            Holder<Attribute> attribute = entry.getKey();

            AttributeInstance attr = map.getInstance(attribute);
            if (attr != null) {
                attr.removeModifier(entry.getValue().id());
            }
        }
    }

    public static ArmourSet getFullSet(EntityEquipment equipment) {
        EquipmentSlot[] slots = new EquipmentSlot[]{EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
        ItemStack head = equipment.get(EquipmentSlot.HEAD);

        if (head.isEmpty() || !head.has(DataComponentTypeRegistry.ARMOUR_SET)) return null;
        ArmourSet set = head.get(DataComponentTypeRegistry.ARMOUR_SET).set();

        for (EquipmentSlot slot : slots) {
            ItemStack stack = equipment.get(slot);

            if (stack.isEmpty() ||
                    !stack.has(DataComponentTypeRegistry.ARMOUR_SET) ||
                    !stack.get(DataComponentTypeRegistry.ARMOUR_SET).set().id().equals(set.id())) return null;
        }
        return set;
    }

    public int getPiecesWithSet(EntityEquipment equipment) {
        EquipmentSlot[] slots = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

        int i = 0;
        for (EquipmentSlot slot : slots) {
            ItemStack stack = equipment.get(slot);

            if (!stack.isEmpty() &&
                    stack.has(DataComponentTypeRegistry.ARMOUR_SET) &&
                    stack.get(DataComponentTypeRegistry.ARMOUR_SET).set().id().equals(id())) i++;
        }
        return i;
    }

    public static void toBuffer(final ByteBuf buffer, ArmourSet set) {
        Utf8String.write(buffer, set.id, 32767);
    }

    public static ArmourSet fromBuffer(ByteBuf buffer) {
        return ArmourSets.SETS.get(Utf8String.read(buffer, 32767));
    }
}
