/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.framework.language.interpreter.parser.generation;

import org.panda_lang.framework.design.interpreter.parser.generation.CycleType;
import org.panda_lang.utilities.commons.ReflectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class GenerationCycles {

    public static final String SYNTAX_LABEL = "SYNTAX";
    public static final CycleType SYNTAX = new CycleType(SYNTAX_LABEL, 1.0);

    public static final String PREPROCESSOR_LABEL = "PREPROCESSOR";
    public static final CycleType PREPROCESSOR = new CycleType(PREPROCESSOR_LABEL, 2.0);

    public static final String TYPES_LABEL = "TYPES";
    public static final CycleType TYPES = new CycleType(TYPES_LABEL, 2.0);

    public static final String DEFAULT_LABEL = "DEFAULT";
    public static final CycleType DEFAULT = new CycleType(DEFAULT_LABEL, 3.0);

    public static final String CONTENT_LABEL = "CONTENT";
    public static final CycleType CONTENT = new CycleType(CONTENT_LABEL, 4.0);

    private static final Collection<CycleType> VALUES;

    static {
        VALUES = ReflectionUtils.getStaticFieldValues(GenerationCycles.class, CycleType.class);
    }

    public static List<? extends CycleType> getValues() {
        return new ArrayList<>(VALUES);
    }

}
