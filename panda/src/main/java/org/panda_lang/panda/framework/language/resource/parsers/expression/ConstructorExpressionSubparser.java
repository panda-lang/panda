/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.panda.framework.language.resource.parsers.expression;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.module.ModuleLoaderUtils;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.ConstructorUtils;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.util.AbstractExpressionSubparserWorker;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.util.ContentProcessor;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.util.SeparatedContentReader;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.prototype.array.ArrayClassPrototype;
import org.panda_lang.panda.framework.language.architecture.prototype.array.ArrayClassPrototypeUtils;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.DiffusedSource;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.ArrayInstanceExpression;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.InstanceExpressionCallback;
import org.panda_lang.panda.framework.language.resource.parsers.general.ArgumentParser;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separator;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.utilities.commons.iterable.Loop;

import java.util.Arrays;
import java.util.Optional;

public class ConstructorExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker() {
        return new ConstructorWorker();
    }

    @Override
    public String getSubparserName() {
        return "constructor";
    }

    private static class ConstructorWorker extends AbstractExpressionSubparserWorker {

        private static final ArgumentParser ARGUMENT_PARSER = new ArgumentParser();

        @Override
        public @Nullable ExpressionResult<Expression> next(ExpressionContext context) {
            if (!context.getCurrentRepresentation().contentEquals(Keywords.NEW)) {
                return null;
            }

            DiffusedSource source = context.getDiffusedSource();
            TokenRepresentation current = source.getCurrent();
            source.backup();

            Loop.LoopResult result = Loop.of(source)
                    .loop((loop, representation) -> loop.breakLoop(TokenUtils.contentEquals(representation, Separators.PARENTHESIS_LEFT, Separators.SQUARE_BRACKET_LEFT)))
                    .run();

            if (!result.isBroke()) {
                source.restore();
                return ExpressionResult.error("Missing type", current);
            }

            Snippet typeSource = source.getLastReadSource().subSource(0, -1);
            ClassPrototype type = ModuleLoaderUtils.getReferenceOrThrow(context.getData(), typeSource.asString(), typeSource).fetch();

            TokenRepresentation separator = source.getCurrent();
            SeparatedContentReader argumentsReader = new SeparatedContentReader((Separator) separator.getToken(), ContentProcessor.NON_PROCESSING);
            argumentsReader.read(context, source);

            if (argumentsReader.getContent() == null) {
                return ExpressionResult.error("Broken arguments", separator);
            }

            if (separator.contentEquals(Separators.PARENTHESIS_LEFT)) {
                return parseDefault(context, source, type, argumentsReader.getContent());
            }

            return parseArray(context, source, type, argumentsReader.getContent());
        }

        private ExpressionResult<Expression> parseDefault(ExpressionContext context, DiffusedSource source, ClassPrototype type, Snippet argumentsSource) {
            Expression[] arguments = ARGUMENT_PARSER.parse(context.getData(), argumentsSource);
            PrototypeConstructor constructor = ConstructorUtils.matchConstructor(type, arguments);

            if (constructor == null) {
                return ExpressionResult.error(type.getClassName() + " does not have constructor with the required parameters: " + Arrays.toString(arguments), argumentsSource);
            }

            return ExpressionResult.of(new InstanceExpressionCallback(type, constructor, arguments).toExpression());
        }

        private ExpressionResult<Expression> parseArray(ExpressionContext context, DiffusedSource source, ClassPrototype type, Snippet capacitySource) {
            Optional<ClassPrototypeReference> reference = ArrayClassPrototypeUtils.obtain(context.getData(), type.getClassName() + "[]");

            if (!reference.isPresent()) {
                return ExpressionResult.error("Cannot fetch type: " + type.getClassName() + "[]", source.getLastReadSource());
            }

            if (capacitySource.isEmpty()) {
                return ExpressionResult.error("Array requires specified capacity", source.getLastReadSource());
            }

            Expression capacity = context.getParser().parse(context.getData(), capacitySource);

            if (!PandaTypes.INT.isAssignableFrom(capacity.getReturnType())) {
                return ExpressionResult.error("Capacity has to be Int", capacitySource);
            }

            return ExpressionResult.of(new ArrayInstanceExpression((ArrayClassPrototype) reference.get().fetch(), capacity).toExpression());
        }

    }

}
