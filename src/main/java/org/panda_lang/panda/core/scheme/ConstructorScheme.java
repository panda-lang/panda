package org.panda_lang.panda.core.scheme;

import org.panda_lang.panda.core.syntax.Constructor;
import org.panda_lang.panda.lang.PObject;

public class ConstructorScheme {

    private final Constructor<? extends PObject> constructor;

    public ConstructorScheme(Constructor<? extends PObject> constructor) {
        this.constructor = constructor;
    }

    public Constructor<? extends PObject> getConstructor() {
        return constructor;
    }

}
