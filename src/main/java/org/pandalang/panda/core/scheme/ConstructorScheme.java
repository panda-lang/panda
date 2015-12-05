package org.pandalang.panda.core.scheme;

import org.pandalang.panda.core.syntax.Constructor;
import org.pandalang.panda.lang.PObject;

public class ConstructorScheme {

    private final Constructor<? extends PObject> constructor;

    public ConstructorScheme(Constructor<? extends PObject> constructor) {
        this.constructor = constructor;
    }

    public Constructor<? extends PObject> getConstructor() {
        return constructor;
    }

}
