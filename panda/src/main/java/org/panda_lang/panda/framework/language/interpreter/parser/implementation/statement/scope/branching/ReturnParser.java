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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.scope.branching;

import org.panda_lang.panda.framework.design.architecture.statement.Container;
import org.panda_lang.panda.framework.design.architecture.statement.StatementCell;
import org.panda_lang.panda.framework.design.architecture.statement.StatementData;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.GenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.dynamic.branching.Return;
import org.panda_lang.panda.framework.language.architecture.statement.PandaStatementData;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternAssistant;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternBuilder;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

@ParserRegistration(target = PandaPipelines.STATEMENT)
public class ReturnParser implements UnifiedParser, ParserHandler {

    private static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "return +*")
            .build();

    @Override
    public boolean handle(TokenReader reader) {
        return TokenUtils.equals(reader.read(), Keywords.RETURN);
    }

    @Override
    public boolean parse(ParserData data, GenerationLayer nextLayer) {
        SourceStream stream = data.getComponent(UniversalComponents.SOURCE_STREAM);
        Container container = data.getComponent(PandaComponents.CONTAINER);

        if (stream.getUnreadLength() == 1) {
            Return returnStatement = new Return(null);
            container.addStatement(returnStatement);

            TokenRepresentation returnToken = stream.read();
            StatementData statementData = new PandaStatementData(returnToken.getLine());
            returnStatement.setStatementData(statementData);

            return true;
        }

        StatementCell cell = container.reserveCell();
        AbyssRedactor redactor = AbyssPatternAssistant.traditionalMapping(PATTERN, data, "return-expression");

        nextLayer.delegate(new ReturnExpressionCasualParserCallback(cell, redactor), data);
        return true;
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
        public void call(ParserData delegatedData, GenerationLayer nextLayer) {
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
