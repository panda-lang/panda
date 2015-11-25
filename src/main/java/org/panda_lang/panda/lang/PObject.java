package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.scheme.ConstructorScheme;
import org.panda_lang.panda.core.scheme.MethodScheme;
import org.panda_lang.panda.core.scheme.ObjectScheme;
import org.panda_lang.panda.core.syntax.Constructor;
import org.panda_lang.panda.core.syntax.IExecutable;
import org.panda_lang.panda.core.syntax.Parameter;

public class PObject {

    static {
        // Register object
        ObjectScheme os = new ObjectScheme(PObject.class, "Object");
        // Constructor
        os.registerConstructor(new ConstructorScheme(new Constructor<PObject>() {
            @Override
            public PObject run(Parameter... parameters) {
                return new PObject();
            }
        }));
        // Method: toString
        os.registerMethod(new MethodScheme("toString", new IExecutable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                return new PString(instance.getValue().toString());
            }
        }));
    }

    public <T> T getMe(Class<T> clazz) {
        return (T) this;
    }

    public String getType() {
        return "Object";
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
