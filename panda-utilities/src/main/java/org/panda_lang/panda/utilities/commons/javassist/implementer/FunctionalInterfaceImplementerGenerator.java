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

package org.panda_lang.panda.utilities.commons.javassist.implementer;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.panda_lang.panda.utilities.commons.ClassPoolUtils;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

final class FunctionalInterfaceImplementerGenerator {

    private final String name;
    private final Class<?> anInterface;
    private final LinkedHashMap<String, CtClass> parameters;
    private final String body;

    FunctionalInterfaceImplementerGenerator(String name, Class<?> anInterface, LinkedHashMap<String, CtClass> parameters, String body) {
        this.name = name;
        this.anInterface = anInterface;
        this.parameters = parameters;
        this.body = body;
    }

    public Class<?> generate() throws NotFoundException, CannotCompileException {
        Method[] methods = anInterface.getDeclaredMethods();

        if (methods.length != 1) {
            throw new IllegalArgumentException("The given class cannot be functional interface");
        }

        CtClass ctClass = ClassPool.getDefault().makeClass(name);
        ctClass.addInterface(ClassPoolUtils.get(anInterface));

        for (Map.Entry<String, CtClass> entry : parameters.entrySet()) {
            CtField field = new CtField(entry.getValue(), entry.getKey(), ctClass);
            ctClass.addField(field);
        }

        CtConstructor constructor = new CtConstructor(parameters.values().toArray(new CtClass[0]), ctClass);
        StringBuilder constructorBody = new StringBuilder("{");
        int index = 1; // $0 == this

        for (Map.Entry<String, CtClass> entry : parameters.entrySet()) {
            constructorBody.append("this.").append(entry.getKey()).append(" = $").append(index++).append(";");
        }

        constructor.setBody(constructorBody.append("}").toString());
        ctClass.addConstructor(constructor);

        Method methodToGenerate = methods[0];
        CtMethod method = new CtMethod(ClassPoolUtils.get(methodToGenerate.getReturnType()), methodToGenerate.getName(), ClassPoolUtils.toCtClasses(methodToGenerate.getParameterTypes()), ctClass);
        method.setBody("{ " + body + " }");
        ctClass.addMethod(method);

        return ctClass.toClass();
    }

}
