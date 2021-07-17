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

package org.panda_lang.panda.language.syntax.scope.block;

import org.panda_lang.framework.interpreter.parser.Context;
import org.panda_lang.framework.interpreter.parser.ContextParser;
import org.panda_lang.framework.interpreter.parser.pool.Targets;
import org.panda_lang.panda.language.syntax.ScopeParser;
import panda.utilities.ArrayUtils;
import org.panda_lang.framework.interpreter.parser.Component;

public abstract class BlockParser<R> implements ContextParser<Object, R> {

    protected static ScopeParser SCOPE_PARSER;

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(Targets.SCOPE);
    }

    @Override
    public void initialize(Context<?> context) {
        if (SCOPE_PARSER == null) {
            SCOPE_PARSER = new ScopeParser(context.getPoolService());
        }
    }

}
