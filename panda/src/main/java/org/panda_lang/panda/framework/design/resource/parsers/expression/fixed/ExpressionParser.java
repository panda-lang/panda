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

package org.panda_lang.panda.framework.design.resource.parsers.expression.fixed;

import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

public class ExpressionParser implements Parser {

    private final Collection<ExpressionSubparser> representations = new ArrayList<>();

    public ExpressionParser(Collection<ExpressionSubparser> representations) {
        if (representations.size() < 2) {
            throw new IllegalArgumentException("Expression parser does not support less than 2 subparsers");
        }

        this.representations.addAll(representations);
    }

    public Expression parse(ParserData data, Tokens source) {
        return parse(data, new PandaSourceStream(source));
    }

    public Expression parse(ParserData data, SourceStream source) {
        Stack<Expression> results = new Stack<>();
        ExpressionResult<Expression> error = null;

        int excluded = 0;
        int cachedRead = 0;
        int read = 0;

        ExpressionSubparserWorker[] subparsers = representations.stream()
                .map(ExpressionSubparser::createSubparser)
                .toArray(ExpressionSubparserWorker[]::new);

        for (TokenRepresentation representation : source.toTokenReader()) {
            for (int i = 0; i < subparsers.length; i++) {
                ExpressionSubparserWorker worker = subparsers[i];

                if (worker == null) {
                    continue;
                }

                ExpressionResult<Expression> result = worker.next(this, data, representation, results);

                if (result == null) {
                    subparsers[i] = null;
                    excluded++;
                    continue;
                }

                if (result.containsError()) {
                    subparsers[i] = null;
                    excluded++;
                    error = result;
                    continue;
                }

                if (!result.isPresent()) {
                    continue;
                }

                results.push(result.get());
                cachedRead = read + 1;
                break;
            }

            if (excluded == subparsers.length) {
                break;
            }

            read++;
        }

        if (results.isEmpty()) {
            if (error != null) {
                throw new ExpressionParserException("Cannot parse the expression: " + error.getErrorMessage(), error.getSource());
            }

            throw new ExpressionParserException("Cannot parse the expression", source.toTokenizedSource());
        }

        if (results.size() > 1) {
            throw new ExpressionParserException("Source contains " + results.size() + " expressions", source.toTokenizedSource());
        }

        source.read(cachedRead);

        if (source.hasUnreadSource() && error != null) {
            throw new ExpressionParserException("Cannot parse the expression, the latest error: " + error.getErrorMessage(), error.getSource());
        }

        return results.pop();
    }

}
