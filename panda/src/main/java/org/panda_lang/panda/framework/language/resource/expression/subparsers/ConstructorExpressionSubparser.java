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

package org.panda_lang.panda.framework.language.resource.expression.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.Arguments;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.AbstractExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.module.ModuleLoaderUtils;
import org.panda_lang.panda.framework.language.architecture.prototype.array.ArrayClassPrototype;
import org.panda_lang.panda.framework.language.architecture.prototype.array.ArrayClassPrototypeUtils;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.parameter.ParametrizedExpression;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionCategory;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.language.interpreter.token.PandaSnippet;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.DiffusedSource;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.framework.language.resource.prototype.parameter.ArgumentsParser;
import org.panda_lang.panda.framework.language.resource.syntax.auxiliary.Section;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

import java.util.Arrays;
import java.util.Optional;

public class ConstructorExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker() {
        return new ConstructorWorker().withSubparser(this);
    }

    @Override
    public int getMinimalRequiredLengthOfSource() {
        return 3;
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
        public @Nullable ExpressionResult next(ExpressionContext context, TokenRepresentation token) {
            // require 'new' keyword
            if (!token.contentEquals(Keywords.NEW)) {
                return null;
            }

            // backup current index
            DiffusedSource source = context.getDiffusedSource();

            // TODO: search for section and then select type
            Snippet typeSource = new PandaSnippet(source.next());
            TokenRepresentation sectionRepresentation = source.next();

            if (sectionRepresentation.getType() != TokenType.SECTION) {
                return null;
            }

            Section section = sectionRepresentation.toToken();

            // require () or [] section
            if (!section.getSeparator().equals(Separators.PARENTHESIS_LEFT) && !section.getSeparator().equals(Separators.SQUARE_BRACKET_LEFT)) {
                return null;
            }

            ClassPrototype type = ModuleLoaderUtils.getReferenceOrThrow(context.getContext(), typeSource.asString(), typeSource).fetch();

            if (section.getSeparator().equals(Separators.PARENTHESIS_LEFT)) {
                return parseDefault(context, type, section.getContent());
            }

            return parseArray(context, source, type, section);
        }

        private ExpressionResult parseDefault(ExpressionContext context, ClassPrototype type, Snippet argsSource) {
            Expression[] arguments = ARGUMENT_PARSER.parse(context.getContext(), argsSource);
            Optional<Arguments<PrototypeConstructor>> adjustedConstructor = type.getConstructors().getAdjustedConstructor(arguments);

            return adjustedConstructor
                    .map(constructorArguments -> ExpressionResult.of(new ParametrizedExpression(null, constructorArguments)))
                    .orElseGet(() -> ExpressionResult.error(type.getName() + " does not have constructor with the required parameters: " + Arrays.toString(arguments), argsSource));
        }

        private ExpressionResult parseArray(ExpressionContext context, DiffusedSource source, ClassPrototype type, Section capacitySourceSection) {
            Optional<ClassPrototypeReference> reference = ArrayClassPrototypeUtils.fetch(context.getContext(), type.getName() + "[]");

            if (!reference.isPresent()) {
                return ExpressionResult.error("Cannot fetch type: " + type.getName() + "[]", source.getLastReadSource());
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
