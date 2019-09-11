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

package org.panda_lang.panda.language.resource.expression.subparsers.number;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.language.resource.PandaTypes;
import org.panda_lang.panda.utilities.commons.collection.Maps;

import java.util.Map;

public class NumberPriorities {

    protected static final int BYTE = 10;
    protected static final int SHORT = 20;
    protected static final int INT = 30;
    protected static final int LONG = 40;
    protected static final int FLOAT = 50;
    protected static final int DOUBLE = 60;

    protected static final Map<ClassPrototype, Integer> HIERARCHY = Maps.of(
            PandaTypes.BYTE, BYTE,
            PandaTypes.SHORT, SHORT,
            PandaTypes.INT, INT,
            PandaTypes.LONG, LONG,
            PandaTypes.FLOAT, FLOAT,
            PandaTypes.DOUBLE, DOUBLE
    );

    public int getPriority(ClassPrototype prototype) {
        return HIERARCHY.get(prototype);
    }

}
