/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.framework.language.architecture.prototype;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeMethod;
import org.panda_lang.framework.design.runtime.Process;
import org.panda_lang.framework.language.architecture.prototype.utils.TypedUtils;
import org.panda_lang.framework.language.runtime.PandaProcessStack;
import org.panda_lang.utilities.commons.ClassPoolUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

public final class AssociatedClassGenerator {

    private static final AtomicInteger ID = new AtomicInteger();
    private static final ClassPool CLASS_POOL = ClassPool.getDefault();

    public Class<?> generate(Prototype prototype) throws Exception {
        String className = prototype.getAssociatedClass().getSimpleName();

        CtClass superclass = ClassPoolUtils.get(prototype.getAssociatedClass().getImplementation());
        CtClass classPrototype = CLASS_POOL.makeClass(className + ID.incrementAndGet(), superclass);

        CtConstructor constructor = new CtConstructor(ClassPoolUtils.toCtClasses(PrototypeScope.class, Process.class), classPrototype);
        constructor.setBody("super($$);");
        classPrototype.addConstructor(constructor);

        CtClass methodClass = ClassPoolUtils.get(PrototypeMethod.class);
        Map<String, PrototypeMethod> methods = new HashMap<>();

        for (PrototypeMethod method : prototype.getMethods().getDeclaredProperties()) {
            if (!method.isNative()) {
                continue;
            }

            String generatedName = method.getPropertyName().replaceAll("[:,-.() ]", "");
            methods.put(generatedName, method);

            CtField methodField = new CtField(methodClass, generatedName, classPrototype);
            methodField.setModifiers(Modifier.PUBLIC | Modifier.STATIC);
            classPrototype.addField(methodField);

            CtClass returnType = ClassPoolUtils.get(method.getType().getAssociatedClass().getImplementation());
            CtClass[] parameters = ClassPoolUtils.toCtClasses(TypedUtils.toClasses(method.getParameterTypes()));

            CtMethod nativeMethod = new CtMethod(returnType, method.getSimpleName(), parameters, classPrototype);
            nativeMethod.setBody("{ " +
                    PandaProcessStack.class.getName() + " stack = new " + PandaProcessStack.class.getName() + "(this.getProcess(), 1);" +
                    "return ($r) " + generatedName + ".invoke(stack, $0, $args);" +
            "}");

            classPrototype.addMethod(nativeMethod);
        }

        Class<?> generatedClass = classPrototype.toClass();

        for (Entry<String, PrototypeMethod> entry : methods.entrySet()) {
            Field methodField = generatedClass.getDeclaredField(entry.getKey());
            methodField.set(null, entry.getValue());
        }

        return generatedClass;
    }
    
}
