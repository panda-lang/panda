package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.scheme.ConstructorScheme;
import org.panda_lang.panda.core.scheme.MethodScheme;
import org.panda_lang.panda.core.scheme.ObjectScheme;
import org.panda_lang.panda.core.syntax.Constructor;
import org.panda_lang.panda.core.syntax.IExecutable;
import org.panda_lang.panda.core.syntax.Parameter;

public class PCharacter extends PObject {

    static {
        // Register object
        ObjectScheme os = new ObjectScheme(PCharacter.class, "Character");
        // Constructor
        os.registerConstructor(new ConstructorScheme(new Constructor<PCharacter>() {
            @Override
            public PCharacter run(Parameter... parameters) {
                if (parameters == null || parameters.length == 0) return new PCharacter('\u0000');
                else return parameters[0].getValue(PCharacter.class);
            }
        }));
        // Method: toString
        os.registerMethod(new MethodScheme("toString", new IExecutable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                PCharacter b = instance.getValue(PCharacter.class);
                return new PString(b.toString());
            }
        }));
    }

    private final char c;

    public PCharacter(char c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return Character.toString(c);
    }

}
