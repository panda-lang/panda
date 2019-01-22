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

package org.panda_lang.panda.framework.language.resource.parsers.prototype.parameter;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.PandaScript;
import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.PandaParameter;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.Parameter;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.TokensUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;

import java.util.ArrayList;
import java.util.List;

public class ParameterParser implements Parser {

    public List<Parameter> parse(ParserData info, @Nullable Tokens tokens) {
        if (TokensUtils.isEmpty(tokens)) {
            return new ArrayList<>(0);
        }

        TokenRepresentation[] tokenRepresentations = tokens.toArray();
        List<Parameter> parameters = new ArrayList<>(tokenRepresentations.length / 3 + 1);

        if (tokens.size() == 0) {
            return parameters;
        }

        for (int i = 0; i < tokenRepresentations.length; i += 3) {
            TokenRepresentation parameterTypeRepresentation = tokenRepresentations[i];
            TokenRepresentation parameterNameRepresentation = tokenRepresentations[i + 1];

            String parameterType = parameterTypeRepresentation.getToken().getTokenValue();
            String parameterName = parameterNameRepresentation.getToken().getTokenValue();

            PandaScript script = info.getComponent(PandaComponents.PANDA_SCRIPT);
            ModuleLoader moduleLoader = script.getModuleLoader();
            ClassPrototypeReference type = moduleLoader.forClass(parameterType);

            if (type == null) {
                throw new PandaParserException("Unknown type '" + parameterType + "'");
            }

            Parameter parameter = new PandaParameter(type, parameterName);
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
