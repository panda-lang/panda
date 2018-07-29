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

package org.panda_lang.panda.framework.language.interpreter.parsers.statement.scope.branching;

import org.panda_lang.panda.framework.design.interpreter.parser.component.*;
import org.panda_lang.panda.framework.language.architecture.statement.PandaStatementData;
import org.panda_lang.panda.framework.design.architecture.statement.StatementData;
import org.panda_lang.panda.framework.design.architecture.statement.Container;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationAssistant;
import org.panda_lang.panda.framework.language.interpreter.parsers.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.framework.design.architecture.dynamic.branching.Continue;

@ParserRegistration(target = PandaPipelines.STATEMENT, parserClass = ContinueParser.class, handlerClass = ContinueParserHandler.class)
public class ContinueParser implements UnifiedParser {

    @Override
    public void parse(ParserData data) {
        CasualParserGenerationAssistant.delegateImmediately(data, new ContinueDeclarationCasualParserCallback());
    }

    @LocalCallback
    private static class ContinueDeclarationCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
            SourceStream stream = delegatedData.getComponent(UniversalComponents.SOURCE_STREAM);
            Container container = delegatedData.getComponent(PandaComponents.CONTAINER);

            Continue continueStatement = new Continue();
            container.addStatement(continueStatement);

            TokenRepresentation continueToken = stream.read();
            StatementData statementData = new PandaStatementData(continueToken.getLine());
            continueStatement.setStatementData(statementData);
        }

    }

}
