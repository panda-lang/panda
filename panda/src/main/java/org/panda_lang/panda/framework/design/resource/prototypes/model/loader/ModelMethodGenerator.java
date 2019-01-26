package org.panda_lang.panda.framework.design.resource.prototypes.model.loader;

import javassist.CtClass;
import javassist.CtMethod;
import org.panda_lang.panda.PandaException;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.prototype.generator.ClassPrototypeGeneratorUtils;
import org.panda_lang.panda.framework.design.architecture.prototype.method.MethodCallback;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PandaMethod;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.resource.prototypes.model.ClassPrototypeModel;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

class ModelMethodGenerator {

    private static final AtomicInteger ID = new AtomicInteger();

    public void generate(Class<? extends ClassPrototypeModel> model, ClassPrototype prototype, Method method) throws Exception {
        ClassPrototypeModel.MethodDeclaration methodDeclaration = method.getAnnotation(ClassPrototypeModel.MethodDeclaration.class);

        if (methodDeclaration == null) {
            return;
        }

        String methodCallbackClassName = model.getSimpleName() + StringUtils.capitalize(method.getName()) + "MethodCallback" + ID.incrementAndGet();
        CtClass generatedMethodCallbackClass = ModelLoaderUtils.POOL.getOrNull(methodCallbackClassName);

        if (generatedMethodCallbackClass != null) {
            return;
        }

        generatedMethodCallbackClass = ModelLoaderUtils.POOL.makeClass(methodCallbackClassName);
        generatedMethodCallbackClass.setSuperclass(ModelLoaderUtils.CPM_METHOD_CALLBACK);

        CtMethod callbackImplementation = new CtMethod(CtClass.voidType, "invoke", ModelLoaderUtils.IMPLEMENTATION, generatedMethodCallbackClass);
        boolean array = method.getParameters()[method.getParameters().length - 1].getType().isArray();
        StringBuilder values = new StringBuilder();
        int valuesCount = 0;

        for (int i = 2; i < method.getParameters().length; i++) {
            values.append(",");

            if (array && i == method.getParameterCount() - 1) {
                values.append("values");
                break;
            }

            values.append("(").append(Value.class.getName()).append(")");
            values.append("$3[").append(i - 2).append("]");
            valuesCount++;
        }

        String instanceType = method.getParameters()[1].getType().getName();
        StringBuilder bodyBuilder = new StringBuilder("{");

        if (array) {
            bodyBuilder.append(String.format("%s[] values = new %s[$3.length - %d];", ModelLoaderUtils.VALUE.getName(), ModelLoaderUtils.VALUE.getName(), valuesCount));
            bodyBuilder.append("for (int i = 0; i < values.length; i++) {");
            bodyBuilder.append(String.format("values[i] = $3[%d + i];", valuesCount));
            bodyBuilder.append("}");
        }

        if (methodDeclaration.isStatic()) {
            bodyBuilder.append(String.format("%s.%s($1, (%s) null %s);", model.getName(), method.getName(), instanceType, values.toString()));
        }
        else {
            // TODO: Improve prototype mapping concept
            bodyBuilder.append(String.format("%s typedInstance = (%s) $2;", model.getName(), model.getName()));
            bodyBuilder.append(String.format("typedInstance.%s($1, (%s) $2 %s);", method.getName(), instanceType, values.toString()));
        }
        bodyBuilder.append("}");

        callbackImplementation.setBody(bodyBuilder.toString());
        generatedMethodCallbackClass.addMethod(callbackImplementation);
        Class<?> methodCallbackClass = generatedMethodCallbackClass.toClass();

        if (!MethodCallback.class.isAssignableFrom(methodCallbackClass)) {
            throw new PandaException("Cannot load prototype, internal error - generated class is not MethodCallback");
        }

        MethodCallback<?> methodCallback = (MethodCallback<?>) methodCallbackClass.newInstance();
        ClassPrototypeReference[] parameterTypes = ClassPrototypeGeneratorUtils.toTypes(prototype.getModule(), method.getParameterTypes());

        PandaMethod pandaMethod = PandaMethod.builder()
                .methodName(method.getName())
                .prototype(prototype.getReference())
                .returnType(PandaTypes.VOID.getReference()) // TODO: Proxy or sth
                .isStatic(methodDeclaration.isStatic())
                .visibility(methodDeclaration.visibility())
                .methodBody(methodCallback)
                .parameterTypes(parameterTypes)
                .catchAllParameters(methodDeclaration.catchAllParameters())
                .build();

        prototype.getMethods().registerMethod(pandaMethod);
    }

}
