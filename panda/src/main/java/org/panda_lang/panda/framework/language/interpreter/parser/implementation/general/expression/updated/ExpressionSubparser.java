package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated;

import org.jetbrains.annotations.NotNull;

public interface ExpressionSubparser extends ExpressionParser, Comparable<ExpressionSubparser> {

    @Override
    default int compareTo(@NotNull ExpressionSubparser subparser) {
        return Double.compare(getPriority(), subparser.getPriority());
    }

    double getPriority();

}
