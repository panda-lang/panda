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

package org.panda_lang.panda.framework.language.resource.parsers.expression.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.module.ModuleLoaderUtils;
import org.panda_lang.panda.framework.language.architecture.prototype.array.ArrayClassPrototype;
import org.panda_lang.panda.framework.language.architecture.prototype.array.ArrayClassPrototypeUtils;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.constructor.ConstructorUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionCategory;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.language.interpreter.token.PandaSnippet;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.DiffusedSource;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.framework.language.resource.parsers.common.ArgumentsParser;
import org.panda_lang.panda.framework.language.resource.parsers.expression.subparsers.callbacks.ArrayInstanceExpression;
import org.panda_lang.panda.framework.language.resource.parsers.expression.subparsers.callbacks.InstanceExpressionCallback;
import org.panda_lang.panda.framework.language.resource.syntax.auxiliary.Section;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

import java.util.Arrays;
import java.util.Optional;

public class ConstructorExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker() {
        return new ConstructorWorker();
    }

    @Override
    public ExpressionCategory getCategory() {
        return ExpressionCategory.STANDALONE;
    }

    @Override
    public String getSubparserName() {
        return "constructor";
    }

    private static class ConstructorWorker extends AbstractExpressionSubparserWorker {

        private static final ArgumentsParser ARGUMENT_PARSER = new ArgumentsParser();

        @Override
        public @Nullable ExpressionResult next(ExpressionContext context) {
            if (!context.getCurrentRepresentation().contentEquals(Keywords.NEW)) {
                return null;
            }

            DiffusedSource source = context.getDiffusedSource();
            source.backup();

            Snippet typeSource = new PandaSnippet(source.next());
            TokenRepresentation sectionRepresentation = source.next();

            if (sectionRepresentation.getType() != TokenType.SECTION) {
                source.restore();
                return null;
            }

            Section section = sectionRepresentation.toToken();

            if (!section.getSeparator().equals(Separators.PARENTHESIS_LEFT) && !section.getSeparator().equals(Separators.SQUARE_BRACKET_LEFT)) {
                source.restore();
                return null;
            }

            source.backup();
            ClassPrototype type = ModuleLoaderUtils.getReferenceOrThrow(context.getContext(), typeSource.asString(), typeSource).fetch();

            if (section.getSeparator().equals(Separators.PARENTHESIS_LEFT)) {
                return parseDefault(context, source, type, section.getContent());
            }

            return parseArray(context, source, type, section);
        }

        private ExpressionResult parseDefault(ExpressionContext context, DiffusedSource source, ClassPrototype type, Snippet argumentsSource) {
            Expression[] arguments = ARGUMENT_PARSER.parse(context.getContext(), argumentsSource);
            PrototypeConstructor constructor = ConstructorUtils.matchConstructor(type, arguments);

            if (constructor == null) {
                return ExpressionResult.error(type.getClassName() + " does not have constructor with the required parameters: " + Arrays.toString(arguments), argumentsSource);
            }

            return ExpressionResult.of(new InstanceExpressionCallback(type, constructor, arguments).toExpression());
        }

        private ExpressionResult parseArray(ExpressionContext context, DiffusedSource source, ClassPrototype type, Section capacitySourceSection) {
            Optional<ClassPrototypeReference> reference = ArrayClassPrototypeUtils.obtain(context.getContext(), type.getClassName() + "[]");

            if (!reference.isPresent()) {
                return ExpressionResult.error("Cannot fetch type: " + type.getClassName() + "[]", source.getLastReadSource());
            }

            Snippet capacitySource = capacitySourceSection.getContent();

            if (capacitySource.isEmpty()) {
                return ExpressionResult.error("Array requires specified capacity", capacitySourceSection.getOpeningSeparator());
            }

            Expression capacity = context.getParser().parse(context.getContext(), capacitySource);

            if (!PandaTypes.INT.isAssignableFrom(capacity.getReturnType())) {
                return ExpressionResult.error("Capacity has to be Int", capacitySource);
            }

            return ExpressionResult.of(new ArrayInstanceExpression((ArrayClassPrototype) reference.get().fetch(), capacity).toExpression());
        }

    }

}
