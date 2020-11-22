/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.panda.language.resource.syntax.scope.branching;

import org.panda_lang.language.architecture.dynamic.ControlledScope;
import org.panda_lang.language.architecture.statement.ScopeUtils;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.ContextParser;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.SourceReader;
import org.panda_lang.language.interpreter.parser.pool.Targets;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.collection.Component;
import org.panda_lang.utilities.commons.function.Completable;
import org.panda_lang.utilities.commons.function.Option;

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
