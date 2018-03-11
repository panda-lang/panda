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

import org.panda_lang.panda.framework.design.architecture.wrapper.Container;
import org.panda_lang.panda.framework.design.architecture.wrapper.StatementCell;
import org.panda_lang.panda.design.interpreter.parser.generation.CasualParserGenerationAssistant;
import org.panda_lang.panda.design.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.design.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.design.interpreter.parser.util.Components;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternAssistant;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.framework.design.runtime.Expression;
import org.panda_lang.panda.language.structure.general.expression.ExpressionParser;
import org.panda_lang.panda.language.structure.scope.branching.statements.Return;
import org.panda_lang.panda.language.syntax.PandaSyntax;

@ParserRegistration(target = DefaultPipelines.SCOPE, parserClass = ReturnParser.class, handlerClass = ReturnParserHandler.class)
public class ReturnParser implements UnifiedParser {

    private static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "return +* ;")
            .build();

    @Override
    public void parse(ParserInfo info) {
        CasualParserGenerationAssistant.delegateImmediately(info, new ReturnCasualParserCallback());
    }

    @LocalCallback
    private static class ReturnCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
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

            AbyssRedactor redactor = AbyssPatternAssistant.traditionalMapping(PATTERN, delegatedInfo, "return-expression");
            delegatedInfo.setComponent("redactor", redactor);

            nextLayer.delegate(new ReturnExpressionCasualParserCallback(), delegatedInfo);
        }

    }

    @LocalCallback
    private static class ReturnExpressionCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            StatementCell cell = delegatedInfo.getComponent("return-cell");
            AbyssRedactor redactor = delegatedInfo.getComponent("redactor");

            TokenizedSource expressionSource = redactor.get("return-expression");
            ExpressionParser expressionParser = new ExpressionParser();

            Expression expression = expressionParser.parse(delegatedInfo, expressionSource);
            Return returnStatement = new Return(expression);

            cell.setStatement(returnStatement);
        }

    }

}
