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

package panda.interpreter.syntax.scope.branching;

import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.parser.Component;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.ContextParser;
import panda.interpreter.parser.SourceReader;
import panda.interpreter.parser.pool.Targets;
import panda.interpreter.resource.syntax.keyword.Keywords;
import panda.utilities.ArrayUtils;
import panda.std.Completable;
import panda.std.Option;

public final class ThrowParser implements ContextParser<Object, Throw> {

    @Override
    public String name() {
        return "throw";
    }

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(Targets.SCOPE);
    }

    @Override
    public Option<Completable<Throw>> parse(Context<?> context) {
        SourceReader sourceReader = new SourceReader(context.getStream());

        if (sourceReader.read(Keywords.THROW).isEmpty()) {
            return Option.none();
        }

        Expression throwValue = context.getExpressionParser().parse(context, context.getStream());
        Throw statement = new Throw(context.getSource().getLocation(), throwValue);
        context.getScope().addStatement(statement);

        return Option.withCompleted(statement);
    }

}
