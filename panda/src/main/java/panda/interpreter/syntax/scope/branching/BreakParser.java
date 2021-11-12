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

import panda.interpreter.architecture.dynamic.ControlledScope;
import panda.interpreter.architecture.statement.ScopeUtils;
import panda.interpreter.parser.Component;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.ContextParser;
import panda.interpreter.parser.PandaParserFailure;
import panda.interpreter.parser.SourceReader;
import panda.interpreter.parser.pool.Targets;
import panda.interpreter.resource.syntax.keyword.Keywords;
import panda.utilities.ArrayUtils;
import panda.std.reactive.Completable;
import panda.std.Option;

public final class BreakParser implements ContextParser<Object, Break> {

    @Override
    public String name() {
        return "break";
    }

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(Targets.SCOPE);
    }

    @Override
    public Option<Completable<Break>> parse(Context<?> context) {
        return new SourceReader(context.getStream()).read(Keywords.BREAK)
                .peek(token -> {
                    if (!ScopeUtils.lookFor(context.getScope(), ControlledScope.class)) {
                        throw new PandaParserFailure(context, token, "Break cannot be used outside of the looping block");
                    }
                })
                .map(token -> new Break(token.getLocation()))
                .peek(statement -> context.getScope().addStatement(statement))
                .map(Completable::completed);
    }

}
