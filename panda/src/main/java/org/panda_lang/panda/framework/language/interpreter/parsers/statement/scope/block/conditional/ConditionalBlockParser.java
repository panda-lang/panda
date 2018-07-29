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

package org.panda_lang.panda.framework.language.interpreter.parsers.statement.scope.block.conditional;

import org.panda_lang.panda.framework.design.architecture.dynamic.block.conditional.*;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.token.*;
import org.panda_lang.panda.framework.design.architecture.dynamic.*;
import org.panda_lang.panda.framework.design.interpreter.parser.*;
import org.panda_lang.panda.framework.design.interpreter.parser.component.*;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.*;
import org.panda_lang.panda.framework.design.runtime.expression.*;
import org.panda_lang.panda.framework.language.interpreter.parser.*;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.*;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.*;
import org.panda_lang.panda.framework.language.interpreter.token.utils.*;
import org.panda_lang.panda.framework.language.interpreter.*;
import org.panda_lang.panda.framework.language.interpreter.parsers.*;
import org.panda_lang.panda.framework.language.interpreter.parsers.general.expression.*;
import org.panda_lang.panda.framework.language.interpreter.parsers.statement.scope.block.*;

@ParserRegistration(target = PandaPipelines.BLOCK, parserClass = ConditionalBlockParser.class, handlerClass = ConditionalBlockParserHandler.class)
public class ConditionalBlockParser implements UnifiedParser {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "+* ( +* )")
            .build();

    @Override
    public void parse(ParserData data) {
        SourceStream stream = data.getComponent(UniversalComponents.SOURCE_STREAM);
        ParserData parentInfo = data.getComponent(UniversalComponents.PARENT_DATA);

        if (stream.getUnreadLength() == 1) {
            ElseBlock elseBlock = new ElseBlock();
            Block previousBlock = parentInfo.getComponent(BlockComponents.PREVIOUS_BLOCK);

            if (!(previousBlock instanceof ConditionalBlock)) {
                throw new PandaParserException("The Else-block without associated If-block at line " + TokenUtils.getLine(stream.toTokenizedSource()));
            }

            ConditionalBlock conditionalBlock = (ConditionalBlock) previousBlock;
            conditionalBlock.setElseBlock(elseBlock);

            data.setComponent(BlockComponents.BLOCK, elseBlock);
            data.setComponent(BlockComponents.UNLISTED_BLOCK, true);
            return;
        }

        AbyssRedactor redactor = AbyssPatternAssistant.traditionalMapping(PATTERN, data, "condition-type", "condition-expression");
        TokenizedSource conditionType = redactor.get("condition-type");
        TokenizedSource conditionExpression = redactor.get("condition-expression");

        ExpressionParser expressionParser = new ExpressionParser();
        Expression expression = expressionParser.parse(data, conditionExpression);

        ConditionalBlock conditionalBlock = new ConditionalBlock(expression);
        data.setComponent(BlockComponents.BLOCK, conditionalBlock);

        switch (conditionType.asString()) {
            case "if":
                break;
            case "else if":
                Block previousBlock = parentInfo.getComponent(BlockComponents.PREVIOUS_BLOCK);

                if (!(previousBlock instanceof ConditionalBlock)) {
                    throw new PandaParserException("The If-Else-block without associated If-block at line " + TokenUtils.getLine(stream.toTokenizedSource()));
                }

                ConditionalBlock previousConditionalBlock = (ConditionalBlock) previousBlock;
                conditionalBlock.setElseBlock(previousConditionalBlock);
                data.setComponent(BlockComponents.UNLISTED_BLOCK, true);
                break;
            default:
                throw new PandaParserException("Unrecognized condition type at line " + TokenUtils.getLine(conditionType));
        }
    }

}
