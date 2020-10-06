/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.language.resource.syntax.operator;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

import static org.panda_lang.utilities.commons.collection.Lists.add;

/**
 * Default operators
 */
public final class Operators {

    /*
        Math
     */

    private static final Collection<Operator> VALUES = new ArrayList<>();

    public static final Operator ADDITION = add(VALUES, new Operator(OperatorFamilies.MATH, "+"));

    public static final Operator SUBTRACTION = add(VALUES, new Operator(OperatorFamilies.MATH, "-"));

    public static final Operator MULTIPLICATION = add(VALUES, new Operator(OperatorFamilies.MATH, "*"));

    public static final Operator DIVISION = add(VALUES, new Operator(OperatorFamilies.MATH, "/"));

    public static final Operator MODULE = add(VALUES, new Operator(OperatorFamilies.MATH, "%"));

    /*
        Logic
     */

    public static final Operator BITWISE_NOT = add(VALUES, new Operator(OperatorFamilies.MATH, "~"));

    public static final Operator BITWISE_AND = add(VALUES, new Operator(OperatorFamilies.MATH, "&"));

    public static final Operator BITWISE_OR = add(VALUES, new Operator(OperatorFamilies.MATH, "|"));

    public static final Operator BITWISE_XOR = add(VALUES, new Operator(OperatorFamilies.MATH, "^"));

    public static final Operator BITWISE_LEFT_SHIFT = add(VALUES, new Operator(OperatorFamilies.MATH, "<<"));

    public static final Operator BITWISE_RIGHT_SHIFT = add(VALUES, new Operator(OperatorFamilies.MATH, ">>"));

    /*
        Logical
     */

    public static final Operator LESS_THAN = add(VALUES, new Operator(OperatorFamilies.LOGICAL, "<"));

    public static final Operator ANGLE_LEFT = LESS_THAN;

    public static final Operator GREATER_THAN = add(VALUES, new Operator(OperatorFamilies.LOGICAL, ">"));

    public static final Operator ANGLE_RIGHT = GREATER_THAN;

    public static final Operator EQUAL_TO = add(VALUES, new Operator(OperatorFamilies.LOGICAL, "=="));

    public static final Operator NOT_EQUAL_TO = add(VALUES, new Operator(OperatorFamilies.LOGICAL, "!="));

    public static final Operator GREATER_THAN_OR_EQUAL_TO = add(VALUES, new Operator(OperatorFamilies.LOGICAL, ">="));

    public static final Operator LESS_THAN_OR_EQUAL_TO = add(VALUES, new Operator(OperatorFamilies.LOGICAL, "<="));

    public static final Operator AND = add(VALUES, new Operator(OperatorFamilies.LOGICAL, "&&"));

    public static final Operator OR = add(VALUES, new Operator(OperatorFamilies.LOGICAL, "||"));

    public static final Operator NOT = add(VALUES, new Operator(OperatorFamilies.LOGICAL, "!"));

    /*
        Crease
     */

    public static final Operator INCREMENT = add(VALUES, new Operator(OperatorFamilies.CREASE, "++"));

    public static final Operator DECREMENT = add(VALUES, new Operator(OperatorFamilies.CREASE, "--"));

    /*
        Assignation
     */

    public static final Operator ASSIGNMENT = add(VALUES, new Operator(OperatorFamilies.ASSIGNATION, "="));

    public static final Operator ADDITION_ASSIGNMENT = add(VALUES, new Operator(OperatorFamilies.ASSIGNATION, "+="));

    public static final Operator SUBTRACTION_ASSIGNMENT = add(VALUES, new Operator(OperatorFamilies.ASSIGNATION, "-="));

    public static final Operator MULTIPLICATION_ASSIGNMENT = add(VALUES, new Operator(OperatorFamilies.ASSIGNATION, "*="));

    public static final Operator DIVISION_ASSIGNMENT = add(VALUES, new Operator(OperatorFamilies.ASSIGNATION, "/="));

    public static final Operator REMAINDER_ASSIGNMENT = add(VALUES, new Operator(OperatorFamilies.ASSIGNATION, "%="));

    /*
        Undefined
     */

    public static final Operator EROTEME = add(VALUES, new Operator(OperatorFamilies.UNDEFINED, "?"));

    public static final Operator COLON = add(VALUES, new Operator(OperatorFamilies.UNDEFINED, ":"));

    public static final Operator ARROW = add(VALUES, new Operator(OperatorFamilies.UNDEFINED, "->"));

    public static final Operator LAMBDA = add(VALUES, new Operator(OperatorFamilies.UNDEFINED, "=>"));

    private Operators() { }

    public static Operator[] getFamily(@Nullable String family) {
        return VALUES.stream()
                .filter(operator -> OperatorUtils.isMemberOf(operator, family))
                .toArray(Operator[]::new);
    }

    public static Operator[] values() {
        return VALUES.toArray(new Operator[0]);
    }

}
