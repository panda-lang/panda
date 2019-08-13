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
import org.panda_lang.panda.framework.PandaFrameworkException;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionCategory;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParserSettings;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionSubparserRepresentation;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionSubparsers;
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
    private ExpressionSubparserWorker[] workers;
    private int call;

    public PandaExpressionParser(ExpressionSubparsers subparsers) {
        if (subparsers.getSubparsers().size() < 2) {
            throw new IllegalArgumentException("Expression parser does not support less than 2 subparsers");
        }

        this.subparsers = subparsers.getSubparsers().stream()
                .map(ExpressionSubparserRepresentation::new)
                .sorted()
                .collect(Collectors.toList());

        updateWorkers();
    }

    private void updateWorkers() {
        this.workers = subparsers.stream()
                //.filter(subparser -> settings.isCombined() || subparser.getSubparser().getSubparserType() != ExpressionSubparserType.INDIVIDUAL)
                .map(subparser -> {
                    ExpressionSubparserWorker worker = subparser.getSubparser().createWorker();

                    if (worker == null) {
                        throw new PandaFrameworkException(subparser.getClass() + ": null worker");
                    }

                    return worker.withSubparser(subparser);
                })
                .toArray(ExpressionSubparserWorker[]::new);
    }

    @Override
    public Optional<Expression> parseSilently(Context context, Snippet source) {
        return parseSilently(context, new PandaSourceStream(source));
    }

    @Override
    public Optional<Expression> parseSilently(Context context, SourceStream source) {
        return parseSilently(context, source, ExpressionParserSettings.COMBINED);
    }

    @Override
    public Optional<Expression> parseSilently(Context context, SourceStream source, ExpressionParserSettings settings) {
        try {
            return Optional.of(parse(context, source, settings));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Expression parse(Context context, Snippet source) {
        return parse(context, new PandaSourceStream(source));
    }

    @Override
    public Expression parse(Context context, DiffusedSource source) {
        return parse(context, source, ExpressionParserSettings.DEFAULT);
    }

    @Override
    public Expression parse(Context context, DiffusedSource source, ExpressionParserSettings settings) {
        SourceStream stream = new PandaSourceStream(source.getAvailableSource());

        Expression expression = parse(context, stream, settings);
        source.setIndex(source.getIndex() + stream.getReadLength());

        return expression;
    }

    @Override
    public Expression parse(Context context, SourceStream source) {
        return parse(context, source, ExpressionParserSettings.COMBINED);
    }

    @Override
    public Expression parse(Context context, SourceStream source, ExpressionParserSettings settings) {
        return parse(context, source, settings, null);
    }

    public Expression parse(Context context, SourceStream source, ExpressionParserSettings settings, @Nullable BiConsumer<ExpressionContext, PandaExpressionParserWorker> visitor) {
        long uptime = System.nanoTime();

        ExpressionContext expressionContext = new PandaExpressionContext(this, context, source);
        PandaExpressionParserWorker worker = new PandaExpressionParserWorker(workers);

        if (!source.hasUnreadSource()) {
            throw new PandaExpressionParserException("Expression expected", expressionContext, source);
        }

        for (TokenRepresentation representation : expressionContext.getDiffusedSource()) {
            if (!worker.next(expressionContext.withUpdatedToken(representation))) {
                break;
            }
        }

        worker.finish(expressionContext);

        // if something went wrong
        if (worker.hasError()) {
            throw new PandaExpressionParserException(worker.getError().getErrorMessage(), expressionContext, worker.getError().getSource());
        }

        // if context does not contain any results
        if (!expressionContext.hasResults()) {
            throw new PandaExpressionParserException("Unknown expression", expressionContext, source.toSnippet());
        }

        // if worker couldn't prepare the final result
        if (expressionContext.getResults().size() > 1) {
            throw new PandaExpressionParserException("Source contains " + expressionContext.getResults().size() + " expressions", expressionContext, source.toSnippet());
        }

        source.read(worker.getLastSucceededRead());

        if (visitor != null) {
            visitor.accept(expressionContext, worker);
        }

        uptime = System.nanoTime() - uptime;
        time += uptime;
        amount++;

        if (call++ > 1000) {
            call = 0;

            Collections.sort(subparsers);
            updateWorkers();
        }

        if (settings.isStandaloneOnly() && worker.getLastCategory() != ExpressionCategory.STANDALONE) {
            throw new PandaExpressionParserException("Invalid category of expression", expressionContext, source.toSnippet());
        }

        return expressionContext.getResults().pop();
    }

}
