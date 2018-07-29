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
import org.panda_lang.panda.framework.design.architecture.statement.StatementCell;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationAssistant;
import org.panda_lang.panda.framework.language.interpreter.parsers.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.token.AbyssPatternAssistant;
import org.panda_lang.panda.framework.design.interpreter.token.AbyssPatternBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parsers.general.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.architecture.dynamic.branching.Return;
import org.panda_lang.panda.framework.language.interpreter.PandaSyntax;

@ParserRegistration(target = PandaPipelines.STATEMENT, parserClass = ReturnParser.class, handlerClass = ReturnParserHandler.class)
public class ReturnParser implements UnifiedParser {

    private static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "return +*")
            .build();

    @Override
    public void parse(ParserData data) {
        CasualParserGenerationAssistant.delegateImmediately(data, new ReturnCasualParserCallback());
    }

    @LocalCallback
    private static class ReturnCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
            SourceStream stream = delegatedData.getComponent(UniversalComponents.SOURCE_STREAM);
            Container container = delegatedData.getComponent(PandaComponents.CONTAINER);

            if (stream.getUnreadLength() == 1) {
                Return returnStatement = new Return(null);
                container.addStatement(returnStatement);

                TokenRepresentation returnToken = stream.read();
                StatementData statementData = new PandaStatementData(returnToken.getLine());
                returnStatement.setStatementData(statementData);

                return;
            }

            StatementCell cell = container.reserveCell();
            AbyssRedactor redactor = AbyssPatternAssistant.traditionalMapping(PATTERN, delegatedData, "return-expression");

            nextLayer.delegate(new ReturnExpressionCasualParserCallback(cell, redactor), delegatedData);
        }

    }

    @LocalCallback
    private static class ReturnExpressionCasualParserCallback implements CasualParserGenerationCallback {

        private final StatementCell cell;
        private final AbyssRedactor redactor;

        public ReturnExpressionCasualParserCallback(StatementCell cell, AbyssRedactor redactor) {
            this.cell = cell;
            this.redactor = redactor;
        }

        @Override
        public void call(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
            TokenizedSource expressionSource = redactor.get("return-expression");
            ExpressionParser expressionParser = new ExpressionParser();
            Expression expression = expressionParser.parse(delegatedData, expressionSource);

            Return returnStatement = new Return(expression);
            cell.setStatement(returnStatement);

            StatementData statementData = new PandaStatementData(expressionSource.getFirst().getLine());
            returnStatement.setStatementData(statementData);
        }

    }

}
