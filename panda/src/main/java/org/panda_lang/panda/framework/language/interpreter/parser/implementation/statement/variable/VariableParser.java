/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.variable;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.GenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.variable.parser.VarParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.variable.parser.VarParserData;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.variable.parser.VarParserResult;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.extractor.AbyssExtractor;

import java.util.List;

@ParserRegistration(target = PandaPipelines.STATEMENT, priority = PandaPriorities.STATEMENT_VARIABLE_PARSER)
public class VariableParser implements UnifiedParser, ParserHandler {

    @Override
    public boolean handle(TokenReader reader) {
        AbyssExtractor extractor = VarParser.PATTERN.extractor();
        List<TokenizedSource> hollows = extractor.extract(reader);
        return hollows != null && hollows.size() > 0;
    }

    @Override
    public boolean parse(ParserData data, GenerationLayer nextLayer) {
        CasualParserGeneration generation = data.getComponent(UniversalComponents.GENERATION);

        VarParser varParser = new VarParser();
        VarParserData parserData = varParser.toVarParserData(data, data.getComponent(UniversalComponents.SOURCE_STREAM));
        VarParserResult parserResult = varParser.parseVariable(parserData, data);

        if (parserResult.isFreshVariable()) {
            parserResult.getScope().addVariable(parserResult.getVariable());
        }

        if (!parserData.hasAssignation()) {
            return true;
        }

        nextLayer.delegate((delegatedData, delegatedNextLayer) -> varParser.parseAssignation(parserData, parserResult, delegatedData), data);
        return true;
    }

}
