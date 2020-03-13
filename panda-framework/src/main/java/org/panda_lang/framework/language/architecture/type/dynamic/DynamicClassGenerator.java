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

package org.panda_lang.framework.language.architecture.type.dynamic;

import io.vavr.collection.Stream;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.type.DynamicClass;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.architecture.type.TypeConstructor;
import org.panda_lang.framework.design.architecture.type.TypeMethod;
import org.panda_lang.framework.design.architecture.type.TypeModels;
import org.panda_lang.framework.language.architecture.type.BaseCall;
import org.panda_lang.framework.language.architecture.type.TypeFrame;
import org.panda_lang.framework.language.architecture.type.TypeInstance;
import org.panda_lang.framework.language.architecture.type.utils.TypedUtils;
import org.panda_lang.framework.language.runtime.PandaProcessStack;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.ClassPoolUtils;
import org.panda_lang.utilities.commons.javassist.CtCode;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

final class DynamicClassGenerator {

    private static final CtClass CT_OBJECT = ClassPoolUtils.require(Object.class);
    private static final CtClass CT_TYPE_FRAME = ClassPoolUtils.require(TypeFrame.class);
    private static final CtClass CT_TYPE_INSTANCE = ClassPoolUtils.require(TypeInstance.class);
    private static final CtClass CT_TYPE_METHOD = ClassPoolUtils.require(TypeMethod.class);

    private final Type type;
    private final CtClass generatedStructure;
    private boolean isInterface;

    DynamicClassGenerator(Type type, CtClass generatedStructure) {
        this.type = type;
        this.generatedStructure = generatedStructure;
        this.isInterface = TypeModels.isInterface(type);
    }

    protected void generateDeclaration() {
        generatedStructure.addInterface(CT_TYPE_INSTANCE);
    }

    protected void generateFields() throws CannotCompileException {
        if (isInterface) {
            return;
        }

        CtField frameField = new CtField(CT_TYPE_FRAME, "__panda__frame", generatedStructure);
        frameField.setModifiers(Modifier.PRIVATE);
        generatedStructure.addField(frameField);
    }


    protected void generateConstructor() throws CannotCompileException {
        if (isInterface) {
            return;
        }

        for (TypeConstructor constructor : type.getConstructors().getDeclaredProperties()) {
            CtClass[] constructorParameters = ArrayUtils.merge(CT_TYPE_FRAME, ClassPoolUtils.require(constructor.getJavaParameterTypes()), CtClass[]::new);
            CtConstructor typeConstructor = new CtConstructor(constructorParameters, generatedStructure);
            StringBuilder constructorBody = new StringBuilder("{ ");

            Class<?>[] types = constructor.getBaseCall()
                    .map(BaseCall::getArguments)
                    .toStream()
                    .flatMap(Stream::of)
                    .map(Expression::getType)
                    .map(Type::getAssociatedClass)
                    .map(DynamicClass::fetchStructure)
                    .toJavaArray(Class[]::new);

            constructor.getBaseCall().peek(parameters -> {
                constructorBody.append("super(");

                type.getSuperclass()
                        .filterNot(Type::isNative)
                        .peek(supertype -> {
                            constructorBody.append("$1, ");
                        });

                for (int index = 0; index < types.length; index++) {
                    constructorBody.append("(").append(types[index].getName()).append(") $1.getBase()[").append(index).append("], ");
                }

                if (constructorBody.toString().endsWith(", ")) {
                    constructorBody.setLength(constructorBody.length() - 2);
                }

                constructorBody.append(");");
            });

            constructorBody.append("$0.__panda__frame = $1; }");
            // System.out.println(constructorBody);

            typeConstructor.setBody(constructorBody.toString());
            generatedStructure.addConstructor(typeConstructor);
        }
    }

    protected void generateInstanceMethods() throws CannotCompileException {
        if (isInterface) {
            return;
        }

        /*
        CtMethod setter = new CtMethod(CT_VOID, "__panda__set_frame", new CtClass[]{ CT_TYPE_FRAME }, generatedStructure);
        setter.setBody("{ $0.__panda__frame = $1; }");
        generatedStructure.addMethod(setter);
         */

        CtMethod getter = new CtMethod(CT_TYPE_FRAME, "__panda__get_frame", new CtClass[0], generatedStructure);
        getter.setBody("{ return $0.__panda__frame; }");
        generatedStructure.addMethod(getter);
    }

    protected Class<?> generate() throws Exception {
        Map<String, TypeMethod> methods = new HashMap<>();

        for (TypeMethod method : type.getMethods().getDeclaredProperties()) {
            if (isInterface) {
                continue;
            }

            String generatedName = method.getName().replaceAll("[:,-.#\\[\\]() ]", "");
            methods.put(generatedName, method);

            CtField methodField = new CtField(CT_TYPE_METHOD, generatedName, generatedStructure);
            methodField.setModifiers(Modifier.PUBLIC | Modifier.STATIC);
            generatedStructure.addField(methodField);

            CtClass returnType = ClassPoolUtils.get(method.getReturnType().getAssociatedClass().fetchStructure());
            CtClass[] parameters = ClassPoolUtils.toCtClasses(TypedUtils.toClasses(method.getParameterTypes()));

            CtMethod nativeMethod = CtCode.of(new CtMethod(returnType, method.getSimpleName(), parameters, generatedStructure))
                    .alias("{ProcessStack}", PandaProcessStack.class.getName())
                    .alias("{generatedName}", generatedName)
                    .compile(
                            "{ProcessStack} stack = new {ProcessStack}($0.__panda__get_frame().getProcess(), 1); \n",
                            "return ($r) {generatedName}.invoke(stack, $0, $args);"
                    );

            generatedStructure.addMethod(nativeMethod);
        }

        Class<?> generatedClass = ClassPoolUtils.toClass(generatedStructure);

        for (Entry<String, TypeMethod> entry : methods.entrySet()) {
            Field methodField = generatedClass.getDeclaredField(entry.getKey());
            methodField.set(null, entry.getValue());
        }

        return generatedClass;
    }
    
}
