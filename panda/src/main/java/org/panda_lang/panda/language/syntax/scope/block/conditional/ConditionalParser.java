/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.panda.language.syntax.scope.block.conditional;

import org.panda_lang.framework.architecture.expression.Expression;
import org.panda_lang.framework.architecture.expression.PandaExpression;
import org.panda_lang.framework.architecture.statement.Scope;
import org.panda_lang.framework.architecture.statement.Statement;
import org.panda_lang.framework.interpreter.parser.Context;
import org.panda_lang.framework.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.interpreter.token.Snippet;
import org.panda_lang.framework.resource.syntax.keyword.Keyword;
import org.panda_lang.panda.language.syntax.PandaSourceReader;
import org.panda_lang.panda.language.syntax.ScopeParser;
import org.panda_lang.utilities.commons.collection.Lists;
import org.panda_lang.utilities.commons.function.Option;

final class ConditionalParser {

    Option<ConditionalBlock> parse(ScopeParser scopeParser, Context<?> context, boolean hasExpression, boolean listed, Keyword... keywords) {
        PandaSourceReader sourceReader = new PandaSourceReader(context.getStream());

        for (Keyword keyword : keywords) {
            if (sourceReader.read(keyword).isEmpty()) {
                return Option.none();
            }
        }

        Expression condition = hasExpression
                ? context.getExpressionParser().parse(context, context.getStream())
                : new PandaExpression(context.getTypeLoader().requireType("panda/panda@::Bool").getSignature(), true);

        Option<Snippet> body = sourceReader.readBody();

        if (body.isEmpty()) {
            throw new PandaParserFailure(context, "Missing condition body");
        }

        ConditionalBlock conditionalBlock = new ConditionalBlock(context.getScope(), context, condition);

        if (listed) {
            context.getScope().addStatement(conditionalBlock);
        }

        scopeParser.parse(context, conditionalBlock, body.get());
        return Option.of(conditionalBlock);
    }

    void linkBlocks(Context<?> context, ConditionalBlock conditionalBlock) {
        Scope scope = context.getScope();
        Statement previous = Lists.get(scope.getStatements(), scope.getStatements().size() - 1);

        if (!(previous instanceof ConditionalBlock)) {
            throw new PandaParserFailure(context, context.getSource(), "The Else-block without associated If-block");
        }

        ConditionalBlock previousConditionalBlock = (ConditionalBlock) previous;

        while (previousConditionalBlock.getElseBlock() != null) {
            previousConditionalBlock = previousConditionalBlock.getElseBlock();
        }

        previousConditionalBlock.setElseBlock(conditionalBlock);
    }

}
