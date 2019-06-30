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

package org.panda_lang.panda.framework.language.interpreter.messenger.mappers;

import org.panda_lang.panda.framework.design.interpreter.messenger.formatters.MessengerDataMapper;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.ParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.ParserBootstrapUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.utilities.commons.ArrayUtils;
import org.panda_lang.panda.utilities.commons.StackTraceUtils;

import java.util.Arrays;

public final class StacktraceMapper implements MessengerDataMapper<StackTraceElement[], StackTraceElement[]> {

    private static final Class<?>[] IGNORED_CLASSES = ArrayUtils.mergeArrays(
            ParserBootstrapUtils.getInternalClasses(),
            ArrayUtils.of(PandaParserFailure.class, ParserBootstrap.class)
    );

    private static final String[] IGNORED = new String[] {
            "sun.reflect", "java.lang.reflect", "org.panda_lang.panda.utilities.inject",
            "ParserLayerGenerator", "PandaGeneration", "PandaInterpretation", "AssignationSubparserBootstrap"
    };

    @Override
    public StackTraceElement[] apply(StackTraceElement[] stackTraceElements) {
        StackTraceElement[] stacktrace = StackTraceUtils.filter(stackTraceElements, IGNORED_CLASSES);

        return Arrays.stream(stacktrace)
                .filter(element -> !ArrayUtils.findIn(IGNORED, value -> element.getClassName().contains(value)).isPresent())
                .toArray(StackTraceElement[]::new);
    }

    @Override
    public Class<StackTraceElement[]> getType() {
        return StackTraceElement[].class;
    }

}
