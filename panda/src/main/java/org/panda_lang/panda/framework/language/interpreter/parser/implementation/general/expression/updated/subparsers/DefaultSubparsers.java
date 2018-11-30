package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.subparsers;

import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionSubparser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultSubparsers {

    private static final List<ExpressionSubparser> SUBPARSERS = new ArrayList<>();

    static {
        SUBPARSERS.add(new LiteralExpressionParser());
        SUBPARSERS.add(new SequenceExpressionParser());

        Collections.sort(SUBPARSERS);
    }

    public static List<ExpressionSubparser> getDefaultSubparsers() {
        return new ArrayList<>(SUBPARSERS);
    }

}
