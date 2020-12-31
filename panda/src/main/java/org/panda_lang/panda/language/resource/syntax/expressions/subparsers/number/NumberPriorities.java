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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.number;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.interpreter.parser.PandaParserException;
import org.panda_lang.utilities.commons.collection.Maps;

import java.util.Map;

public class NumberPriorities {

    protected static final int BYTE = 10;
    protected static final int SHORT = 20;
    protected static final int INT = 30;
    protected static final int LONG = 40;
    protected static final int FLOAT = 50;
    protected static final int DOUBLE = 60;

    protected static final Map<String, Integer> HIERARCHY = Maps.of(
            "panda::Byte", BYTE,
            "panda::Short", SHORT,
            "panda::Int", INT,
            "panda::Long", LONG,
            "panda::Float", FLOAT,
            "panda::Double", DOUBLE
    );

    public int getPriority(Type type) {
        @Nullable Integer priority = HIERARCHY.get(type.getName());

        if (priority == null) {
            priority = HIERARCHY.get(type.getName().replace("Primitive", "")); // TODO: prefix hotfix
        }

        if (priority == null) {
            throw new PandaParserException("Unknown number type: " + type.getName());
        }

        return priority;
    }

}
