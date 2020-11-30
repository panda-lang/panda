package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.old;

import org.panda_lang.language.interpreter.parser.expression.ExpressionTransaction;

public final class AssignationContext {

    private final ExpressionTransaction transaction;

    public AssignationContext(ExpressionTransaction transaction) {
        this.transaction = transaction;
    }

    public ExpressionTransaction getTransaction() {
        return transaction;
    }

}
