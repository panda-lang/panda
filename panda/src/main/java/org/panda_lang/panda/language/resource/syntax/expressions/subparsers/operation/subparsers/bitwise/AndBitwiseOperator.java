package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.bitwise;

import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.interpreter.parser.PandaParserException;
import org.panda_lang.language.runtime.ProcessStack;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.rpn.RPNOperationAction;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.number.NumericOperator;

import java.util.function.BiFunction;

public final class AndBitwiseOperator extends BitwiseOperation {

    @Override
    public RPNOperationAction<Number> of(Type returnType, int priority, Expression a, Expression b) {
        BiFunction<Number, Number, Number> and = toFunction(priority);

        return new NumericOperator(returnType, a, b) {
            @Override
            public Number get(ProcessStack stack, Object instance, Number a, Number b) {
                return and.apply(a, b);
            }
        };
    }

    private BiFunction<Number, Number, Number> toFunction(int priority) {
        switch (priority) {
            case BYTE:
                return (a, b) -> a.byteValue() & b.byteValue();
            case SHORT:
                return (a, b) -> a.shortValue() & b.shortValue();
            case INT:
                return (a, b) -> a.intValue() & b.intValue();
            case LONG:
                return (a, b) -> a.longValue() & b.longValue();
            case FLOAT:
                throw new PandaParserException("Cannot use and operator on float type");
            case DOUBLE:
                throw new PandaParserException("Cannot use and operator on double type");
            default:
                throw new PandaParserException("Unknown type " + priority);
        }
    }

}
