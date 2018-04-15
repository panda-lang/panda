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

package org.panda_lang.panda.language.structure.scope.branching;

import org.panda_lang.panda.framework.language.architecture.statement.PandaStatementData;
import org.panda_lang.panda.framework.design.architecture.statement.StatementData;
import org.panda_lang.panda.framework.design.architecture.statement.Container;
import org.panda_lang.panda.design.interpreter.parser.generation.CasualParserGenerationAssistant;
import org.panda_lang.panda.design.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.design.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.design.architecture.statement.Break;

@ParserRegistration(target = DefaultPipelines.STATEMENT, parserClass = BreakParser.class, handlerClass = BreakParserHandler.class)
public class BreakParser implements UnifiedParser {

    @Override
    public void parse(ParserInfo info) {
        CasualParserGenerationAssistant.delegateImmediately(info, new BreakDeclarationCasualParserCallback());
    }

    @LocalCallback
    private static class BreakDeclarationCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            SourceStream stream = delegatedInfo.getComponent(PandaComponents.SOURCE_STREAM);
            Container container = delegatedInfo.getComponent("container");

            Break breakStatement = new Break();
            container.addStatement(breakStatement);

            TokenRepresentation breakToken = stream.read();
            StatementData statementData = new PandaStatementData(breakToken.getLine());
            breakStatement.setStatementData(statementData);
        }

    }

}
