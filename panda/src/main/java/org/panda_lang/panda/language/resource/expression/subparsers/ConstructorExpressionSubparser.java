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

package org.panda_lang.panda.language.resource.expression.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.Arguments;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionCategory;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.Snippet;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.language.architecture.module.ModuleLoaderUtils;
import org.panda_lang.panda.language.architecture.prototype.array.ArrayClassPrototype;
import org.panda_lang.panda.language.architecture.prototype.standard.parameter.ParametrizedExpression;
import org.panda_lang.panda.language.interpreter.parser.expression.AbstractExpressionSubparserWorker;
import org.panda_lang.panda.language.interpreter.token.distributors.DiffusedSource;
import org.panda_lang.panda.language.resource.PandaTypes;
import org.panda_lang.panda.language.resource.expression.subparsers.assignation.variable.VariableDeclarationUtils;
import org.panda_lang.panda.language.resource.head.ArgumentsParser;
import org.panda_lang.panda.language.resource.syntax.auxiliary.Section;
import org.panda_lang.panda.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class ConstructorExpressionSubparser implements ExpressionSubparser {

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

    private static final class ConstructorWorker extends AbstractExpressionSubparserWorker {

        private static final ArgumentsParser ARGUMENT_PARSER = new ArgumentsParser();

        @Override
        public @Nullable ExpressionResult next(ExpressionContext context, TokenRepresentation token) {
            // require 'new' keyword
            if (!token.contentEquals(Keywords.NEW)) {
                return null;
            }

            // backup current index
            DiffusedSource source = context.getDiffusedSource();

            if (!source.hasNext()) {
                return null;
            }

            // read type
            Optional<Snippet> typeValue = VariableDeclarationUtils.readType(source.getAvailableSource());

            if (!typeValue.isPresent()) {
                return null;
            }

            // fetch type reference and update source index
            Snippet typeSource = typeValue.get();
            source.setIndex(source.getIndex() + typeSource.size());

            // parse if type is array
            if (VariableDeclarationUtils.isArray(typeSource)) {
                return parseArray(context, typeSource);
            }

            if (!source.hasNext()) {
                return null;
            }

            // look for () section
            TokenRepresentation next = source.next();

            if (next.getType() != TokenType.SECTION) {
                return null;
            }

            Section section = next.toToken();

            if (!section.getSeparator().equals(Separators.PARENTHESIS_LEFT)) {
                return null;
            }

            // parse constructor call
            ClassPrototype type = ModuleLoaderUtils.getReferenceOrThrow(context.getContext(), typeSource.asSource(), typeSource).fetch();
            return parseDefault(context, type, section.getContent());
        }

        private ExpressionResult parseDefault(ExpressionContext context, ClassPrototype type, Snippet argsSource) {
            Expression[] arguments = ARGUMENT_PARSER.parse(context.getContext(), argsSource);
            Optional<Arguments<PrototypeConstructor>> adjustedConstructor = type.getConstructors().getAdjustedConstructor(arguments);

            return adjustedConstructor
                    .map(constructorArguments -> ExpressionResult.of(new ParametrizedExpression(null, constructorArguments)))
                    .orElseGet(() -> ExpressionResult.error(type.getName() + " does not have constructor with the required parameters: " + Arrays.toString(arguments), argsSource));
        }

        private ExpressionResult parseArray(ExpressionContext context, Snippet typeSource) {
            List<Section> sections = VariableDeclarationUtils.getArraySections(typeSource);
            List<Expression> capacities = new ArrayList<>();

            for (Section section : sections) {
                Snippet content = section.getContent();

                if (content.isEmpty()) {
                    break;
                }

                Expression capacity = context.getParser().parse(context.getContext(), content);

                if (!PandaTypes.INT.isAssignableFrom(capacity.getReturnType())) {
                    return ExpressionResult.error("Capacity has to be Int", content);
                }

                capacities.add(capacity);
            }

            if (capacities.isEmpty()) {
                return ExpressionResult.error("Array requires specified capacity", typeSource);
            }

            String baseClassName = typeSource.subSource(0, typeSource.size() - sections.size()).asSource();
            String endTypeName = baseClassName + StringUtils.repeated(sections.size(), "[]");

            ArrayClassPrototype instanceType = (ArrayClassPrototype) ModuleLoaderUtils.getReferenceOrThrow(context.getContext(), endTypeName, typeSource).fetch();
            ArrayClassPrototype baseType = instanceType;

            for (int declaredCapacities = 0; declaredCapacities < capacities.size() - 1; declaredCapacities++) {
                ClassPrototype componentType = baseType.getType().fetch();

                if (!(componentType instanceof ArrayClassPrototype)) {
                    throw new RuntimeException("Should not happen");
                }

                baseType = (ArrayClassPrototype) componentType;
            }

            return ExpressionResult.of(new ArrayInstanceExpression(instanceType, baseType.getType().fetch(), capacities.toArray(new Expression[0])).toExpression());
        }

    }

}
