package org.exodusstudio.frostbite.common.contracts;

import java.util.HashMap;

public class ContractAttributes {
    public static final HashMap<String, ContractAttribute> ATTRIBUTES = new HashMap<>();

    public static final ContractAttribute DIABETIC = of(
                "diabetic",
                ContractRank.WHITE,
                Polarity.NEGATIVE
            );


    private static ContractAttribute of(String id, ContractRank rank, Polarity polarity) {
        ContractAttribute attribute = new ContractAttribute(id, rank, polarity);
        ATTRIBUTES.put(attribute.id, attribute);
        return attribute;
    }
}
