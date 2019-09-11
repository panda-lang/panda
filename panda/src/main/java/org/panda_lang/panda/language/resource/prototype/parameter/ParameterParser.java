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

package org.panda_lang.panda.language.resource.prototype.parameter;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.PrototypeParameter;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.SnippetUtils;
import org.panda_lang.panda.language.architecture.module.ModuleLoaderUtils;
import org.panda_lang.panda.language.architecture.prototype.standard.parameter.PandaParameter;
import org.panda_lang.panda.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.utilities.commons.ArrayUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParameterParser implements Parser {

    public List<PrototypeParameter> parse(Context context, @Nullable Snippet snippet) {
        if (SnippetUtils.isEmpty(snippet)) {
            return Collections.emptyList();
        }

        Snippet[] parametersSource = snippet.split(Separators.COMMA);
        List<PrototypeParameter> parameters = new ArrayList<>(parametersSource.length);

        if (ArrayUtils.isEmpty(parametersSource)) {
            return parameters;
        }

        for (int index = 0; index < parametersSource.length; index++) {
            Snippet source = parametersSource[index];
            Token name = source.getLast();

            if (name == null) {
                throw PandaParserFailure.builder("Missing parameter at " + index + " position", context)
                        .withStreamOrigin(snippet)
                        .build();
            }

            int end = source.size() - 1;

            if (source.contains(Separators.PERIOD)) {
                end -= 3;
            }

            ClassPrototypeReference reference = ModuleLoaderUtils.getReferenceOrThrow(context, source.subSource(0, end).asSource(), source);
            boolean varargs = end + 1 < source.size();

            if (varargs) {
                reference = reference.toArray(context.getComponent(UniversalComponents.MODULE_LOADER));
            }

            PrototypeParameter parameter = new PandaParameter(index, reference, name.getValue(), varargs, false);
            parameters.add(parameter);
        }

        return parameters;
    }

}
