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

package org.panda_lang.panda.language.resource.syntax.type;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.module.Imports;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.member.parameter.PropertyParameter;
import org.panda_lang.language.architecture.type.member.parameter.PropertyParameterImpl;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.SnippetUtils;
import org.panda_lang.language.interpreter.token.Token;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.language.resource.syntax.separator.Separators;
import org.panda_lang.utilities.commons.ArrayUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ParameterParser implements Parser {

    @Override
    public String name() {
        return "parameter";
    }

    public List<PropertyParameter> parse(Context<?> context, @Nullable Snippet snippet) {
        if (SnippetUtils.isEmpty(snippet)) {
            return Collections.emptyList();
        }

        Snippet[] parametersSource = snippet.split(Separators.COMMA);
        List<PropertyParameter> parameters = new ArrayList<>(parametersSource.length);

        if (ArrayUtils.isEmpty(parametersSource)) {
            return parameters;
        }

        Imports imports = context.getImports();

        for (int index = 0; index < parametersSource.length; index++) {
            Snippet source = parametersSource[index];
            Token name = source.getLast();

            if (name == null) {
                throw new PandaParserFailure(context, snippet, "Missing parameter at " + index + " position");
            }

            int start = 0;
            int end = source.size() - 1;

            boolean mutable = source.contains(Keywords.MUT);
            boolean nillable = source.contains(Keywords.NIL);
            boolean varargs = source.contains(Separators.VARARGS);

            start += mutable ? 1 : 0;
            start += nillable ? 1 : 0;
            end -= varargs ? 1 : 0;
            Snippet typeSource = source.subSource(start, end);

            Type type = imports.forName(typeSource.asSource()).orThrow(() -> {
                throw new PandaParserFailure(context, typeSource, "Unknown type", "Make sure that type is imported");
            });

            if (varargs) {
                type = type.toArray(context.getTypeLoader());
            }

            PropertyParameter parameter = new PropertyParameterImpl(index, type, name.getValue(), varargs, mutable, nillable);
            parameters.add(parameter);
        }

        return parameters;
    }

}
