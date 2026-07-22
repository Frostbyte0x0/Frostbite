package org.exodusstudio.frostbite.common.contracts;

import java.util.HashMap;
import java.util.List;

public class ContractAttributes {
    public static final HashMap<String, ContractAttribute> ATTRIBUTES = new HashMap<>();

    // Player positive attributes
    public static final ScalableContractAttribute CHILLY = of(
            "chilly",
            List.of(1.5f, 3f, 5f),
            0,
            Polarity.POSITIVE,
            ContractTarget.PLAYER
    );
    public static final ScalableContractAttribute DEXTEROUS = of(
            "dexterous",
            List.of(3f, 5f, 10f),
            0,
            Polarity.POSITIVE,
            ContractTarget.PLAYER
    );
    public static final ScalableContractAttribute ILLUSIONS = of(
            "illusions",
            List.of(2f, 5f, 10f),
            0,
            Polarity.POSITIVE,
            ContractTarget.PLAYER
    );
    public static final ContractAttribute REPEL = of(
            "repel",
            ContractRank.GREY,
            Polarity.POSITIVE,
            ContractTarget.PLAYER
    );
    public static final ScalableContractAttribute FROG = of(
            "frog",
            List.of(1f, 2f, 3f),
            0,
            Polarity.POSITIVE,
            ContractTarget.PLAYER
    );
    public static final ContractAttribute SMELLY = of(
            "smelly",
            ContractRank.BLACK,
            Polarity.POSITIVE,
            ContractTarget.LIVING
    );

    // Player negative attributes
    public static final ContractAttribute DIABETIC = of(
            "diabetic",
            ContractRank.WHITE,
            Polarity.NEGATIVE,
            ContractTarget.PLAYER
    );
    public static final ContractAttribute VEGETARIAN = of(
            "vegetarian",
            ContractRank.GREY,
            Polarity.NEGATIVE,
            ContractTarget.PLAYER
    );
    public static final ContractAttribute INEXPERIENCED = of(
            "inexperienced",
            ContractRank.WHITE,
            Polarity.NEGATIVE,
            ContractTarget.PLAYER
    );
    public static final ContractAttribute UNAWARE = of(
            "unaware",
            ContractRank.BLACK,
            Polarity.NEGATIVE,
            ContractTarget.PLAYER
    );
    public static final ScalableContractAttribute MISSTEP = of(
            "misstep",
            List.of(1f, 3f, 5f),
            0,
            Polarity.NEGATIVE,
            ContractTarget.PLAYER
    );


    // Living positive attributes
    public static final ScalableContractAttribute FAST = of(
            "fast",
            List.of(20f, 30f, 40f),
            0,
            Polarity.POSITIVE,
            ContractTarget.LIVING
    );
    public static final ScalableContractAttribute TANK = of(
            "tank",
            List.of(2f, 4f, 6f),
            0,
            Polarity.POSITIVE,
            ContractTarget.LIVING
    );
    public static final ScalableContractAttribute SUMO = of(
            "sumo",
            List.of(20f, 30f, 40f),
            0,
            Polarity.POSITIVE,
            ContractTarget.LIVING
    );
    public static final ContractAttribute CATLIKE = of(
            "catlike",
            ContractRank.BLACK,
            Polarity.POSITIVE,
            ContractTarget.PLAYER
    );
    public static final ScalableContractAttribute COWARDLY = of(
            "cowardly",
            List.of(20f, 30f, 40f),
            0,
            Polarity.POSITIVE,
            ContractTarget.LIVING
    );
    public static final ScalableContractAttribute BERSERK = of(
            "berserk",
            List.of(10f, 20f, 30f),
            0,
            Polarity.POSITIVE,
            ContractTarget.LIVING
    );
    public static final ScalableContractAttribute FLAME = of(
            "flame",
            List.of(2f, 4f, 6f),
            0,
            Polarity.POSITIVE,
            ContractTarget.LIVING
    );

