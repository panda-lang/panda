package org.panda_lang.panda.language.structure.prototype.mapper.generator;

import org.panda_lang.panda.core.structure.value.PandaValue;
import org.panda_lang.panda.language.runtime.PandaRuntimeException;
import org.panda_lang.panda.language.structure.general.expression.Expression;
import org.panda_lang.panda.language.structure.overall.module.ModuleRegistry;
import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.structure.field.FieldVisibility;
import org.panda_lang.panda.language.structure.prototype.structure.field.PrototypeField;
import org.panda_lang.panda.language.structure.prototype.structure.field.StaticValue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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
        ClassPrototype returnType = ModuleRegistry.forClass(field.getType());
        PrototypeField prototypeField = new PrototypeField(returnType, prototype.getFields().size(), field.getName(), FieldVisibility.PUBLIC, Modifier.isStatic(field.getModifiers()), true);

        // TODO: Generate bytecode
        Expression fieldExpression = new Expression(returnType, (expression, branch) -> {
            Object instance = branch != null ? branch.getInstance().getValue() : null;

            try {
                Object value = field.get(instance);
                return new PandaValue(returnType, value);
            } catch (IllegalAccessException e) {
                throw new PandaRuntimeException(e);
            }
        });

        prototypeField.setDefaultValue(fieldExpression);
        prototypeField.setStaticValue(StaticValue.of(fieldExpression, null));
        return prototypeField;
    }

}
