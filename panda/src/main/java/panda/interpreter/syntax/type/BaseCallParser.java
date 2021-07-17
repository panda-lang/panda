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

package panda.interpreter.syntax.type;

import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.architecture.statement.Scope;
import panda.interpreter.architecture.type.Type;
import panda.interpreter.architecture.type.TypeContext;
import panda.interpreter.architecture.type.member.constructor.BaseCall;
import panda.interpreter.architecture.type.member.constructor.ConstructorScope;
import panda.interpreter.architecture.type.member.constructor.TypeConstructor;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.ContextParser;
import panda.interpreter.parser.PandaParserFailure;
import panda.interpreter.parser.pool.Targets;
import panda.interpreter.token.Snippet;
import panda.interpreter.resource.syntax.keyword.Keywords;
import panda.interpreter.syntax.PandaSourceReader;
import panda.interpreter.syntax.expressions.subparsers.ArgumentsParser;
import panda.utilities.ArrayUtils;
import panda.interpreter.parser.Component;
import panda.std.Completable;
import panda.std.Option;

import java.util.List;

public final class BaseCallParser implements ContextParser<TypeContext, BaseCall> {

    private static final ArgumentsParser ARGUMENTS_PARSER = new ArgumentsParser();

    @Override
    public String name() {
        return "base";
    }

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(Targets.SCOPE);
    }

    @Override
    public Option<Completable<BaseCall>> parse(Context<? extends TypeContext> context) {
        PandaSourceReader sourceReader = new PandaSourceReader(context.getStream());

        if (sourceReader.read(Keywords.BASE).isEmpty()) {
            return Option.none();
        }

        Type type = context.getSubject().getType();
        Scope parent = context.getScope();

        if (!(parent instanceof ConstructorScope)) {
            throw new PandaParserFailure(context, context.getSource(), "Cannot use base constructor outside of the constructor");
        }

        if (!parent.getStatements().isEmpty()) {
            throw new PandaParserFailure(context, context.getSource(), "Base constructor has to be the first statement in the scope");
        }

        if (type.getSuperclass().isEmpty()) {
            throw new PandaParserFailure(context, context.getSource(), "Cannot use base call without superclass");
        }

        Option<Snippet> arguments = sourceReader.readArguments();

        if (arguments.isEmpty()) {
            throw new PandaParserFailure(context, context.getSource(), "Missing base arguments");
        }

        List<Expression> expressions = ARGUMENTS_PARSER.parse(context, arguments.get());
        Option<TypeConstructor> constructor = type.getSuperclass().get().fetchType().getConstructors().getConstructor(expressions);

        constructor.onEmpty(() -> {
            throw new PandaParserFailure(context, context.getSource(), "Base type does not contain constructor with the given parameters");
        });

        BaseCall baseCall = new BaseCall(context.toLocation(), constructor.get(), expressions);
        parent.addStatement(baseCall);

        return Option.ofCompleted(baseCall);
    }

}