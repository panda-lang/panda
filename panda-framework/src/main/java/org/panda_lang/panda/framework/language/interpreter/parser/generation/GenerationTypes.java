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

package org.panda_lang.panda.framework.language.interpreter.parser.generation;

import org.panda_lang.panda.framework.design.interpreter.parser.generation.PipelineType;
import org.panda_lang.panda.utilities.commons.ReflectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GenerationTypes {

    public static final String RAW_SYNTAX_LABEL = "RAW_SYNTAX";
    public static final PipelineType RAW_SYNTAX = new PipelineType(RAW_SYNTAX_LABEL, 1.0);

    public static final String PREPROCESSOR_LABEL = "PREPROCESSOR";
    public static final PipelineType PREPROCESSOR = new PipelineType(PREPROCESSOR_LABEL, 2.0);

    public static final String TYPES_LABEL = "TYPES";
    public static final PipelineType TYPES = new PipelineType(TYPES_LABEL, 2.0);

    public static final String SYNTAX_LABEL = "SYNTAX";
    public static final PipelineType SYNTAX = new PipelineType(SYNTAX_LABEL, 3.0);

    public static final String CONTENT_LABEL = "CONTENT";
    public static final PipelineType CONTENT = new PipelineType(CONTENT_LABEL, 4.0);

    private static final Collection<PipelineType> VALUES;

    static {
        VALUES = ReflectionUtils.getStaticFieldValues(GenerationTypes.class, PipelineType.class);
    }

    public static List<? extends PipelineType> getValues() {
        return new ArrayList<>(VALUES);
    }

}
