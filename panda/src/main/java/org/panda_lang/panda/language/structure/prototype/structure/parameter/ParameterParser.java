/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.language.structure.prototype.structure.parameter;

import org.panda_lang.panda.core.interpreter.parser.util.Components;
import org.panda_lang.panda.core.structure.PandaScript;
import org.panda_lang.panda.framework.implementation.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.Parser;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.token.Token;
import org.panda_lang.panda.framework.language.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.language.interpreter.token.TokenType;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;
import org.panda_lang.panda.language.structure.imports.ImportRegistry;
import org.panda_lang.panda.language.structure.prototype.ClassPrototype;

import java.util.ArrayList;
import java.util.List;

public class ParameterParser implements Parser {

    public List<Parameter> parse(ParserInfo info, TokenizedSource tokenizedSource) {
        TokenRepresentation[] tokenRepresentations = tokenizedSource.toArray();
        List<Parameter> parameters = new ArrayList<>(tokenRepresentations.length / 3 + 1);

        for (int i = 0; i < tokenRepresentations.length; i += 3) {
            TokenRepresentation parameterTypeRepresentation = tokenRepresentations[i];
            TokenRepresentation parameterNameRepresentation = tokenRepresentations[i + 1];

            String parameterType = parameterTypeRepresentation.getToken().getTokenValue();
            String parameterName = parameterNameRepresentation.getToken().getTokenValue();

            PandaScript script = info.getComponent(Components.SCRIPT);
            ImportRegistry importRegistry = script.getImportRegistry();
            ClassPrototype type = importRegistry.forClass(parameterType);

            if (type == null) {
                throw new PandaParserException("Unknown type '" + parameterType + "'");
            }

            Parameter parameter = new Parameter(type, parameterName);
            parameters.add(parameter);

            if (i + 2 < tokenRepresentations.length) {
                TokenRepresentation separatorRepresentation = tokenRepresentations[i + 2];
                Token separator = separatorRepresentation.getToken();

                if (separator.getType() != TokenType.SEPARATOR) {
                    throw new PandaParserException("Unexpected token " + separatorRepresentation);
                }
            }
        }

        return parameters;
    }

}
