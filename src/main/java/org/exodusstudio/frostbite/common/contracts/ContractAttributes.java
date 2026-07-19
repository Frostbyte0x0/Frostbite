package org.exodusstudio.frostbite.common.contracts;

import java.util.HashMap;

public class ContractAttributes {
    public static final HashMap<String, ContractAttribute> ATTRIBUTES = new HashMap<>();

    public static final ContractAttribute SEQUENCE = of(
                "sequence",
                ContractRank.LIGHT,
                Polarity.POSITIVE
            );
    public static final ContractAttribute CRITICAL = of(
                "critical",
                ContractRank.LIGHT,
                Polarity.POSITIVE
            );
    public static final ContractAttribute CHILLY = of(
                "chilly",
                ContractRank.WHITE,
                Polarity.POSITIVE
            );
    public static final ContractAttribute DIABETIC = of(
                "diabetic",
                ContractRank.WHITE,
                Polarity.NEGATIVE
            );
    public static final ContractAttribute VEGETARIAN = of(
                "vegetarian",
                ContractRank.LIGHT,
                Polarity.NEGATIVE
            );


    private static ContractAttribute of(String id, ContractRank rank, Polarity polarity) {
        ContractAttribute attribute = new ContractAttribute(id, rank, polarity);
        ATTRIBUTES.put(attribute.id, attribute);
        return attribute;
    }
}
