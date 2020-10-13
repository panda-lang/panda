package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.variable;

import org.panda_lang.language.architecture.expression.Expression;

public final class AssignationContext {

    private final Expression expression;

    public AssignationContext(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

}
