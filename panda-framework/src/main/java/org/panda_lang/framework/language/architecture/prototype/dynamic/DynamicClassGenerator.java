/*
 * Copyright (c) 2015-2020 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.framework.language.architecture.prototype.dynamic;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;
import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeMethod;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.language.architecture.prototype.PrototypeScope.PrototypeFrame;
import org.panda_lang.framework.language.architecture.prototype.utils.TypedUtils;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.language.runtime.PandaProcessStack;
import org.panda_lang.framework.language.runtime.PandaRuntimeException;
import org.panda_lang.utilities.commons.ClassPoolUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

public final class DynamicClassGenerator {

    private static final AtomicInteger ID = new AtomicInteger();
    private static final ClassPool CLASS_POOL = ClassPool.getDefault();
    private static final CtClass FRAME;

    static {
        try {
            FRAME = ClassPoolUtils.get(PrototypeFrame.class);
        } catch (NotFoundException e) {
            throw new PandaFrameworkException(e);
        }
    }

    public Class<?> generate(Context context, Prototype prototype) throws Exception {
        String className = prototype.getAssociatedClass().getSimpleName();
        boolean isInterface = prototype.getModel().equalsIgnoreCase("interface");

        List<CtClass> interfaces = new ArrayList<>();
        CtClass superclass = ClassPoolUtils.get(prototype.getAssociatedClass().getImplementation());

        if (superclass.isInterface()) {
            if (!superclass.getName().equals(Object.class.getName())) {
                interfaces.add(superclass);
            }

            superclass = null;
        }

        for (Prototype basis : prototype.getBases()) {
            if (basis.getModel().equalsIgnoreCase("interface")) {
                interfaces.add(ClassPoolUtils.get(basis.getAssociatedClass().getImplementation()));
                continue;
            }

            if (!basis.getModel().equalsIgnoreCase("class")) {
                continue;
            }

            if (superclass != null) {
                throw new PandaParserFailure(context, "Cannot extend more than one class");
            }

            if (isInterface && basis.getAssociatedClass().getImplementation().getName().equals(Object.class.getName())) {
                continue;
            }

            superclass = ClassPoolUtils.get(basis.getAssociatedClass().getImplementation());
        }

        CtClass generated;

        if (prototype.getModel().equalsIgnoreCase("class")) {
            generated = CLASS_POOL.makeClass(className + ID.incrementAndGet(), superclass);
        }
        else if (isInterface) {
            generated = CLASS_POOL.makeInterface(className + ID.incrementAndGet(), superclass);

            if (superclass != null) {
                throw new PandaRuntimeException("Interface cannot extend class");
            }
        }
        else {
            throw new PandaRuntimeException("Unsupported model: " + prototype.getModel());
        }

        interfaces.forEach(generated::addInterface);

        if (!isInterface) {
            for (CtField field : FRAME.getFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                CtField copy = new CtField(field.getType(), field.getName(), generated);
                copy.setModifiers(field.getModifiers());
                generated.addField(copy);
            }

            for (CtConstructor constructor : FRAME.getConstructors()) {
                generated.addConstructor(CtNewConstructor.copy(constructor, generated, null));
            }
        }

        for (CtMethod method : FRAME.getMethods()) {
            if (Modifier.isFinal(method.getModifiers())) {
                continue;
            }

            CtMethod copy = CtNewMethod.copy(method, generated, null);

            if (isInterface) {
                copy.setModifiers(copy.getModifiers() & ~Modifier.NATIVE);
                copy.setModifiers(copy.getModifiers() & ~Modifier.SYNCHRONIZED);
                copy.setModifiers(copy.getModifiers() & ~Modifier.NATIVE);
                copy.setModifiers(AccessFlag.setPublic(copy.getModifiers()));
            }

            generated.addMethod(copy);
        }

        CtClass methodClass = ClassPoolUtils.get(PrototypeMethod.class);
        Map<String, PrototypeMethod> methods = new HashMap<>();

        for (PrototypeMethod method : prototype.getMethods().getDeclaredProperties()) {
            if (!method.isNative()) {
                continue;
            }

            String generatedName = method.getName().replaceAll("[:,-.() ]", "");
            methods.put(generatedName, method);

            CtField methodField = new CtField(methodClass, generatedName, generated);
            methodField.setModifiers(Modifier.PUBLIC | Modifier.STATIC);
            generated.addField(methodField);

            CtClass returnType = ClassPoolUtils.get(method.getType().getAssociatedClass().getImplementation());
            CtClass[] parameters = ClassPoolUtils.toCtClasses(TypedUtils.toClasses(method.getParameterTypes()));

            CtMethod nativeMethod = new CtMethod(returnType, method.getSimpleName(), parameters, generated);
            nativeMethod.setBody("{ " +
                    PandaProcessStack.class.getName() + " stack = new " + PandaProcessStack.class.getName() + "(this._panda_get_process(), 1);" +
                    "return ($r) " + generatedName + ".invoke(stack, $0, $args);" +
            "}");

            generated.addMethod(nativeMethod);
        }

        Class<?> generatedClass = ClassPoolUtils.toClass(generated);

        for (Entry<String, PrototypeMethod> entry : methods.entrySet()) {
            Field methodField = generatedClass.getDeclaredField(entry.getKey());
            methodField.set(null, entry.getValue());
        }

        return generatedClass;
    }
    
}
