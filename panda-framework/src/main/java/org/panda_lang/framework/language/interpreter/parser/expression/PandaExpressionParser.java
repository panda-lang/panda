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

package org.panda_lang.framework.language.interpreter.parser.expression;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.ParserFailure;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionCategory;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionParserSettings;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparsers;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionTransaction.Commit;
import org.panda_lang.framework.design.interpreter.token.SourceStream;
import org.panda_lang.framework.design.interpreter.token.Streamable;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.language.resource.syntax.sequence.SequencesUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public final class PandaExpressionParser implements ExpressionParser {

    public static long time;
    public static int amount;

    private final List<PandaExpressionSubparserRepresentation> subparsers;
    private int call;

    public PandaExpressionParser(ExpressionSubparsers subparsers) {
        this.subparsers = subparsers.getSubparsers().stream()
                .map(PandaExpressionSubparserRepresentation::new)
                .collect(Collectors.toList());

        updateWorkers();
    }

    private void updateWorkers() {
        Collections.sort(subparsers);
    }

    @Override
    public Optional<ExpressionTransaction> parseSilently(Context context, Streamable streamable, ExpressionParserSettings settings) {
        try {
            return Optional.of(parse(context, streamable, settings));
        } catch (PandaExpressionParserException e) {
            return Optional.empty();
        }
    }

    @Override
    public ExpressionTransaction parse(Context context, Streamable streamable, ExpressionParserSettings settings) {
        return parse(context, streamable, settings, null);
    }

    public ExpressionTransaction parse(Context context, Streamable streamable, ExpressionParserSettings settings, @Nullable BiConsumer<ExpressionContext, PandaExpressionParserWorker> visitor) {
        SourceStream source = streamable.toStream();

        if (!source.hasUnreadSource()) {
            throw new PandaExpressionParserException("Expression expected");
        }

        long uptime = System.nanoTime();
        PandaExpressionContext expressionContext = new PandaExpressionContext(this, context, source);
        PandaExpressionParserWorker worker = new PandaExpressionParserWorker(context, subparsers);

        try {
            for (TokenRepresentation representation : expressionContext.getSynchronizedSource()) {
                if (SequencesUtils.isComment(representation)) {
                    continue;
                }

                if (!worker.next(expressionContext, representation)) {
                    break;
                }
            }

            worker.finish(expressionContext);
        } catch (Exception e) {
            for (Commit commit : expressionContext.getCommits()) {
                commit.rollback();
            }

            if (e instanceof ParserFailure) {
                throw e;
            }

            throw new PandaExpressionParserFailure(expressionContext, expressionContext.getSynchronizedSource().getSource(), e.getMessage());
        }

        PandaExpressionTransaction transaction = new PandaExpressionTransaction(null, expressionContext.getCommits());

        // if something went wrong
        if (worker.hasError()) {
            transaction.rollback();
            throw new PandaExpressionParserFailure(expressionContext, worker.getError().getErrorSource(), worker.getError().getErrorMessage());
        }

        // if context does not contain any results
        if (!expressionContext.hasResults()) {
            transaction.rollback();
            throw new PandaExpressionParserFailure(expressionContext, source.toSnippet(), "Unknown expression");
        }

        // if worker couldn't prepare the final result (in theory it should never happen)
        if (expressionContext.getResults().size() > 1) {
            transaction.rollback();
            throw new PandaExpressionParserFailure(expressionContext, source.toSnippet(), "Source contains " + expressionContext.getResults().size() + " expressions");
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
            updateWorkers();
        }

        if (settings.isStandaloneOnly() && worker.getLastCategory() != ExpressionCategory.STANDALONE) {
            transaction.rollback();
            throw new PandaExpressionParserFailure(expressionContext, source.toSnippet(), "Invalid category of expression");
        }

        return transaction.withExpression(expressionContext.getResults().pop());
    }

}
