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

package panda.interpreter.syntax;

import panda.interpreter.architecture.statement.Scope;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.Parser;
import panda.interpreter.parser.pool.PoolParser;
import panda.interpreter.parser.pool.PoolService;
import panda.interpreter.parser.pool.Targets;
import panda.interpreter.token.PandaSourceStream;
import panda.interpreter.token.Snippet;
import panda.interpreter.token.SourceStream;

public final class ScopeParser implements Parser {

    private final PoolParser<Object> scopePool;

    public ScopeParser(PoolService poolService) {
        this.scopePool = poolService.getPool(Targets.SCOPE).toParser();
    }

    @Override
    public String name() {
        return "scope";
    }

    public Scope parse(Context<?> context, Scope scope, Snippet body) {
        SourceStream stream = new PandaSourceStream(body);

        Context<?> delegatedContext = context.forkCreator()
                .withStream(stream)
                .withScope(scope)
                .toContext();

        scopePool.parse(delegatedContext, stream);
        return scope;
    }

}
