package org.panda_lang.panda.language.structure.general.expression.callbacks.instance;

import org.panda_lang.panda.design.architecture.value.Value;
import org.panda_lang.panda.design.runtime.ExecutableBranch;
import org.panda_lang.panda.language.structure.general.expression.Expression;
import org.panda_lang.panda.language.structure.general.expression.ExpressionCallback;
import org.panda_lang.panda.design.architecture.prototype.ClassPrototype;

public class ThisExpressionCallback implements ExpressionCallback {

    @Override
    public Value call(Expression expression, ExecutableBranch branch) {
        return branch.getInstance();
    }

    public static Expression asExpression(ClassPrototype type) {
        return new Expression(type, new ThisExpressionCallback());
    }

}
