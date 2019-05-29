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

package org.panda_lang.panda.framework.language.interpreter.parser.expression;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParserSettings;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.DiffusedSource;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class PandaExpressionParser implements ExpressionParser {

    public static long time;
    public static int amount;

    private final List<ExpressionSubparserRepresentation> subparsers;
    private int call;

    public PandaExpressionParser(ExpressionSubparsers subparsers) {
        if (subparsers.getSubparsers().size() < 2) {
            throw new IllegalArgumentException("Expression parser does not support less than 2 subparsers");
        }

        this.subparsers = subparsers.getSubparsers().stream()
                .map(ExpressionSubparserRepresentation::new)
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Expression> parseSilently(ParserData data, Snippet source) {
        return parseSilently(data, new PandaSourceStream(source));
    }

    @Override
    public Optional<Expression> parseSilently(ParserData data, SourceStream source) {
        return parseSilently(data, source, ExpressionParserSettings.COMBINED);
    }

    @Override
    public Optional<Expression> parseSilently(ParserData data, SourceStream source, ExpressionParserSettings settings) {
        try {
            return Optional.of(parse(data, source, settings));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Expression parse(ParserData data, Snippet source) {
        return parse(data, new PandaSourceStream(source));
    }

    @Override
    public Expression parse(ParserData data, DiffusedSource source) {
        return parse(data, source, ExpressionParserSettings.DEFAULT);
    }

    @Override
    public Expression parse(ParserData data, DiffusedSource source, ExpressionParserSettings settings) {
        SourceStream stream = new PandaSourceStream(source.getAvailableSource());

        Expression expression = parse(data, stream, settings);
        source.setIndex(source.getIndex() + stream.getReadLength());

        return expression;
    }

    @Override
    public Expression parse(ParserData data, SourceStream source) {
        return parse(data, source, ExpressionParserSettings.COMBINED);
    }

    @Override
    public Expression parse(ParserData data, SourceStream source, ExpressionParserSettings settings) {
        return parse(data, source, settings, null);
    }

    public Expression parse(ParserData data, SourceStream source, ExpressionParserSettings settings, @Nullable BiConsumer<ExpressionContext, ExpressionParserWorker> visitor) {
        long uptime = System.nanoTime();

        ExpressionContext context = new ExpressionContext(this, data, source);
        ExpressionParserWorker worker = new ExpressionParserWorker(this, context, source, subparsers, settings);

        if (!source.hasUnreadSource()) {
            throw new ExpressionParserException("Expression expected", context, source);
        }

        for (TokenRepresentation representation : context.getDiffusedSource()) {
            if (!worker.next(context.withUpdatedToken(representation))) {
                break;
            }
        }
        worker.finish(context);

        // if something went wrong
        if (worker.hasError()) {
            throw new ExpressionParserException(worker.getError().getErrorMessage(), context, worker.getError().getSource());
        }

        // if context does not contain any results
        if (!context.hasResults()) {
            throw new ExpressionParserException("Unknown expression", context, source.toSnippet());
        }

        // if worker couldn't prepare the final result
        if (context.getResults().size() > 1) {
            throw new ExpressionParserException("Source contains " + context.getResults().size() + " expressions", context, source.toSnippet());
        }

        source.read(worker.getLastSucceededRead());

        if (visitor != null) {
            visitor.accept(context, worker);
        }

        uptime = System.nanoTime() - uptime;
        time += uptime;
        amount++;

        if (call++ > 1000) {
            Collections.sort(subparsers);
            call = 0;
        }

        if (settings.isStandaloneOnly() && worker.getLastCategory() != ExpressionCategory.STANDALONE) {
            throw new ExpressionParserException("Invalid category of expression", context, source.toSnippet());
        }

        return context.getResults().pop();
    }

}
