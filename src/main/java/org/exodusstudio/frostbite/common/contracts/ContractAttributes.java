package org.exodusstudio.frostbite.common.contracts;

import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.exodusstudio.frostbite.Frostbite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContractAttributes {
    public static final HashMap<String, ContractAttribute> ATTRIBUTES = new HashMap<>();

    // Player positive attributes
    public static final ContractAttribute CHILLY = of(
            new ContractAttribute.Builder("chilly", Polarity.POSITIVE, ContractTarget.PLAYER)
                .scalable(List.of(1.5f, 3f, 5f))
    );
    public static final ContractAttribute DEXTEROUS = of(
            new ContractAttribute.Builder("dexterous", Polarity.POSITIVE, ContractTarget.PLAYER)
                .scalable(List.of(3f, 5f, 10f))
    );
    public static final ContractAttribute ILLUSIONS = of(
            new ContractAttribute.Builder("illusions", Polarity.POSITIVE, ContractTarget.PLAYER)
                .scalable(List.of(2f, 5f, 10f))
    );
    public static final ContractAttribute REPEL = of(
            new ContractAttribute.Builder("repel", Polarity.POSITIVE, ContractTarget.PLAYER)
                .rank(ContractRank.GREY)
    );
    public static final ContractAttribute FROG = of(
            new ContractAttribute.Builder("frog", Polarity.POSITIVE, ContractTarget.PLAYER)
                .scalable(List.of(1f, 2f, 3f))
    );

    // Player negative attributes
    public static final ContractAttribute DIABETIC = of(
            new ContractAttribute.Builder("diabetic", Polarity.NEGATIVE, ContractTarget.PLAYER)
                .rank(ContractRank.WHITE)
    );
    public static final ContractAttribute VEGETARIAN = of(
            new ContractAttribute.Builder("vegetarian", Polarity.NEGATIVE, ContractTarget.PLAYER)
                .rank(ContractRank.GREY)
    );
    public static final ContractAttribute INEXPERIENCED = of(
            new ContractAttribute.Builder("inexperienced", Polarity.NEGATIVE, ContractTarget.PLAYER)
                .rank(ContractRank.WHITE)
    );
    public static final ContractAttribute UNAWARE = of(
            new ContractAttribute.Builder("unaware", Polarity.NEGATIVE, ContractTarget.PLAYER)
                .rank(ContractRank.BLACK)
    );
    public static final ContractAttribute MISSTEP = of(
            new ContractAttribute.Builder("misstep", Polarity.NEGATIVE, ContractTarget.PLAYER)
                .scalable(List.of(1f, 3f, 5f))
    );


    // Living positive attributes
    public static final ContractAttribute FAST = of(
            new ContractAttribute.Builder("fast", Polarity.POSITIVE, ContractTarget.LIVING)
                .scalable(List.of(20f, 30f, 40f))
                .templateInfo(Attributes.MOVEMENT_SPEED, Map.of(
                        1, new MobEffect.AttributeTemplate(
                                Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "fast_contract"), 0.2f, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                        2, new MobEffect.AttributeTemplate(
                                Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "fast_contract"), 0.3f, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                        3, new MobEffect.AttributeTemplate(
                                Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "fast_contract"), 0.4f, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                ))
    );
    public static final ContractAttribute TANK = of(
            new ContractAttribute.Builder("tank", Polarity.POSITIVE, ContractTarget.LIVING)
                .scalable(List.of(2f, 4f, 6f))
    );
    public static final ContractAttribute SUMO = of(
            new ContractAttribute.Builder("sumo", Polarity.POSITIVE, ContractTarget.LIVING)
                .scalable(List.of(20f, 30f, 40f))
    );
    public static final ContractAttribute CATLIKE = of(
            new ContractAttribute.Builder("catlike", Polarity.POSITIVE, ContractTarget.PLAYER)
                .rank(ContractRank.BLACK)
    );
    public static final ContractAttribute COWARDLY = of(
            new ContractAttribute.Builder("cowardly", Polarity.POSITIVE, ContractTarget.LIVING)
                .scalable(List.of(20f, 30f, 40f))
    );
    public static final ContractAttribute BERSERK = of(
            new ContractAttribute.Builder("berserk", Polarity.POSITIVE, ContractTarget.LIVING)
                .scalable(List.of(10f, 20f, 30f))
    );
    public static final ContractAttribute FLAME = of(
            new ContractAttribute.Builder("flame", Polarity.POSITIVE, ContractTarget.LIVING)
                .scalable(List.of(2f, 4f, 6f))
    );
    public static final ContractAttribute SMELLY = of(
            new ContractAttribute.Builder("smelly", Polarity.POSITIVE, ContractTarget.LIVING)
                    .rank(ContractRank.BLACK)
    );

    // Living negative attributes
    public static final ContractAttribute SLIDY = of(
            new ContractAttribute.Builder("slidy", Polarity.NEGATIVE, ContractTarget.LIVING)
                .rank(ContractRank.GREY)
    );
    public static final ContractAttribute HEAVY = of(
            new ContractAttribute.Builder("heavy", Polarity.NEGATIVE, ContractTarget.LIVING)
                .scalable(List.of(20f, 30f, 40f))
    );
    public static final ContractAttribute CHARGED = of(
            new ContractAttribute.Builder("charged", Polarity.NEGATIVE, ContractTarget.LIVING)
                .scalable(List.of(10f, 20f, 30f, 40f))
    );
    public static final ContractAttribute PALPITATIONS = of(
            new ContractAttribute.Builder("palpitations", Polarity.NEGATIVE, ContractTarget.LIVING)
                .scalable(List.of(120f, 90f, 60f))
    );
    public static final ContractAttribute HYDROPHOBIA = of(
            new ContractAttribute.Builder("hydrophobia", Polarity.NEGATIVE, ContractTarget.LIVING)
                .rank(ContractRank.WHITE)
    );
    public static final ContractAttribute TRANSPORT = of(
            new ContractAttribute.Builder("transport", Polarity.NEGATIVE, ContractTarget.LIVING)
                .scalable(List.of(1f, 2f, 3f))
    );

    // Weapon positive attributes
    public static final ContractAttribute SEQUENCE = of(
                new ContractAttribute.Builder("sequence", Polarity.POSITIVE, ContractTarget.WEAPON)
                    .scalable(List.of(15f, 30f, 50f))
            );
    public static final ContractAttribute CRITICAL = of(
                new ContractAttribute.Builder("critical", Polarity.POSITIVE, ContractTarget.WEAPON)
                    .scalable(List.of(15f, 30f, 50f))
            );
    public static final ContractAttribute LEECH = of(
                new ContractAttribute.Builder("leech", Polarity.POSITIVE, ContractTarget.WEAPON)
                    .scalable(List.of(1f, 2f, 3f))
            );
    public static final ContractAttribute MIDAS = of(
                new ContractAttribute.Builder("midas", Polarity.POSITIVE, ContractTarget.WEAPON)
                    .scalable(List.of(1f, 2f, 3f))
            );
    public static final ContractAttribute SPIN = of(
                new ContractAttribute.Builder("spin", Polarity.POSITIVE, ContractTarget.WEAPON)
                    .scalable(List.of(10f, 20f, 30f))
            );
    public static final ContractAttribute UPPERCUT = of(
                new ContractAttribute.Builder("uppercut", Polarity.POSITIVE, ContractTarget.WEAPON)
                    .scalable(List.of(10f, 20f, 30f))
            );
    public static final ContractAttribute LOCK_IN = of(
                new ContractAttribute.Builder("lock_in", Polarity.POSITIVE, ContractTarget.WEAPON)
                    .scalable(List.of(25f, 50f, 75f))
            );
    public static final ContractAttribute SHARP = of(
            new ContractAttribute.Builder("sharp", Polarity.POSITIVE, ContractTarget.WEAPON)
                .scalable(List.of(25f, 50f, 75f))
            );

    // Weapon negative attributes
    public static final ContractAttribute CORROSION = of(
            new ContractAttribute.Builder("corrosion", Polarity.NEGATIVE, ContractTarget.WEAPON)
                .scalable(List.of(120f, 90f, 60f))
            );
    public static final ContractAttribute SLIPPERY = of(
            new ContractAttribute.Builder("slippery", Polarity.NEGATIVE, ContractTarget.WEAPON)
                .scalable(List.of(1f, 2f, 3f))
            );
    public static final ContractAttribute DULL = of(
            new ContractAttribute.Builder("dull", Polarity.NEGATIVE, ContractTarget.WEAPON)
                .rank(ContractRank.BLACK)
            );
    public static final ContractAttribute BRITTLE = of(
            new ContractAttribute.Builder("brittle", Polarity.NEGATIVE, ContractTarget.WEAPON)
                .scalable(List.of(1f, 2f, 3f))
            );


    // Location positive attributes


    // Location negative attributes




    private static ContractAttribute of(
            ContractAttribute.Builder builder
    ) {
        ContractAttribute attribute = builder.build();
        ATTRIBUTES.put(attribute.id, attribute);
        return attribute;
    }
}
