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

package org.panda_lang.panda.language.interpreter.messenger.mappers;

import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.language.interpreter.messenger.MessengerDataMapper;
import org.panda_lang.panda.language.interpreter.parser.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.context.ParserBootstrapUtils;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.StackTraceUtils;

public final class StacktraceMapper implements MessengerDataMapper<StackTraceElement[], StackTraceElement[]> {

    public static final Class<?>[] IGNORED_CLASSES = ArrayUtils.mergeArrays(
            ParserBootstrapUtils.getInternalClasses(),
            ArrayUtils.of(PandaParserFailure.class, ParserBootstrap.class)
    );

    public static final String[] IGNORED = {
            "sun.reflect", "java.lang.reflect", "org.panda_lang.utilities.inject",
            "ParserLayerGenerator", "PandaGeneration", "PandaInterpretation", "AssignationSubparserBootstrap"
    };

    @Override
    public StackTraceElement[] apply(StackTraceElement[] stacktrace) {
        return StackTraceUtils.filter(StackTraceUtils.filter(stacktrace, IGNORED_CLASSES), IGNORED);
    }

    @Override
    public Class<StackTraceElement[]> getType() {
        return StackTraceElement[].class;
    }

}
