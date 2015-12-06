package org.panda_lang.panda.core.scheme;

import org.panda_lang.panda.core.syntax.Constructor;
import org.panda_lang.panda.core.syntax.Essence;

public class ConstructorScheme {

    private final Constructor<? extends Essence> constructor;

    public ConstructorScheme(Constructor<? extends Essence> constructor) {
        this.constructor = constructor;
    }

    public Constructor<? extends Essence> getConstructor() {
        return constructor;
    }

}
