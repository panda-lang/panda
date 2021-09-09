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

package panda.interpreter.syntax.scope.block.looping;

import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.parser.Component;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.PandaParserException;
import panda.interpreter.parser.pool.Targets;
import panda.interpreter.resource.syntax.keyword.Keywords;
import panda.interpreter.syntax.PandaSourceReader;
import panda.interpreter.syntax.scope.block.BlockParser;
import panda.utilities.ArrayUtils;
import panda.std.Completable;
import panda.std.Option;

public final class LoopParser extends BlockParser<LoopBlock> {

    @Override
    public String name() {
        return "loop";
    }

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(Targets.SCOPE);
    }

    @Override
    public Option<Completable<LoopBlock>> parse(Context<?> context) {
        PandaSourceReader sourceReader = new PandaSourceReader(context.getStream());

        if (sourceReader.read(Keywords.LOOP).isEmpty()) {
            return Option.none();
        }

        Expression loopExpression = context.getExpressionParser().parse(context, context.getStream());

        if (!loopExpression.getKnownType().is("panda/panda@::Int")) {
            throw new PandaParserException("Loop requires number as an argument");
        }

        LoopBlock loopBlock = new LoopBlock(context.getScope(), context.getSource(), loopExpression);
        context.getScope().addStatement(loopBlock);
        SCOPE_PARSER.parse(context, loopBlock, sourceReader.readBody().get());

        return Option.withCompleted(loopBlock);
    }

}
