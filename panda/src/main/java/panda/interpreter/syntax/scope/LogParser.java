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

package panda.interpreter.syntax.scope;

import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.ContextParser;
import panda.interpreter.parser.pool.Targets;
import panda.interpreter.resource.syntax.keyword.Keywords;
import panda.interpreter.syntax.PandaSourceReader;
import panda.utilities.ArrayUtils;
import panda.interpreter.parser.Component;
import panda.std.reactive.Completable;
import panda.std.Option;

import java.util.List;

public final class LogParser implements ContextParser<Object, LogStatement> {

    @Override
    public String name() {
        return "log";
    }

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(Targets.SCOPE);
    }

    @Override
    public Option<Completable<LogStatement>> parse(Context<?> context) {
        PandaSourceReader sourceReader = new PandaSourceReader(context.getStream());

        if (sourceReader.read(Keywords.LOG).isEmpty()) {
            return Option.none();
        }

        List<Expression> expressions = sourceReader.readExpressions(context);
        LogStatement statement = new LogStatement(context.getSource(), context.getLogger(), expressions);
        context.getScope().addStatement(statement);

        return Option.withCompleted(statement);
    }

}
