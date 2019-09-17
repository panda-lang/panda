/*
 * Copyright (c) 2015-2019 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.panda.language.resource.syntax.operator;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.utilities.commons.ReflectionUtils;

import java.util.Arrays;
import java.util.Collection;

/**
 * Default operators
 */
public final class Operators {

    public static final Operator ADDITION = new Operator(OperatorFamilies.MATH, "+");

    public static final Operator SUBTRACTION = new Operator(OperatorFamilies.MATH, "-");

    public static final Operator MULTIPLICATION = new Operator(OperatorFamilies.MATH, "*");

    public static final Operator DIVISION = new Operator(OperatorFamilies.MATH, "/");

    public static final Operator MODULE = new Operator(OperatorFamilies.MATH, "%");

    public static final Operator BITWISE_NOT = new Operator(OperatorFamilies.MATH, "~");

    public static final Operator BITWISE_AND = new Operator(OperatorFamilies.MATH, "&");

    public static final Operator BITWISE_OR = new Operator(OperatorFamilies.MATH, "|");

    public static final Operator BITWISE_XOR = new Operator(OperatorFamilies.MATH, "^");

    public static final Operator BITWISE_LEFT_SHIFT = new Operator(OperatorFamilies.MATH, "<<");

    public static final Operator BITWISE_RIGHT_SHIFT = new Operator(OperatorFamilies.MATH, ">>");


    public static final Operator EQUAL_TO = new Operator(OperatorFamilies.LOGICAL, "==");

    public static final Operator NOT_EQUAL_TO = new Operator(OperatorFamilies.LOGICAL, "!=");

    public static final Operator GREATER_THAN = new Operator(OperatorFamilies.LOGICAL, ">");

    public static final Operator LESS_THAN = new Operator(OperatorFamilies.LOGICAL, "<");

    public static final Operator GREATER_THAN_OR_EQUAL_TO = new Operator(OperatorFamilies.LOGICAL, ">=");

    public static final Operator LESS_THAN_OR_EQUAL_TO = new Operator(OperatorFamilies.LOGICAL, "<=");

    public static final Operator AND = new Operator(OperatorFamilies.LOGICAL, "&&");

    public static final Operator OR = new Operator(OperatorFamilies.LOGICAL, "||");

    public static final Operator NOT = new Operator(OperatorFamilies.LOGICAL, "!");


    public static final Operator INCREMENT = new Operator(OperatorFamilies.CREASE, "++");

    public static final Operator DECREMENT = new Operator(OperatorFamilies.CREASE, "--");


    public static final Operator ASSIGNMENT = new Operator(OperatorFamilies.ASSIGNATION, "=");

    public static final Operator ADDITION_ASSIGNMENT = new Operator(OperatorFamilies.ASSIGNATION, "+=");

    public static final Operator SUBTRACTION_ASSIGNMENT = new Operator(OperatorFamilies.ASSIGNATION, "-=");

    public static final Operator MULTIPLICATION_ASSIGNMENT = new Operator(OperatorFamilies.ASSIGNATION, "*=");

    public static final Operator DIVISION_ASSIGNMENT = new Operator(OperatorFamilies.ASSIGNATION, "/=");

    public static final Operator REMAINDER_ASSIGNMENT = new Operator(OperatorFamilies.ASSIGNATION, "%=");


    public static final Operator EROTEME = new Operator(OperatorFamilies.UNDEFINED, "?");

    public static final Operator COLON = new Operator(OperatorFamilies.UNDEFINED, ":");


    private static final Collection<Operator> VALUES;

    static {
        VALUES = ReflectionUtils.getStaticFieldValues(Operators.class, Operator.class);
    }

    private Operators() { }

    public static Operator[] values() {
        return VALUES.toArray(new Operator[0]);
    }

    public static Operator[] getFamily(@Nullable String family) {
        return Arrays.stream(Operators.values())
                .filter(operator -> OperatorUtils.isMemberOf(operator, family))
                .toArray(Operator[]::new);
    }

}
