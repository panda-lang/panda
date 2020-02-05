/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers;

import io.vavr.control.Option;
import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.type.Adjustment;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.architecture.type.TypeConstructor;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionCategory;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.language.architecture.module.PandaImportsUtils;
import org.panda_lang.framework.language.architecture.type.TypeExecutableExpression;
import org.panda_lang.framework.language.architecture.type.array.ArrayType;
import org.panda_lang.framework.language.architecture.type.utils.StateComparator;
import org.panda_lang.framework.language.architecture.type.utils.TypeDeclarationUtils;
import org.panda_lang.framework.language.architecture.type.utils.VisibilityComparator;
import org.panda_lang.framework.language.interpreter.token.SynchronizedSource;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;
import org.panda_lang.framework.language.resource.syntax.auxiliary.Section;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.utilities.commons.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class ConstructorExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker(Context context) {
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
            SynchronizedSource source = context.getSynchronizedSource();

            if (!source.hasNext()) {
                return null;
            }

            // read type
            Option<Snippet> typeValue = TypeDeclarationUtils.readType(source.getAvailableSource());

            if (!typeValue.isDefined()) {
                return null;
            }

            // fetch type Prototype and update source index
            Snippet typeSource = typeValue.get();
            source.setIndex(source.getIndex() + typeSource.size());

            // parse if type is array
            if (TypeDeclarationUtils.isArray(typeSource)) {
                return parseArray(context, typeSource);
            }

            if (!source.hasNext()) {
                return null;
            }

            // look for () section
            TokenRepresentation next = source.next();

            if (next.getType() != TokenTypes.SECTION) {
                return null;
            }

            Section section = next.toToken();

            if (!section.getSeparator().equals(Separators.PARENTHESIS_LEFT)) {
                return null;
            }

            // parse constructor call
            Type type = PandaImportsUtils.getReferenceOrThrow(context.getContext(), typeSource.asSource(), typeSource).fetch();
            VisibilityComparator.requireAccess(type, context.getContext(), typeSource);
            StateComparator.requireInstantiation(context.getContext(), type, typeSource);

            return parseDefault(context, type, next);
        }

        private ExpressionResult parseDefault(ExpressionContext context, Type type, TokenRepresentation section) {
            Snippet argsSource = section.toToken(Section.class).getContent();
            Expression[] arguments = ARGUMENT_PARSER.parse(context, argsSource);
            Optional<Adjustment<TypeConstructor>> adjustedConstructor = type.getConstructors().getAdjustedConstructor(arguments);

            return adjustedConstructor
                    .map(constructorArguments -> ExpressionResult.of(new TypeExecutableExpression(null, constructorArguments)))
                    .orElseGet(() -> ExpressionResult.error(type.getSimpleName() + " does not have constructor with the required parameters: " + Arrays.toString(arguments), section));
        }

        private ExpressionResult parseArray(ExpressionContext context, Snippet typeSource) {
            List<Section> sections = getArraySections(typeSource);
            List<Expression> capacities = new ArrayList<>();

            for (Section section : sections) {
                Snippet content = section.getContent();

                if (content.isEmpty()) {
                    break;
                }

                ExpressionTransaction capacityTransaction = context.getParser().parse(context.getContext(), content);
                context.commit(capacityTransaction::rollback);
                Expression capacity = capacityTransaction.getExpression();

                if (!capacity.getType().getAssociatedClass().isAssignableTo(Integer.class)) {
                    return ExpressionResult.error("Capacity has to be Int", content);
                }

                capacities.add(capacity);
            }

            if (capacities.isEmpty()) {
                return ExpressionResult.error("Array requires specified capacity", typeSource);
            }

            String baseClassName = typeSource.subSource(0, typeSource.size() - sections.size()).asSource();
            String endTypeName = baseClassName + StringUtils.repeated(sections.size(), "[]");

            ArrayType instanceType = (ArrayType) PandaImportsUtils.getReferenceOrThrow(context.getContext(), endTypeName, typeSource).fetch();
            ArrayType baseType = instanceType;

            for (int declaredCapacities = 0; declaredCapacities < capacities.size() - 1; declaredCapacities++) {
                Type componentType = baseType.getArrayType();

                if (!(componentType instanceof ArrayType)) {
                    throw new PandaFrameworkException("Should not happen");
                }

                baseType = (ArrayType) componentType;
            }

            return ExpressionResult.of(new ArrayInstanceExpression(instanceType, baseType.getArrayType(), capacities.toArray(new Expression[0])).toExpression());
        }

        private List<Section> getArraySections(Snippet type) {
            List<Section> sections = new ArrayList<>();

            for (int index = type.size() - 1; index >= 0; index--) {
                TokenRepresentation representation = type.get(index);

                if (representation.getType() != TokenTypes.SECTION) {
                    break;
                }

                sections.add(representation.toToken());
            }

            return sections;
        }

    }

}
