package org.panda_lang.panda.core.statement;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.parser.essential.util.MathBuilder;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.lang.DoubleEssence;

import java.util.Stack;

public class Math implements Executable {

    private final MathBuilder mathBuilder;

    public Math(MathBuilder mathBuilder) {
        this.mathBuilder = mathBuilder;
    }

    @Override
    public Essence execute(Alice alice) {
        Stack<Numeric> values = new Stack<>();

        for (int i = 0; i < mathBuilder.size(); i++) {
            MathBuilder.Type type = mathBuilder.next();
            if (type == MathBuilder.Type.OPERATOR) {
                char operator = mathBuilder.getOperator();
                Numeric a = values.pop();
                Numeric b = values.pop();
                Numeric c;
                switch (operator) {
                    case '+':
                        c = a.add(b);
                        break;
                    case '-':
                        c = a.subtract(b);
                        break;
                    case '*':
                        c = a.multiply(b);
                        break;
                    case '/':
                        c = a.divide(b);
                        break;
                    case '^':
                        c = new DoubleEssence(java.lang.Math.pow(a.getDouble(), b.getDouble()));
                        break;
                    default:
                        c = null;
                }
                values.push(c);
            }
            else {
                RuntimeValue runtimeValue = mathBuilder.getParameter();
                Numeric value = runtimeValue.getValue(alice);
                values.push(value);
            }
        }
        return values.pop();
    }

}
