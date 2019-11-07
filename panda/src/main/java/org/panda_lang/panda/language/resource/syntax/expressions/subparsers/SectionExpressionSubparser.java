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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;
import org.panda_lang.framework.language.resource.syntax.auxiliary.Section;
import org.panda_lang.framework.language.resource.syntax.separator.Separators;

public final class SectionExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker() {
        return new SectionWorker().withSubparser(this);
    }

    @Override
    public String getSubparserName() {
        return "section";
    }

    private static final class SectionWorker extends AbstractExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult next(ExpressionContext context, TokenRepresentation token) {
            if (token.getType() != TokenTypes.SECTION) {
                return null;
            }

            Section section = token.toToken();

            if (!section.getSeparator().equals(Separators.PARENTHESIS_LEFT)) {
                return null;
            }

            if (section.getContent().isEmpty()) {
                return ExpressionResult.error("Expression expected", token);
            }

            ExpressionTransaction expressionTransaction = context.getParser().parse(context.getContext(), section.getContent());
            context.commit(expressionTransaction::rollback);

            return ExpressionResult.of(expressionTransaction.getExpression());
        }

    }

}
