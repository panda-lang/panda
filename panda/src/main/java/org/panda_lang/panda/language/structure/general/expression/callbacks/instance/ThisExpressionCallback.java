package org.panda_lang.panda.language.structure.general.expression.callbacks.instance;

import org.panda_lang.panda.core.structure.value.Value;
import org.panda_lang.panda.language.runtime.ExecutableBranch;
import org.panda_lang.panda.language.structure.general.expression.Expression;
import org.panda_lang.panda.language.structure.general.expression.ExpressionCallback;

public class ThisExpressionCallback implements ExpressionCallback {

    @Override
    public Value call(Expression expression, ExecutableBranch branch) {
        return branch.getInstance();
    }

}
