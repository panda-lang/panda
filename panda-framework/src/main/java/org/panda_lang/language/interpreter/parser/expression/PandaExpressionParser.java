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
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.ParserFailure;
import org.panda_lang.language.interpreter.parser.expression.ExpressionTransaction.Commit;
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
    public Option<ExpressionTransaction> parseSilently(Context context, Streamable streamable, ExpressionParserSettings settings) {
        try {
            return Option.of(parse(context, streamable, settings));
        } catch (PandaExpressionParserException e) {
            return Option.none();
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
            for (Commit commit : expressionContext.getCommits()) {
                commit.rollback();
            }

            if (throwable instanceof ParserFailure) {
                throw throwable;
            }

            throw new PandaParserFailure(throwable, expressionContext, expressionContext.getSynchronizedSource().getSource(), throwable.toString(), "");
        }

        PandaExpressionTransaction transaction = new PandaExpressionTransaction(null, expressionContext.getCommits());

        // if something went wrong
        if (worker.hasError()) {
            transaction.rollback();
            throw new PandaParserFailure(
                    expressionContext, worker.getError().getErrorSource(),
                    worker.getError().getErrorMessage(),
                    "マルセルへの注意 \uD83E\uDD20"
            );
        }

        // if context does not contain any results
        if (!expressionContext.hasResults()) {
            transaction.rollback();
            throw new PandaParserFailure(expressionContext, source, "Unknown expression '" + source.toSnippet().getFirstLine() + "'");
        }

        // if worker couldn't prepare the final result (in theory it should never happen)
        if (expressionContext.getResults().size() > 1) {
            transaction.rollback();
            throw new PandaParserFailure(expressionContext, source, "Source contains " + expressionContext.getResults().size() + " expressions");
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
            transaction.rollback();
            throw new PandaParserFailure(expressionContext, source.toSnippet(), "Invalid category of expression");
        }

        return transaction.withExpression(expressionContext.getResults().pop());
    }

}
