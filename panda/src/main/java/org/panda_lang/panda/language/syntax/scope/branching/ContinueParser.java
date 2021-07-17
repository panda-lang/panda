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

package org.panda_lang.panda.language.syntax.scope.branching;

import org.panda_lang.framework.architecture.dynamic.ControlledScope;
import org.panda_lang.framework.architecture.statement.ScopeUtils;
import org.panda_lang.framework.interpreter.parser.Component;
import org.panda_lang.framework.interpreter.parser.Context;
import org.panda_lang.framework.interpreter.parser.ContextParser;
import org.panda_lang.framework.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.interpreter.parser.SourceReader;
import org.panda_lang.framework.interpreter.parser.pool.Targets;
import org.panda_lang.framework.resource.syntax.keyword.Keywords;
import panda.utilities.ArrayUtils;
import panda.std.Completable;
import panda.std.Option;

public final class ContinueParser implements ContextParser<Object, Continue> {

    @Override
    public String name() {
        return "continue";
    }

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(Targets.SCOPE);
    }

    @Override
    public Option<Completable<Continue>> parse(Context<?> context) {
        return new SourceReader(context.getStream()).read(Keywords.CONTINUE)
                .peek(token -> {
                    if (!ScopeUtils.lookFor(context.getScope(), ControlledScope.class)) {
                        throw new PandaParserFailure(context, token, "Continue cannot be used outside of the looping block");
                    }
                })
                .map(token -> new Continue(token.getLocation()))
                .peek(statement -> context.getScope().addStatement(statement))
                .map(Completable::completed);
    }

}
