package org.panda_lang.panda.language.structure.prototype.mapper.generator;

import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.structure.PandaClassPrototype;
import org.panda_lang.panda.language.structure.prototype.structure.method.MethodCallback;
import org.panda_lang.panda.language.structure.prototype.structure.method.MethodVisibility;
import org.panda_lang.panda.language.structure.prototype.structure.method.PrototypeMethod;
import org.panda_lang.panda.language.structure.prototype.structure.method.variant.PandaMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ClassPrototypeMethodGenerator {

    private final Class<?> type;
    private final ClassPrototype prototype;
    private final Method method;

    public ClassPrototypeMethodGenerator(Class<?> type, ClassPrototype prototype, Method method) {
        this.type = type;
        this.prototype = prototype;
        this.method = method;
    }

    public PrototypeMethod generate() {
        ClassPrototype returnType = PandaClassPrototype.forClass(method.getReturnType());

        MethodCallback<Object> methodBody = (bridge, instance, parameters) -> {
            Object[] args = new Object[parameters.length];

            for (int i = 0; i < parameters.length; i++) {
                args[i] = parameters[i].getValue();
            }

            try {
                method.invoke(instance, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        ClassPrototype[] parameters = new ClassPrototype[method.getParameterCount()];

        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = PandaClassPrototype.forClass(method.getParameterTypes()[i]);
        }

        return new PandaMethod(prototype, MethodVisibility.PUBLIC, Modifier.isStatic(method.getModifiers()), returnType, method.getName(), methodBody, parameters);
    }

}
