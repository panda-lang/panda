package org.panda_lang.panda.language.structure.prototype.mapper.generator;

import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.structure.field.PrototypeField;

import java.lang.reflect.Field;

public class ClassPrototypeFieldGenerator {

    private final Class<?> type;
    private final ClassPrototype prototype;
    private final Field field;

    public ClassPrototypeFieldGenerator(Class<?> type, ClassPrototype prototype, Field field) {
        this.type = type;
        this.prototype = prototype;
        this.field = field;
    }

    public PrototypeField generate() {
        return null;
    }

}
