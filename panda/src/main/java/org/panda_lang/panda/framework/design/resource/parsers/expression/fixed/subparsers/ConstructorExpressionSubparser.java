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

package org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.module.ModuleLoaderUtils;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.ConstructorUtils;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionContext;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionResult;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.util.AbstractExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.util.ContentProcessor;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.util.SeparatedContentReader;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.DiffusedSource;
import org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.callbacks.InstanceExpressionCallback;
import org.panda_lang.panda.framework.language.resource.parsers.general.ArgumentParser;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.utilities.commons.iterable.Loop;

import java.util.Arrays;

public class ConstructorExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker() {
        return new ConstructorWorker();
    }

    private static class ConstructorWorker extends AbstractExpressionSubparserWorker {

        private static final ArgumentParser ARGUMENT_PARSER = new ArgumentParser();

        @Override
        public @Nullable ExpressionResult<Expression> next(ExpressionContext context) {
            if (!context.getCurrent().contentEquals(Keywords.NEW)) {
                return null;
            }

            DiffusedSource source = context.getDiffusedSource();
            TokenRepresentation current = source.getCurrent();
            source.backup();

            Loop.LoopResult result = Loop.of(source)
                    .loop((loop, representation) -> loop.breakLoop(representation.contentEquals(Separators.PARENTHESIS_LEFT)))
                    .run();

            if (!result.isBroke()) {
                source.restore();
                return ExpressionResult.error("Missing type", current);
            }

            Snippet typeSource = source.getLastReadSource();
            TokenRepresentation separator = source.getCurrent();

            SeparatedContentReader argumentsReader = new SeparatedContentReader(Separators.PARENTHESIS_LEFT, ContentProcessor.NON_PROCESSING);
            argumentsReader.read(context, source);

            if (!argumentsReader.hasContent()) {
                return ExpressionResult.error("Broken arguments", separator);
            }

            ClassPrototypeReference type = ModuleLoaderUtils.getReferenceOrThrow(context.getData(), typeSource.asString(), typeSource);
            Expression[] arguments = ARGUMENT_PARSER.parse(context.getData(), argumentsReader.getContent());
            PrototypeConstructor constructor = ConstructorUtils.matchConstructor(type.fetch(), arguments);

            if (constructor == null) {
                return ExpressionResult.error(type.getClassName() + " does not have constructor with the required parameters: " + Arrays.toString(arguments), typeSource);
            }

            return ExpressionResult.of(new InstanceExpressionCallback(type.fetch(), constructor, arguments).toExpression());
        }

    }

}
