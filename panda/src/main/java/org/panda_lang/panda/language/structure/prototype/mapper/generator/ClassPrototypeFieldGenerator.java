package org.panda_lang.panda.language.structure.prototype.mapper.generator;

import org.panda_lang.panda.core.structure.value.PandaValue;
import org.panda_lang.panda.core.structure.value.Value;
import org.panda_lang.panda.language.runtime.PandaRuntimeException;
import org.panda_lang.panda.language.structure.general.expression.Expression;
import org.panda_lang.panda.language.structure.overall.module.ModuleRegistry;
import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.structure.field.FieldVisibility;
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
        PrototypeField prototypeField = new PrototypeField(prototype, prototype.getFields().size(), field.getName(), FieldVisibility.PUBLIC, false);
        ClassPrototype returnType = ModuleRegistry.forClass(field.getType());

        Expression fieldExpression = new Expression(returnType, (expression, branch) -> {
            Value instance = branch.getInstance();

            try {
                Object value = field.get(instance.getValue());
                return new PandaValue(returnType, value);
            } catch (IllegalAccessException e) {
                throw new PandaRuntimeException(e);
            }
        });

        prototypeField.setDefaultValue(fieldExpression);
        return prototypeField;
    }

}
