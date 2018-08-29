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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.scope.block.conditional;

import org.panda_lang.panda.framework.design.architecture.dynamic.Block;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.GenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.dynamic.block.conditional.ConditionalBlock;
import org.panda_lang.panda.framework.language.architecture.dynamic.block.conditional.ElseBlock;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.scope.block.BlockComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternAssistant;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternBuilder;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

@ParserRegistration(target = PandaPipelines.BLOCK)
public class ConditionalBlockParser implements UnifiedParser, ParserHandler {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "+* ( +* )")
            .build();

    @Override
    public boolean handle(TokenReader reader) {
        TokenRepresentation representation = reader.read();
        return TokenUtils.equals(representation, Keywords.IF) || TokenUtils.equals(representation, Keywords.ELSE);
    }

    @Override
    public boolean parse(ParserData data, GenerationLayer nextLayer) {
        SourceStream stream = data.getComponent(UniversalComponents.SOURCE_STREAM);
        ParserData parentData = data.getComponent(UniversalComponents.PARENT_DATA);

        if (stream.getUnreadLength() == 1) {
            ElseBlock elseBlock = new ElseBlock();
            Block previousBlock = parentData.getComponent(BlockComponents.PREVIOUS_BLOCK);

            if (!(previousBlock instanceof ConditionalBlock)) {
                throw new PandaParserFailure("The Else-block without associated If-block at line", data);
            }

            ConditionalBlock conditionalBlock = (ConditionalBlock) previousBlock;
            conditionalBlock.setElseBlock(elseBlock);

            data.setComponent(BlockComponents.BLOCK, elseBlock);
            data.setComponent(BlockComponents.UNLISTED_BLOCK, true);
            return true;
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
                Block previousBlock = parentData.getComponent(BlockComponents.PREVIOUS_BLOCK);

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

        return true;
    }

}
