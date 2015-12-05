package org.pandalang.panda.lang;

import org.pandalang.panda.core.scheme.MethodScheme;
import org.pandalang.panda.core.scheme.ObjectScheme;
import org.pandalang.panda.core.syntax.IExecutable;
import org.pandalang.panda.core.syntax.Parameter;

public class PArray extends PObject {

    static {
        // Register object
        ObjectScheme os = new ObjectScheme(PArray.class, "Array");
        // Method: get
        os.registerMethod(new MethodScheme("get", new IExecutable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                PArray a = instance.getValue(PArray.class);
                PNumber n = parameters[0].getValue(PNumber.class);
                return a.getArray()[n.getNumber().intValue()].getValue();
            }
        }));
        // Method: size
        os.registerMethod(new MethodScheme("size", new IExecutable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                PArray a = instance.getValue(PArray.class);
                return new PNumber(a.getArray().length);
            }
        }));
    }

    private final Parameter[] array;

    public PArray(Parameter... values) {
        this.array = values;
    }

    public Parameter[] getArray() {
        return array;
    }

    @Override
    public String getType() {
        return "Array";
    }

    @Override
    public String toString() {
        return array.toString();
    }

}
