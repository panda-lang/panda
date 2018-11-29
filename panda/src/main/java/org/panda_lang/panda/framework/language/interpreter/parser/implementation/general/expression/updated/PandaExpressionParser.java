package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PandaExpressionParser implements ExpressionParser {

    private final List<ExpressionSubparser> subparsers;

    public PandaExpressionParser(Collection<? extends ExpressionSubparser> subparsers) {
        this.subparsers = new ArrayList<>(subparsers);
        this.sortSubparsers();
    }

    @Override
    public Expression parse(ParserData data, Tokens source) {
        Result result = readResult(source);

        if (result == null) {
            throw new PandaExpressionException("Cannot read the specified source");
        }

        return result.subparser.parse(data, result.source);
    }

    @Override
    public @Nullable Tokens read(Tokens source) {
        Result result = readResult(source);
        return result != null ? result.source : null;
    }

    private @Nullable Result readResult(Tokens source) {
        for (ExpressionSubparser subparser : subparsers) {
            Tokens tokens = subparser.read(source);

            if (tokens == null || tokens.size() == 0) {
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
