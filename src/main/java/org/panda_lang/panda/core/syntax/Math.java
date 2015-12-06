package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.VialCenter;
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
                Parameter parameter = mathBuilder.getParametr();
                Double value = Double.valueOf(parameter.getValue().toString());
                values.push(value);
            }
        }

        values.pop();
        return VialCenter.getVial("Number").initializeInstance(null);
    }

}
