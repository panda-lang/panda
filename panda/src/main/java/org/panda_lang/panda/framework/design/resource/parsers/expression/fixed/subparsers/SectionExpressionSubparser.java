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
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionParser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionResult;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.util.AbstractExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.util.SeparatedContentReader;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

import java.util.Stack;

public class SectionExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createSubparser() {
        return new SentenceWorker().withSubparser(this);
    }

    static class SentenceWorker extends AbstractExpressionSubparserWorker implements ExpressionSubparserWorker {

        private final SeparatedContentReader contentReader = new SeparatedContentReader(Separators.PARENTHESIS_LEFT, SeparatedContentReader.ContentProcessor.DEFAULT);

        @Override
        public @Nullable ExpressionResult<Expression> next(ExpressionParser parser, ParserData data, TokenRepresentation token, Stack<Expression> results) {
            return contentReader.read(parser, data, token);
        }

    }

}
