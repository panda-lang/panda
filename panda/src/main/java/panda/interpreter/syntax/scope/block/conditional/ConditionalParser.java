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

package panda.interpreter.syntax.scope.block.conditional;

import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.architecture.expression.PandaExpression;
import panda.interpreter.architecture.statement.Scope;
import panda.interpreter.architecture.statement.Statement;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.PandaParserFailure;
import panda.interpreter.token.Snippet;
import panda.interpreter.resource.syntax.keyword.Keyword;
import panda.interpreter.syntax.PandaSourceReader;
import panda.interpreter.syntax.ScopeParser;
import panda.utilities.collection.Lists;
import panda.std.Option;

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
