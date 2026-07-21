package org.exodusstudio.frostbite.common.contracts;

import java.util.HashMap;
import java.util.List;

public class ContractAttributes {
    public static final HashMap<String, ContractAttribute> ATTRIBUTES = new HashMap<>();

    public static final ContractAttribute SEQUENCE = of(
                "sequence",
                ContractRank.LIGHT,
                Polarity.POSITIVE,
                ContractTarget.WEAPON
            );
    public static final ContractAttribute CRITICAL = of(
                "critical",
                ContractRank.LIGHT,
                Polarity.POSITIVE,
                ContractTarget.WEAPON
            );
    public static final ScalableContractAttribute CHILLY = of(
                "chilly",
                List.of(10f, 20f, 30f, 40f),
                0,
                Polarity.POSITIVE,
                ContractTarget.PLAYER
            );
    public static final ContractAttribute DIABETIC = of(
                "diabetic",
                ContractRank.WHITE,
                Polarity.NEGATIVE,
                ContractTarget.PLAYER
            );
    public static final ContractAttribute VEGETARIAN = of(
                "vegetarian",
                ContractRank.LIGHT,
                Polarity.NEGATIVE,
                ContractTarget.PLAYER
            );


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
