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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.module.ImportsUtils;
import org.panda_lang.language.architecture.type.Adjustment;
import org.panda_lang.language.architecture.type.State;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.TypeDeclarationUtils;
import org.panda_lang.language.architecture.type.TypeExecutableExpression;
import org.panda_lang.language.architecture.type.VisibilityComparator;
import org.panda_lang.language.architecture.type.member.constructor.TypeConstructor;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.expression.ExpressionCategory;
import org.panda_lang.language.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.language.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.language.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.language.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.SynchronizedSource;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.resource.syntax.TokenTypes;
import org.panda_lang.language.resource.syntax.auxiliary.Section;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.language.resource.syntax.separator.Separators;
import org.panda_lang.utilities.commons.function.Option;

import java.util.Arrays;

public final class ConstructorExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker(Context<?> context) {
        return new ConstructorWorker().withSubparser(this);
    }

    @Override
    public int minimalRequiredLengthOfSource() {
        return 3;
    }

    @Override
    public ExpressionCategory category() {
        return ExpressionCategory.STANDALONE;
    }

    @Override
    public String name() {
        return "constructor";
    }

    private static final class ConstructorWorker extends AbstractExpressionSubparserWorker {

        private static final ArgumentsParser ARGUMENT_PARSER = new ArgumentsParser();

        @Override
        public @Nullable ExpressionResult next(ExpressionContext<?> context, TokenInfo token) {
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

            if (typeValue.isEmpty()) {
                return null;
            }

            // fetch type type and update source index
            Snippet typeSource = typeValue.get();
            source.setIndex(source.getIndex() + typeSource.size());

            /*
            // parse if type is array
            if (TypeDeclarationUtils.isArray(typeSource)) {
                return parseArray(context, typeSource);
            }
             */

            if (!source.hasNext()) {
                return null;
            }

            // look for () section
            TokenInfo next = source.next();

            if (next.getType() != TokenTypes.SECTION) {
                return null;
            }

            Section section = next.toToken();

            if (!section.getSeparator().equals(Separators.PARENTHESIS_LEFT)) {
                return null;
            }

            // parse constructor call
            Type type = ImportsUtils.getTypeOrThrow(context.toContext(), typeSource.asSource(), typeSource);
            VisibilityComparator.requireAccess(type, context.toContext(), typeSource);
            State.requireInstantiation(context.toContext(), type, typeSource);

            return parseDefault(context, type, next);
        }

        private ExpressionResult parseDefault(ExpressionContext<?> context, Type type, TokenInfo section) {
            Snippet argsSource = section.toToken(Section.class).getContent();
            Expression[] arguments = ARGUMENT_PARSER.parse(context, argsSource);
            Option<Adjustment<TypeConstructor>> adjustedConstructor = type.getConstructors().getAdjustedConstructor(arguments);

            return adjustedConstructor
                    .map(constructorArguments -> ExpressionResult.of(new TypeExecutableExpression(null, constructorArguments)))
                    .orElseGet(() -> ExpressionResult.error(type.getSimpleName() + " does not have constructor with the required parameters: " + Arrays.toString(arguments), section));
        }

    }

}
