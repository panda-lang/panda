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

package org.panda_lang.panda.language.syntax.scope;

import org.panda_lang.framework.architecture.expression.Expression;
import org.panda_lang.framework.interpreter.parser.Context;
import org.panda_lang.framework.interpreter.parser.ContextParser;
import org.panda_lang.framework.interpreter.parser.pool.Targets;
import org.panda_lang.framework.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.syntax.PandaSourceReader;
import panda.utilities.ArrayUtils;
import org.panda_lang.framework.interpreter.parser.Component;
import panda.std.Completable;
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

        return Option.ofCompleted(statement);
    }

}
