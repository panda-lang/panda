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

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;

import java.util.Collection;
import java.util.Stack;

class ExpressionParserWorker {

    private final ExpressionParser parser;
    private final SourceStream source;
    private final ExpressionSubparserWorker[] subparsers;
    private final Stack<Expression> results = new Stack<>();

    private ExpressionResult<Expression> error = null;
    private int previousSubparser = -1;
    private int cachedRead = 0;
    private int read = 0;

    protected ExpressionParserWorker(ExpressionParser parser, SourceStream source, Collection<ExpressionSubparser> subparsers) {
        this.parser = parser;
        this.source = source;

        this.subparsers = subparsers.stream()
                .map(ExpressionSubparser::createSubparser)
                .toArray(ExpressionSubparserWorker[]::new);
    }

    protected Expression parse(ParserData data) {
        for (TokenRepresentation representation : source.toTokenReader()) {
            if (!this.next(data, representation)) {
                break;
            }
        }

        // if some subparsers have survived
        this.finish(data);

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

    private void finish(ParserData data) {
        for (ExpressionSubparserWorker worker : subparsers) {
            // skip removed subparsers
            if (worker == null || worker.getSubparser().getType() != ExpressionSubparserType.MUTUAL) {
                continue;
            }

            ExpressionResult<Expression> result = worker.finish(parser, data, results);

            if (result != null && result.isPresent()) {
                results.push(result.get());
                break;
            }
        }
    }

    private boolean next(ParserData data, TokenRepresentation next) {
        int cachedSubparser = previousSubparser;

        // try to use cached subparser
        if (previousSubparser != -1) {
            // cache and reset values
            previousSubparser = -1;

            // return result from cached subparser
            if (!this.next(data, next, cachedSubparser) && previousSubparser != -1) {
                read++;
                return true;
            }
        }

        for (int i = 0; i < subparsers.length; i++) {
            // skip cached subparser
            if (previousSubparser == i) {
                continue;
            }

            // return result from subparser
            if (!this.next(data, next, i)) {
                break;
            }
        }

        read++;
        return true;
    }

    private boolean next(ParserData data, TokenRepresentation next, int index) {
        ExpressionSubparserWorker worker = subparsers[index];

        // skip individual subparser if there's some content
        if (worker.getSubparser().getType() == ExpressionSubparserType.INDIVIDUAL && results.size() > 0) {
            return true;
        }

        ExpressionResult<Expression> result = worker.next(parser, data, next, results);

        // if something went wrong
        if (result == null || result.containsError()) {

            // do not override previous error
            if (result != null && error == null) {
                error = result;
            }

            return true;
        }

        previousSubparser = index;

        if (result.isEmpty()) {
            return true;
        }

        // save the result, cleanup cache, move the index
        results.push(result.get());
        cachedRead = read + 1;
        error = null;
        return false;
    }

    private enum Status {

        PROCESSING,
        DONE,
        ERROR

    }

}
