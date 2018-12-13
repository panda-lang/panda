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
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.BootstrapParser;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.variable.parser.VarParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.variable.parser.VarParserData;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.variable.parser.VarParserResult;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserRegistration;

import java.util.List;

@ParserRegistration(target = PandaPipelines.SCOPE, priority = PandaPriorities.SCOPE_VARIABLE_PARSER)
public class VariableParser extends BootstrapParser {

    {
        parserBuilder = builder();
    }

    @Override
    public boolean handle(TokenReader reader) {
        List<Tokens> hollows = VarParser.PATTERN.extractor().extract(reader);
        return hollows != null && hollows.size() > 0;
    }

    @Autowired
    public boolean parse(ParserData data, LocalData localData, @Component SourceStream sourceStream) {
        VarParser varParser = localData.allocateInstance(new VarParser());
        VarParserData parserData = localData.allocateInstance(varParser.toVarParserData(data, sourceStream));
        VarParserResult parserResult = localData.allocateInstance(varParser.parseVariable(parserData, data));

        if (parserResult.isFreshVariable()) {
            parserResult.getScope().addVariable(parserResult.getVariable());
        }

        return true;
    }

    @Autowired(order = 1)
    public void parseAssignation(ParserData data, @Local VarParser varParser, @Local VarParserData parserData, @Local VarParserResult parserResult) {
        if (!parserData.hasAssignation()) {
            return;
        }

        varParser.parseAssignation(parserData, parserResult, data);
    }

}
