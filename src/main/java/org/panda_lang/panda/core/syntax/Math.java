package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.lang.PNumber;
import org.panda_lang.panda.util.MathBuilder;

import java.util.Stack;

public class Math implements Executable {

    private final MathBuilder mathBuilder;

    public Math(MathBuilder mathBuilder) {
        this.mathBuilder = mathBuilder;
    }

    @Override
    public Essence run(Particle particle) {
        Stack<Double> values = new Stack<>();

        for (int i = 0; i < mathBuilder.size(); i++) {
            MathBuilder.Type type = mathBuilder.next();
            if (type == MathBuilder.Type.OPERATOR) {
                char operator = mathBuilder.getOperator();
                double t = values.pop();
                double d = values.pop();
                switch (operator) {
                    case '+':
                        values.push(d + t);
                        break;
                    case '-':
                        values.push(d - t);
                        break;
                    case '*':
                        values.push(d * t);
                        break;
                    case '/':
                        values.push(d / t);
                        break;
                    case '^':
                        values.push(java.lang.Math.pow(d, t));
                        break;
                }
            } else {
                Factor factor = mathBuilder.getParameter();
                Double value = Double.valueOf(factor.getValue(particle.getMemory()).toString());
                values.push(value);
            }
        }
        return new PNumber(values.pop());
    }

}
