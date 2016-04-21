package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.statement.association.Association;

public class StringAssociation {

    static {
        Association<String> stringAssociation = new Association<>(String.class, "String");
        stringAssociation.constructor(alice -> String.valueOf(alice.getValueOfFactor(0)));
    }

}
