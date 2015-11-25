package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.scheme.ConstructorScheme;
import org.panda_lang.panda.core.scheme.MethodScheme;
import org.panda_lang.panda.core.scheme.ObjectScheme;
import org.panda_lang.panda.core.syntax.Constructor;
import org.panda_lang.panda.core.syntax.IExecutable;
import org.panda_lang.panda.core.syntax.Parameter;

public class PBoolean extends PObject {

    static {
        // Register object
        ObjectScheme os = new ObjectScheme(PBoolean.class, "Boolean");
        // Constructor
        os.registerConstructor(new ConstructorScheme(new Constructor<PBoolean>() {
            @Override
            public PBoolean run(Parameter... parameters) {
                if (parameters == null || parameters.length == 0) return new PBoolean(false);
                else return parameters[0].getValue(PBoolean.class);
            }
        }));
        // Method: toString
        os.registerMethod(new MethodScheme("toString", new IExecutable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                PBoolean b = instance.getValue(PBoolean.class);
                return new PString(b.toString());
            }
        }));
    }

    private final boolean b;

    public PBoolean(boolean b) {
        this.b = b;
    }

    public boolean isTrue() {
        return b;
    }

    public boolean isFalse() {
        return !b;
    }

    public boolean getBoolean() {
        return b;
    }

    @Override
    public String getType() {
        return "Boolean";
    }

    @Override
    public String toString() {
        return Boolean.toString(b);
    }

}
