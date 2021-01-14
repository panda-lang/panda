package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.bitwise;

import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.module.TypeLoader;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.rpn.RPNOperationAction;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.number.NumericOperation;

public abstract class BitwiseOperation extends NumericOperation<Number> {

    public abstract RPNOperationAction<Number> of(Type returnType, int priority, Expression a, Expression b);

    @Override
    public RPNOperationAction<Number> of(TypeLoader typeLoader, Expression a, Expression b) {
        Type returnType = returnType(typeLoader, a.getKnownType(), b.getKnownType());
        return of(returnType, getPriority(returnType), a, b);
    }

    @Override
    public Type returnType(TypeLoader typeLoader, Type a, Type b) {
        return estimateType(a, b);
    }

    @Override
    public Type requiredType(TypeLoader typeLoader) {
        return typeLoader.requireType("panda::Number");
    }

}
