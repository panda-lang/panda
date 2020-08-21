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

package org.panda_lang.panda.language.resource.syntax.type;

import org.panda_lang.language.architecture.statement.Scope;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.LocalChannel;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.parser.pipeline.Handler;
import org.panda_lang.language.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.language.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.architecture.type.member.constructor.ConstructorScope;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.resource.syntax.TokenTypes;
import org.panda_lang.language.resource.syntax.literal.Literals;
import org.panda_lang.panda.language.interpreter.parser.autowired.AutowiredInitializer;
import org.panda_lang.panda.language.interpreter.parser.autowired.AutowiredParser;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Channel;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Ctx;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Src;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.ArgumentsParser;
import org.panda_lang.utilities.commons.ArrayUtils;

public final class SelfConstructorParser extends AutowiredParser<Void> {

    private static final ArgumentsParser ARGUMENTS_PARSER = new ArgumentsParser();

    @Override
    public PipelineComponent<? extends Parser>[] pipeline() {
        return ArrayUtils.of(Pipelines.SCOPE);
    }

    @Override
    protected AutowiredInitializer<Void> initialize(Context context, AutowiredInitializer<Void> initializer) {
        return initializer.linear("this args:(~)");
    }

    @Override
    protected Boolean customHandle(Handler handler, Context context, LocalChannel channel, Snippet source) {
        if (source.size() < 2) {
            return false;
        }

        if (!source.getFirst().contentEquals(Literals.THIS)) {
            return false;
        }

        return source.get(1).getType() == TokenTypes.SECTION;
    }

    @Autowired(order = 1)
    public void parse(Context context, @Ctx Scope parent, @Ctx Type type, @Channel Location location, @Src("args") Snippet args) {
        if (!(parent instanceof ConstructorScope)) {
            throw new PandaParserFailure(context, args, "Cannot use constructor call outside of the constructor");
        }

        type.getConstructors().getAdjustedConstructor(ARGUMENTS_PARSER.parse(context, args))
                .peek(constructor -> parent.addStatement(new SelfConstructor(location, constructor)))
                .onEmpty(() -> {
                    throw new PandaParserFailure(context, args, "Type does not contain constructor with requested parameter types");
                });
    }

}
