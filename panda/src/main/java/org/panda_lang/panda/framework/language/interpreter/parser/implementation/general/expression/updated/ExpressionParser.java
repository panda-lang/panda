package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.TokensUtils;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ExpressionParser {

    private final List<ExpressionSubparser> subparsers;

    public ExpressionParser(Collection<? extends ExpressionSubparser> subparsers) {
        this.subparsers = new ArrayList<>(subparsers);
        this.sortSubparsers();
    }

    public Expression parse(ParserData data, Tokens tokens) {
        return parse(data, new PandaSourceStream(tokens));
    }

    public Expression parse(ParserData data, SourceStream source) {
        Result result = readResult(source.toTokenizedSource());

        if (result == null) {
            throw new PandaExpressionFailure("Cannot read the specified source", data);
        }

        Expression expression = result.subparser.parse(this, data, result.source);

        if (expression == null) {
            throw new PandaExpressionFailure("Cannot parse expression", data);
        }

        source.readDifference(result.source);
        return expression;
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

        for (ExpressionSubparser subparser : subparsers) {
            Tokens tokens = subparser.read(this, source);

            if (TokensUtils.isEmpty(tokens)) {
                continue;
            }

            return new Result(subparser, tokens);
        }

        return null;
    }

    private void sortSubparsers() {
        Collections.sort(subparsers);
    }

    public void addSubparser(ExpressionSubparser subparser) {
        this.subparsers.add(subparser);
        this.sortSubparsers();
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