    // Living negative attributes
    public static final ContractAttribute SLIDY = of(
            "slidy",
            ContractRank.GREY,
            Polarity.NEGATIVE,
            ContractTarget.LIVING
    );
    public static final ScalableContractAttribute HEAVY = of(
            "heavy",
            List.of(20f, 30f, 40f),
            0,
            Polarity.NEGATIVE,
            ContractTarget.LIVING
    );
    public static final ScalableContractAttribute CHARGED = of(
            "charged",
            List.of(10f, 20f, 30f, 40f),
            0,
            Polarity.NEGATIVE,
            ContractTarget.LIVING
    );
    public static final ScalableContractAttribute PALPITATIONS = of(
            "palpitations",
            List.of(120f, 90f, 60f),
            0,
            Polarity.NEGATIVE,
            ContractTarget.LIVING
    );
    public static final ContractAttribute HYDROPHOBIA = of(
            "hydrophobia",
            ContractRank.WHITE,
            Polarity.NEGATIVE,
            ContractTarget.LIVING
    );
    public static final ScalableContractAttribute TRANSPORT = of(
            "transport",
            List.of(1f, 2f, 3f),
            0,
            Polarity.NEGATIVE,
            ContractTarget.LIVING
    );

    // Weapon positive attributes
    public static final ScalableContractAttribute SEQUENCE = of(
                "sequence",
                List.of(15f, 30f, 50f),
                0,
                Polarity.POSITIVE,
                ContractTarget.WEAPON
            );
    public static final ScalableContractAttribute CRITICAL = of(
                "critical",
                List.of(15f, 30f, 50f),
                0,
                Polarity.POSITIVE,
                ContractTarget.WEAPON
            );
    public static final ScalableContractAttribute LEECH = of(
                "leech",
                List.of(1f, 2f, 3f),
                0,
                Polarity.POSITIVE,
                ContractTarget.WEAPON
            );
    public static final ScalableContractAttribute MIDAS = of(
                "midas",
                List.of(1f, 2f, 3f),
                0,
                Polarity.POSITIVE,
                ContractTarget.WEAPON
            );
    public static final ScalableContractAttribute SPIN = of(
                "spin",
                List.of(10f, 20f, 30f),
                0,
                Polarity.POSITIVE,
                ContractTarget.WEAPON
            );
    public static final ScalableContractAttribute UPPERCUT = of(
                "uppercut",
                List.of(10f, 20f, 30f),
                0,
                Polarity.POSITIVE,
                ContractTarget.WEAPON
            );
    public static final ScalableContractAttribute LOCK_IN = of(
                "lock_in",
                List.of(25f, 50f, 75f),
                0,
                Polarity.POSITIVE,
                ContractTarget.WEAPON
            );
    public static final ScalableContractAttribute SHARP = of(
                "sharp",
                List.of(25f, 50f, 75f),
                0,
                Polarity.POSITIVE,
                ContractTarget.WEAPON
            );

    // Weapon negative attributes
    public static final ScalableContractAttribute CORROSION = of(
                "corrosion",
                List.of(120f, 90f, 60f),
                0,
                Polarity.NEGATIVE,
                ContractTarget.WEAPON
            );
    public static final ScalableContractAttribute SLIPPERY = of(
                "slippery",
                List.of(1f, 2f, 3f),
                0,
                Polarity.NEGATIVE,
                ContractTarget.WEAPON
            );
    public static final ContractAttribute DULL = of(
                "dull",
                ContractRank.BLACK,
                Polarity.NEGATIVE,
                ContractTarget.WEAPON
            );
    public static final ScalableContractAttribute BRITTLE = of(
                "brittle",
                List.of(1f, 2f, 3f),
                0,
                Polarity.NEGATIVE,
                ContractTarget.WEAPON
            );


    // Location positive attributes


    // Location negative attributes




    private static ContractAttribute of(String id, ContractRank rank, Polarity polarity, ContractTarget target) {
        ContractAttribute attribute = new ContractAttribute(id, rank, polarity, target);
        ATTRIBUTES.put(attribute.id, attribute);
        return attribute;
    }

    private static ScalableContractAttribute of(String id, List<Float> stat, int start, Polarity polarity, ContractTarget target) {
        ScalableContractAttribute attribute = new ScalableContractAttribute(id, stat, start, polarity, target);
        ATTRIBUTES.put(attribute.id, attribute);
        return attribute;
    }
}
