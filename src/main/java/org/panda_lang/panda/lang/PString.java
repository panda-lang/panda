package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.VialCenter;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.Vial;

public class PString extends PObject {

    private final static Vial vial;

    static {

        vial = VialCenter.initializeVial("String");
        vial.constructor(new Executable() {
            @Override
            public Essence run(Particle particle) {
                return new PString(particle.get(0).getValue().toString());
            }
        });

        /*
        // Register object
        ObjectScheme os = new ObjectScheme(PString.class, "String");
        // Constructor
        os.registerConstructor(new ConstructorScheme(new Constructor<PString>() {
            @Override
            public PString run(Parameter... parameters) {
                return parameters != null && parameters.length != 0 ? new PString(parameters[0].getValue().toString()) : new PString("");
            }
        }));
        // Method: contains;
        os.registerMethod(new MethodScheme("contains", new Executable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                PString me = instance.getValue(PString.class);
                return me.contains(parameters[0].getValue());
            }
        }));
        // Method: replace;
        os.registerMethod(new MethodScheme("replace", new Executable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                PString me = instance.getValue(PString.class);
                return me.replace(parameters[0].getValue(), parameters[1].getValue());
            }
        }));
        // Method: equals
        os.registerMethod(new MethodScheme("equals", new Executable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                PString me = instance.getValue(PString.class);
                return new PBoolean(me.toString().equals(parameters[0].getValue()));
            }
        }));
        // Method: equalsIgnoreCase
        os.registerMethod(new MethodScheme("equalsIgnoreCase", new Executable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                PString me = instance.getValue(PString.class);
                return new PBoolean(me.toString().equals(parameters[0].getValue()));
            }
        }));
        // Method: length
        os.registerMethod(new MethodScheme("length", new Executable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                PString me = instance.getValue(PString.class);
                return new PNumber(me.toString().length());
            }
        }));
        */
    }

    private final String string;

    public PString(String string) {
        super(vial);
        this.string = string;
    }

    public PBoolean contains(PObject o) {
        return new PBoolean(string.contains(o.toString()));
    }

    public PString replace(PObject f, PObject t) {
        String from = f.toString();
        String to = t.toString();
        return new PString(string.replace(from, to));
    }

    public String stringValue() {
        return this.string;
    }

    @Override
    public String getType() {
        return "String";
    }

    @Override
    public String toString() {
        return this.string;
    }

}
