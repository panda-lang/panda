package org.panda_lang.panda.language.structure.prototype.mapper.generator;

import org.panda_lang.panda.core.structure.value.PandaValue;
import org.panda_lang.panda.core.structure.value.Value;
import org.panda_lang.panda.language.runtime.PandaRuntimeException;
import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.structure.PandaClassPrototype;
import org.panda_lang.panda.language.structure.prototype.structure.method.MethodCallback;
import org.panda_lang.panda.language.structure.prototype.structure.method.MethodVisibility;
import org.panda_lang.panda.language.structure.prototype.structure.method.PrototypeMethod;
import org.panda_lang.panda.language.structure.prototype.structure.method.variant.PandaMethod;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.InvalidParameterException;

public class ClassPrototypeMethodGenerator {

    private final Class<?> type;
    private final ClassPrototype prototype;
    private final Method method;

    public ClassPrototypeMethodGenerator(Class<?> type, ClassPrototype prototype, Method method) {
        if (method == null) {
            throw new InvalidParameterException("Method cannot be null");
        }

        this.type = type;
        this.prototype = prototype;
        this.method = method;
    }

    public PrototypeMethod generate() {
        ClassPrototype returnType = PandaClassPrototype.forClass(method.getReturnType());
        boolean isVoid = returnType.getClassName().equals("void");

        // TODO: Generate bytecode
        MethodCallback<Object> methodBody = (branch, instance, parameters) -> {
            try {
                int amountOfArgs = parameters.length;
                int parameterCount = method.getParameterCount();
                Object varargs = null;

                if (amountOfArgs != parameterCount) {
                    if (parameterCount < 1) {
                        throw new PandaRuntimeException("Too many arguments");
                    }

                    Class<?> last = method.getParameterTypes()[parameterCount - 1];
                    String lastName = last.getName();
                    Class<?> rootLast = Class.forName(lastName.substring(2, lastName.length() - 1));

                    if (amountOfArgs + 1 != parameterCount || !last.isArray()) {
                        throw new PandaRuntimeException("Cannot invoke mapped mapped method (args.length != parameters.length)");
                    }

                    varargs = Array.newInstance(rootLast, 0);
                    ++amountOfArgs;
                }

                Object[] args = new Object[amountOfArgs];

                for (int i = 0; i < parameters.length; i++) {
                    Value parameter = parameters[i];

                    if (parameter == null) {
                        continue;
                    }

                    args[i] = parameter.getValue();
                }

                if (varargs != null) {
                    args[amountOfArgs - 1] = varargs;
                }

                Object returnValue = method.invoke(instance, args);

                if (isVoid) {
                    return;
                }

                Value value = new PandaValue(returnType, returnValue);
                branch.setReturnValue(value);
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
