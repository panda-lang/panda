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

import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;

import java.util.Collection;

class ExpressionParserWorker {

    private static final int NONE = -1;

    private final ExpressionSubparserWorker[] subparsers;
    private ExpressionResult<Expression> error = null;
    private int previousSubparser = NONE;
    private int lastSucceededRead = 0;

    protected ExpressionParserWorker(ExpressionParser parser, SourceStream source, Collection<ExpressionSubparser> subparsers, boolean combined) {
        this.subparsers = subparsers.stream()
                .filter(subparser -> combined || subparser.getType() != ExpressionType.COMBINED)
                .map(subparser -> {
                    ExpressionSubparserWorker worker = subparser.createWorker();

                    if (worker == null) {
                        throw new ExpressionParserException(subparser.getClass() + ": null worker", source);
                    }

                    return worker.withSubparser(subparser);
                })
                .toArray(ExpressionSubparserWorker[]::new);
    }

    protected void finish(ExpressionContext context) {
        for (ExpressionSubparserWorker worker : subparsers) {
            // skip removed subparsers
            if (worker == null || worker.getSubparser().getSubparserType() != ExpressionSubparserType.MUTUAL) {
                continue;
            }

            ExpressionResult<Expression> result = worker.finish(context);

            if (result != null && result.isPresent()) {
                context.getResults().push(result.get());
                break;
            }
        }
    }

    protected boolean next(ExpressionContext context) {
        int cachedSubparser = previousSubparser;

        // try to use cached subparser
        if (previousSubparser != NONE) {
            // cache and reset values
            previousSubparser = NONE;

            // return result from cached subparser
            if (this.next(context, cachedSubparser) && previousSubparser != NONE) {
                return true;
            }
        }

        for (int index = 0; index < subparsers.length; index++) {
            // skip cached subparser
            if (previousSubparser == index) {
                continue;
            }

            // return result from subparser
            if (this.next(context, index)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Parse the next element from context using the subparser at the specified index
     *
     * @param context the context with data
     * @param index the index of subparser in the array
     * @return true if the result was found using the specified subparser, otherwise false
     */
    private boolean next(ExpressionContext context, int index) {
        ExpressionSubparserWorker worker = subparsers[index];

        // skip individual subparser if there's some content
        if (worker.getSubparser().getSubparserType() == ExpressionSubparserType.INDIVIDUAL && !context.getResults().isEmpty()) {
            return false;
        }

        ExpressionResult<Expression> result = worker.next(context);

        // if something went wrong
        if (result == null || result.containsError()) {

            // do not override previous error
            if (result != null && error == null) {
                error = result;
            }

            return false;
        }

        previousSubparser = index;

        // not yet
        if (result.isEmpty()) {
            return true;
        }

        // save the result, cleanup cache, move the index
        context.getResults().push(result.get());
        lastSucceededRead = context.getDiffusedSource().getIndex();
        error = null;
        return true;
    }

    protected boolean hasError() {
        return getError() != null;
    }

    protected int getLastSucceededRead() {
        return lastSucceededRead;
    }

    protected ExpressionResult<Expression> getError() {
        return error;
    }

}
