package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.parser.essential.util.MathBuilder;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.lang.PDouble;

import java.util.Stack;

public class Math implements Executable {

    private final MathBuilder mathBuilder;

    public Math(MathBuilder mathBuilder) {
        this.mathBuilder = mathBuilder;
    }

    @Override
    public Essence run(Particle particle) {
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
                        c = new PDouble(java.lang.Math.pow(a.getDouble(), b.getDouble()));
                        break;
                    default:
                        c = null;
                }
                values.push(c);
            }
            else {
                Factor factor = mathBuilder.getParameter();
                Numeric value = factor.getValue(particle.getMemory());
                values.push(value);
            }
        }
        return values.pop();
    }

}
