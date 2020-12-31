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

package org.panda_lang.language.interpreter.parser.expression;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.Failure;
import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.Contextual;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.token.SourceStream;
import org.panda_lang.language.interpreter.token.Streamable;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.resource.syntax.sequence.SequencesUtils;
import org.panda_lang.utilities.commons.function.Option;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public final class PandaExpressionParser implements ExpressionParser {

    public static long time;
    public static int amount;

    private final List<SubparserRepresentation> subparsers;
    private int call;

    public PandaExpressionParser(ExpressionSubparsers subparsers) {
        this.subparsers = subparsers.getSubparsers().stream()
                .map(SubparserRepresentation::new)
                .collect(Collectors.toList());

        updateWorkers();
    }

    private void updateWorkers() {
        Collections.sort(subparsers);
    }

    @Override
    public String name() {
        return "expression";
    }

    @Override
    public Option<Expression> parseSilently(Contextual<?> context, Streamable streamable, ExpressionParserSettings settings) {
        try {
            return Option.of(parse(context, streamable, settings));
        } catch (ExpressionParserException e) {
            return Option.none();
        }
    }

    @Override
    public Expression parse(Contextual<?> context, Streamable streamable, ExpressionParserSettings settings) {
        return parse(context, streamable, settings, null);
    }

    public Expression parse(Contextual<?> context, Streamable streamable, ExpressionParserSettings settings, @Nullable BiConsumer<ExpressionContext<?>, ExpressionParserWorker> visitor) {
        SourceStream source = streamable.toStream();

        if (!source.hasUnreadSource()) {
            throw new ExpressionParserException("Expression expected");
        }

        Context<?> delegatedContext = context.toContext().forkCreator()
                .withStream(source)
                .withSource(source)
                .toContext();

        long uptime = System.nanoTime();
        PandaExpressionContext<?> expressionContext = new PandaExpressionContext<>(this, delegatedContext, source);
        ExpressionParserWorker worker = new ExpressionParserWorker(delegatedContext, subparsers);

        try {
            for (TokenInfo representation : expressionContext.getSynchronizedSource()) {
                if (SequencesUtils.isComment(representation)) {
                    continue;
                }

                if (!worker.next(expressionContext, representation)) {
                    break;
                }
            }

            worker.finish(expressionContext);
        } catch (Throwable throwable) {
            if (throwable instanceof Failure) {
                throw throwable;
            }

            throw new PandaParserFailure(throwable, delegatedContext, expressionContext.getSynchronizedSource().getSource(), throwable.toString(), "");
        }

        // if something went wrong
        if (worker.hasError()) {
            throw new PandaParserFailure(delegatedContext, worker.getError().getErrorSource(), worker.getError().getErrorMessage());
        }

        // if context does not contain any results
        if (!expressionContext.hasResults()) {
            throw new PandaParserFailure(delegatedContext, source, "Unknown expression '" + source.toSnippet().getFirstLine() + "'");
        }

        // if worker couldn't prepare the final result (in theory it should never happen)
        if (expressionContext.getResults().size() > 1) {
            throw new PandaParserFailure(delegatedContext, source, "Source contains " + expressionContext.getResults().size() + " expressions");
        }

        source.readSilently(worker.getLastSucceededRead());

        if (visitor != null) {
            visitor.accept(expressionContext, worker);
        }

        uptime = System.nanoTime() - uptime;
        time += uptime;
        amount++;

        if (call++ > 1000) {
            call = 0;
            updateWorkers();
        }

        if (settings.isStandaloneOnly() && worker.getLastCategory() != ExpressionCategory.STANDALONE) {
            if (!expressionContext.getErrors().isEmpty()) {
                ExpressionResult error = expressionContext.getErrors().peek();
                throw new PandaParserFailure(expressionContext.toContext(), error.getErrorSource(), error.getErrorMessage());
            }

            throw new PandaParserFailure(expressionContext.toContext(), source.toSnippet(), "Invalid category of expression");
        }

        return expressionContext.getResults().pop();
    }

}
