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

package org.panda_lang.framework.language.interpreter.parser.expression;

import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionCategory;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparserPostProcessor;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparserType;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionTransaction.Commit;
import org.panda_lang.framework.design.interpreter.token.TokenInfo;
import org.panda_lang.utilities.commons.collection.Maps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public final class PandaExpressionParserWorker {

    public static final Map<String, Long> TIMES = new HashMap<>();
    private static final int NONE = -1;

    private final List<PandaExpressionSubparserRepresentation> subparsers;
    private final ExpressionSubparserWorker[] workers;
    private final Stack<ExpressionSubparserWorker> cachedWorkers = new Stack<>();
    private ExpressionResult error = null;
    private int previousSubparser = NONE;
    private int lastSucceededRead = 0;

    protected PandaExpressionParserWorker(Context context, List<PandaExpressionSubparserRepresentation> subparsers) {
        this.subparsers = subparsers;
        this.workers = new ExpressionSubparserWorker[subparsers.size()];

        for (int index = 0; index < subparsers.size(); index++) {
            this.workers[index] = subparsers.get(index).getSubparser().createWorker(context);
        }
    }

    protected void finish(ExpressionContext context) {
        for (ExpressionSubparserWorker worker : workers) {
            // skip removed subparsers
            if (!(worker instanceof ExpressionSubparserPostProcessor)) {
                continue;
            }

            ExpressionResult result = ((ExpressionSubparserPostProcessor) worker).finish(context);

            if (result != null && result.isPresent()) {
                context.getResults().push(result.get());
                cachedWorkers.push(worker);
                break;
            }
        }
    }

    protected boolean next(ExpressionContext context, TokenInfo token) {
        int cachedSubparser = previousSubparser;

        // try to use cached subparser
        if (previousSubparser != NONE) {
            // cache and reset values
            previousSubparser = NONE;

            // return result from cached subparser
            if (this.next(cachedSubparser, context, token) && previousSubparser != NONE) {
                return true;
            }
        }

        for (int index = 0; index < workers.length; index++) {
            // skip cached subparser
            if (previousSubparser == index) {
                continue;
            }

            // return result from subparser
            if (this.next(index, context, token)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Parse the next element from context using the subparser at the specified index
     *
     * @param context the context with data
     * @param index   the index of subparser in the array
     * @return true if the result was found using the specified subparser, otherwise false
     */
    private boolean next(int index, ExpressionContext context, TokenInfo token) {
        ExpressionSubparserWorker worker = workers[index];

        // skip removed subparsers
        if (worker == null) {
            return false;
        }

        ExpressionSubparser subparser = worker.getSubparser();

        // skip subparser that does not meet assumptions
        if (subparser.getMinimalRequiredLengthOfSource() > context.getSynchronizedSource().getAmountOfAvailableSource() + 1) {
            return false;
        }

        // skip individual subparser if there's some content
        if (subparser.getSubparserType() == ExpressionSubparserType.INDIVIDUAL && context.hasResults()) {
            return false;
        }

        long time = System.nanoTime();
        int cachedIndex = context.getSynchronizedSource().getIndex();
        int cachedCommits = context.getCommits().size();

        ExpressionResult result = worker.next(context, token);
        Maps.update(TIMES, subparser.getSubparserName(), () -> 0L, cachedTime -> cachedTime + (System.nanoTime() - time));

        // if something went wrong
        if (result == null || result.containsError()) {
            context.getSynchronizedSource().setIndex(cachedIndex);
            rollback(context, cachedCommits);

            // do not override previous error
            if (result != null && error == null) {
                this.error = result;
            }

            return false;
        }

        // cache current subparser
        previousSubparser = index;

        // not yet
        if (result.isEmpty()) {
            return true;
        }

        // increase usage
        subparsers.get(index).increaseUsages();
        cachedWorkers.push(worker);

        // only one expr on stack
        if (context.hasResults()) {
            context.getSynchronizedSource().setIndex(cachedIndex);
            rollback(context, cachedCommits);
            return false;
        }

        // save the result
        context.getResults().push(result.get());

        // cleanup cache, move the index
        lastSucceededRead = context.getSynchronizedSource().getIndex();
        error = subparser instanceof PartialResultSubparser ? error : null;
        return true;
    }

    private void rollback(ExpressionContext context, int cachedCommits) {
        List<Commit> commits = context.getCommits();

        if (commits.size() <= cachedCommits) {
            return;
        }

        List<Commit> latestCommits = commits.subList(cachedCommits, commits.size());
        latestCommits.forEach(Commit::rollback);
        commits.removeAll(latestCommits);
    }

    public boolean hasError() {
        return getError() != null;
    }

    public ExpressionResult getError() {
        return error;
    }

    public ExpressionCategory getLastCategory() {
        return cachedWorkers.peek().getSubparser().getCategory();
    }

    public int getLastSucceededRead() {
        return lastSucceededRead;
    }

}
