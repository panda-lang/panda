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
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.TokensUtils;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.subparsers.DefaultSubparsers;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ExpressionParser {

    private static final ExpressionParser PARSER = new ExpressionParser(DefaultSubparsers.Instances.getDefaultSubparsers());
    private final List<ExpressionSubparser> subparsers;

    public ExpressionParser(Collection<? extends ExpressionSubparser> subparsers) {
        this.subparsers = new ArrayList<>(subparsers);
        this.sortSubparsers();
    }

    public @Nullable Expression parseSilently(ParserData data, Tokens tokens) {
        try {
            return parse(data, tokens);
        } catch (Throwable throwable) {
            // mute, we don't want to catch any error that comes from ExpressionParser#parse method
            return null;
        }
    }

    public Expression parse(ParserData data, Tokens tokens) {
        return parse(data, new PandaSourceStream(tokens));
    }

    public Expression parse(ParserData data, SourceStream source) {
        Result result = readResult(source.toTokenizedSource());

        if (result == null) {
            throw new PandaExpressionFailure("Cannot read the specified source", data);
        }

        Expression expression = result.subparser.parse(PARSER, data, result.source);

        if (expression == null) {
            throw new PandaExpressionFailure("Cannot parse expression", data);
        }

        source.readDifference(result.source);
        return expression;
    }

    public @Nullable Tokens read(SourceStream source) {
        Tokens result = read(source.toTokenizedSource());

        if (result == null) {
            return null;
        }

        source.readDifference(result);
        return result;
    }

    public @Nullable Tokens read(Tokens source) {
        Result result = readResult(source);

        if (result == null) {
            return null;
        }

        if (TokensUtils.isEmpty(result.source)) {
            return null;
        }

        return result.source;
    }

    private @Nullable Result readResult(Tokens source) {
        if (source.isEmpty()) {
            return null;
        }

        // create special group of subparsers to compare
        Result previousResult = null;

        for (ExpressionSubparser subparser : subparsers) {
            Tokens tokens = subparser.read(PARSER, source);

            if (TokensUtils.isEmpty(tokens)) {
                continue;
            }

            if (previousResult == null || previousResult.source.size() < tokens.size()) {
                previousResult = new Result(subparser, new ExpressionTokens(tokens, subparser));
            }
        }

        return previousResult;
    }

    private void sortSubparsers() {
        Collections.sort(subparsers);
    }

    public void addSubparser(ExpressionSubparser subparser) {
        this.subparsers.add(subparser);
        this.sortSubparsers();
    }

    public void removeSubparsers(Collection<String> names) {
        for (String name : names) {
            removeSubparser(name.trim());
        }
    }

    public void removeSubparser(String name) {
        subparsers.removeIf(expressionSubparser -> expressionSubparser.getName().equals(name));
    }

    public static ExpressionParser getInstance() {
        return PARSER;
    }

    private final class Result {

        private final ExpressionSubparser subparser;
        private final Tokens source;

        public Result(ExpressionSubparser subparser, Tokens source) {
            this.subparser = subparser;
            this.source = source;
        }

    }

}
