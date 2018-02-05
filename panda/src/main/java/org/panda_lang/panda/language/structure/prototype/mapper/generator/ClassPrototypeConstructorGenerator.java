package org.panda_lang.panda.language.structure.prototype.mapper.generator;

import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;

import java.lang.reflect.Constructor;

public class ClassPrototypeConstructorGenerator {

    private final Class<?> type;
    private final ClassPrototype prototype;
    private final Constructor<?> constructor;

    public ClassPrototypeConstructorGenerator(Class<?> type, ClassPrototype prototype, Constructor<?> constructor) {
        this.type = type;
        this.prototype = prototype;
        this.constructor = constructor;
    }



}
