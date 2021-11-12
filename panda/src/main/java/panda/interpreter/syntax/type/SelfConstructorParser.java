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
import panda.interpreter.architecture.type.Type;
import panda.interpreter.architecture.type.TypeContext;
import panda.interpreter.architecture.type.member.constructor.ConstructorScope;
import panda.interpreter.parser.Component;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.ContextParser;
import panda.interpreter.parser.PandaParserFailure;
import panda.interpreter.parser.pool.Targets;
import panda.interpreter.resource.syntax.literal.Literals;
import panda.interpreter.syntax.PandaSourceReader;
import panda.interpreter.syntax.expressions.subparsers.ArgumentsParser;
import panda.utilities.ArrayUtils;
import panda.std.reactive.Completable;
import panda.std.Option;

import java.util.List;

public final class SelfConstructorParser implements ContextParser<TypeContext, SelfConstructor> {

    private static final ArgumentsParser ARGUMENTS_PARSER = new ArgumentsParser();

    @Override
    public String name() {
        return "self";
    }

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(Targets.SCOPE);
    }

    @Override
    public Option<Completable<SelfConstructor>> parse(Context<? extends TypeContext> context) {
        PandaSourceReader sourceReader = new PandaSourceReader(context.getStream());

        if (sourceReader.read(Literals.THIS).isEmpty()) {
            return Option.none();
        }

        Option<List<Expression>> arguments = sourceReader.readArguments()
                .map(source -> ARGUMENTS_PARSER.parse(context, source));

        if (arguments.isEmpty()) {
            return Option.none();
        }

        if (!(context.getScope() instanceof ConstructorScope)) {
            throw new PandaParserFailure(context, "Cannot use constructor call outside of the constructor");
        }

        Type type = context.getSubject().getType();

        return type.getConstructors().getConstructor(arguments.get())
                .map(constructor -> context.getScope().addStatement(new SelfConstructor(context, constructor, arguments.get())))
                .map(Completable::completed)
                .onEmpty(() -> {
                    throw new PandaParserFailure(context, sourceReader, "Type does not contain constructor with requested parameter types");
                });
    }

}
