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

package org.panda_lang.framework.language.interpreter.parser.stage;

import org.panda_lang.framework.design.interpreter.parser.stage.StageType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.panda_lang.utilities.commons.collection.Lists.add;

public final class Stages {

    private static final Collection<StageType> VALUES = new ArrayList<>();

    public static final String SYNTAX_LABEL = "SYNTAX";
    public static final StageType SYNTAX = add(VALUES, new StageType(SYNTAX_LABEL, 1.0));

    public static final String PREPROCESSOR_LABEL = "PREPROCESSOR";
    public static final StageType PREPROCESSOR = add(VALUES, new StageType(PREPROCESSOR_LABEL, 2.0));

    public static final String TYPES_LABEL = "TYPES";
    public static final StageType TYPES = add(VALUES, new StageType(TYPES_LABEL, 2.0));

    public static final String DEFAULT_LABEL = "DEFAULT";
    public static final StageType DEFAULT = add(VALUES, new StageType(DEFAULT_LABEL, 3.0));

    public static final String CONTENT_LABEL = "CONTENT";
    public static final StageType CONTENT = add(VALUES, new StageType(CONTENT_LABEL, 4.0));

    private Stages() { }

    public static List<? extends StageType> getValues() {
        return new ArrayList<>(VALUES);
    }

}
