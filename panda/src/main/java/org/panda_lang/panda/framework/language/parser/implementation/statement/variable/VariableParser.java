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

package org.panda_lang.panda.framework.language.parser.implementation.statement.variable;

import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.parser.*;
import org.panda_lang.panda.framework.design.interpreter.parser.component.*;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.*;
import org.panda_lang.panda.framework.language.parser.implementation.statement.variable.parser.*;

@ParserRegistration(target = PandaPipelines.STATEMENT, parserClass = VariableParser.class, handlerClass = VariableParserHandler.class, priority = PandaPriorities.STATEMENT_VARIABLE_PARSER)
public class VariableParser implements UnifiedParser {

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
