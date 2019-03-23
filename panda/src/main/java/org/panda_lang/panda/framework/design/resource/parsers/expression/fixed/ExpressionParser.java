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
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.util.DiffusedSource;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class ExpressionParser implements Parser {

    private final List<ExpressionSubparser> subparsers = new ArrayList<>();

    public ExpressionParser(Collection<ExpressionSubparser> subparsers) {
        if (subparsers.size() < 2) {
            throw new IllegalArgumentException("Expression parser does not support less than 2 subparsers");
        }

        this.subparsers.addAll(subparsers);
        Collections.sort(this.subparsers);
    }

    public Expression parse(ParserData data, Snippet source) {
        return parse(data, new PandaSourceStream(source));
    }

    public Expression parse(ParserData data, SourceStream source) {
        ExpressionContext context = new ExpressionContext(this, data, source, new Stack<>(), new DiffusedSource(source.toSnippet()));
        ExpressionParserWorker worker = new ExpressionParserWorker(this, source, subparsers);

        for (TokenRepresentation representation : context.getDiffusedSource()) {
            if (!worker.next(context.withUpdatedToken(representation))) {
                break;
            }
        }

        worker.finish(context);

        if (context.getResults().isEmpty()) {
            if (worker.hasError()) {
                throw new ExpressionParserException("Cannot parse the expression: " + worker.getError().getErrorMessage(), worker.getError().getSource());
            }

            throw new ExpressionParserException("Cannot parse the expression", source.toSnippet());
        }

        // if worker couldn't prepare the final result
        if (context.getResults().size() > 1) {
            throw new ExpressionParserException("Source contains " + context.getResults().size() + " expressions", source.toSnippet());
        }

        source.read(worker.getLastSucceededRead());

        // if something went wrong
        if (source.hasUnreadSource() && worker.hasError()) {
            throw new ExpressionParserException("Cannot parse the expression, the latest error: " + worker.getError().getErrorMessage(), worker.getError().getSource());
        }

        return context.getResults().pop();
    }

}
