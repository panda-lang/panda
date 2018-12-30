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

package org.panda_lang.panda.framework.language.resource.syntax.operator;

import org.panda_lang.panda.utilities.commons.ReflectionUtils;

import java.util.Collection;

/**
 * Default operators
 */
public class Operators {

    public static final Operator ASSIGNMENT = new Operator("=");

    public static final Operator ADDITION = new Operator("+");

    public static final Operator SUBTRACTION = new Operator("-");

    public static final Operator MULTIPLICATION = new Operator("*");

    public static final Operator DIVISION = new Operator("/");

    public static final Operator MODULE = new Operator("%");

    public static final Operator INCREMENT = new Operator("++");

    public static final Operator DECREMENT = new Operator("--");

    public static final Operator EQUAL_TO = new Operator("==");

    public static final Operator NOT_EQUAL_TO = new Operator("!=");

    public static final Operator GREATER_THAN = new Operator(">");

    public static final Operator LESS_THAN = new Operator("<");

    public static final Operator GREATER_THAN_OR_EQUAL_TO = new Operator(">=");

    public static final Operator LESS_THAN_OR_EQUAL_TO = new Operator("<=");

    public static final Operator NOT = new Operator("!");

    public static final Operator AND = new Operator("&&");

    public static final Operator OR = new Operator("||");

    public static final Operator BITWISE_NOT = new Operator("~");

    public static final Operator BITWISE_AND = new Operator("&");

    public static final Operator BITWISE_OR = new Operator("|");

    public static final Operator BITWISE_XOR = new Operator("^");

    public static final Operator BITWISE_LEFT_SHIFT = new Operator("<<");

    public static final Operator BITWISE_RIGHT_SHIFT = new Operator(">>");

    public static final Operator COLON = new Operator(":");

    private static final Collection<Operator> VALUES;

    static {
        VALUES = ReflectionUtils.getStaticFieldValues(Operators.class, Operator.class);
    }

    public static Operator[] values() {
        return VALUES.toArray(new Operator[0]);
    }

}
