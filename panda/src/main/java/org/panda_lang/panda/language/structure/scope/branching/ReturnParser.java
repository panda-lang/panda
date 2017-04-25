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

package org.panda_lang.panda.language.structure.scope.branching;

import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenHollowRedactor;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPattern;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPatternHollows;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPatternUtils;
import org.panda_lang.panda.core.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.core.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.core.interpreter.parser.util.Components;
import org.panda_lang.panda.core.structure.wrapper.Container;
import org.panda_lang.panda.core.structure.wrapper.StatementCell;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.ParserGeneration;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.ParserGenerationCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.ParserGenerationLayer;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.ParserGenerationType;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.language.PandaSyntax;
import org.panda_lang.panda.language.structure.scope.branching.statements.Return;
import org.panda_lang.panda.language.structure.general.expression.Expression;
import org.panda_lang.panda.language.structure.general.expression.ExpressionParser;

@ParserRegistration(target = DefaultPipelines.SCOPE, parserClass = ReturnParser.class, handlerClass = ReturnParserHandler.class)
public class ReturnParser implements UnifiedParser {

    private static final TokenPattern PATTERN = TokenPattern.builder().compile(PandaSyntax.getInstance(), "return +* ;").build();

    @Override
    public void parse(ParserInfo info) {
        ParserGeneration generation = info.getComponent(Components.GENERATION);

        generation.getLayer(ParserGenerationType.HIGHER)
                .delegateImmediately(new ReturnParserCallback(), info.fork());
    }

    @LocalCallback
    private static class ReturnParserCallback implements ParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {
            SourceStream stream = delegatedInfo.getComponent(Components.SOURCE_STREAM);
            Container container = delegatedInfo.getComponent("container");

            if (stream.getUnreadLength() == 2) {
                Return returnStatement = new Return(null);
                container.addStatement(returnStatement);
                stream.read();
                stream.read();
                return;
            }

            StatementCell cell = container.reserveCell();
            delegatedInfo.setComponent("return-cell", cell);

            TokenPatternHollows hollows = TokenPatternUtils.extract(PATTERN, delegatedInfo);
            TokenHollowRedactor redactor = new TokenHollowRedactor(hollows);

            redactor.map("return-expression");
            delegatedInfo.setComponent("redactor", redactor);

            nextLayer.delegate(new ReturnExpressionParserCallback(), delegatedInfo);
        }

    }

    @LocalCallback
    private static class ReturnExpressionParserCallback implements ParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {
            StatementCell cell = delegatedInfo.getComponent("return-cell");
            TokenHollowRedactor redactor = delegatedInfo.getComponent("redactor");

            TokenizedSource expressionSource = redactor.get("return-expression");
            ExpressionParser expressionParser = new ExpressionParser();

            Expression expression = expressionParser.parse(delegatedInfo, expressionSource);
            Return returnStatement = new Return(expression);

            cell.setStatement(returnStatement);
        }

    }

}
