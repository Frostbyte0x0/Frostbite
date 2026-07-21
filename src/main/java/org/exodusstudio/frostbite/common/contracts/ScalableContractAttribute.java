package org.exodusstudio.frostbite.common.contracts;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.Utf8String;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.common.item.contract.ContractFragmentItem;
import org.exodusstudio.frostbite.common.registry.AttachmentRegistry;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;

import java.util.ArrayList;
import java.util.List;

import static org.exodusstudio.frostbite.common.contracts.PlayerLiteracy.*;

public class ScalableContractAttribute extends ContractAttribute {
    private final List<Float> stats;
    private final int start;

    public ScalableContractAttribute(String id, List<Float> stats, int start, Polarity polarity, ContractTarget target) {
        super(id, null, polarity, target);
        this.stats = stats;
        this.start = start;
    }

    public Component getExtraInfo(Player player, ItemStack stack) {
        PlayerContractInfo info = player.getData(AttachmentRegistry.PLAYER_CONTRACT_INFO);
        PlayerLiteracy r = PlayerContractInfo.hasDiscoveredAttribute(player, this) ? LITERATE : info.literacyRank();
        MutableComponent c = Component.literal("    ");
        if (r.ordinal() == PROFICIENT.ordinal()) {
            c.append(Component.translatable("contract.attribute." + id + "_desc"));
        } else if (r.ordinal() == LITERATE.ordinal()) {
            c.append(Component.translatable("contract.attribute." + id + "_desc_complete", getStat(stack), "%"));
        } else {
            c.append(Component.literal("§kaaaa"));
        }
        return c.withStyle(getPolarity() == Polarity.POSITIVE ? ChatFormatting.GREEN : ChatFormatting.RED);
    }

    public Component getSmallInfo(Player player, ItemStack stack) {
        PlayerContractInfo info = player.getData(AttachmentRegistry.PLAYER_CONTRACT_INFO);
        PlayerLiteracy r = PlayerContractInfo.hasDiscoveredAttribute(player, this) ? LITERATE : info.literacyRank();

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

    public static float getStat(ItemStack stack) {
        ScalableContractAttribute attribute = (ScalableContractAttribute) stack.get(DataComponentTypeRegistry.CONTRACT_ATTRIBUTE).attribute();
        int level = getLevel(stack);
        if (level > 0 && level <= attribute.stats.size()) {
            return attribute.stats.get(level - 1);
        }
        return 0.0f;
    }

    public String getNumeral(ItemStack stack) {
        return switch(getLevel(stack)) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            default -> "";
        };
    }

    @SuppressWarnings("DataFlowIssue")
    public static int getLevel(ItemStack stack) {
        if (stack.getItem() instanceof ContractFragmentItem) {
            return stack.get(DataComponentTypeRegistry.CONTRACT_ATTRIBUTE).level();
        }
        return 0;
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
}
