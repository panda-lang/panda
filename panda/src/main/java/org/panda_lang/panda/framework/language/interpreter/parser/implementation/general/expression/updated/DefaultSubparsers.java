package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated;

import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.subparsers.BooleanExpressionParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultSubparsers {

    private static final List<ExpressionSubparser> subparsers = new ArrayList<>();

    static {
        subparsers.add(new BooleanExpressionParser());

        Collections.sort(subparsers);
    }

    public static List<ExpressionSubparser> getDefaultSubparsers() {
        return new ArrayList<>(subparsers);
    }

}
