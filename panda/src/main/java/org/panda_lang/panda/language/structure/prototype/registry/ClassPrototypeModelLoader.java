/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.language.structure.prototype.registry;

import com.sun.xml.internal.ws.util.StringUtils;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.core.structure.value.Value;
import org.panda_lang.panda.language.runtime.ExecutableBranch;
import org.panda_lang.panda.language.structure.overall.module.Module;
import org.panda_lang.panda.language.structure.overall.module.ModuleRegistry;
import org.panda_lang.panda.language.structure.prototype.registry.ClassPrototypeModel.ClassDeclaration;
import org.panda_lang.panda.language.structure.prototype.registry.ClassPrototypeModel.MethodDeclaration;
import org.panda_lang.panda.language.structure.prototype.registry.ClassPrototypeModel.ModuleDeclaration;
import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.structure.method.MethodCallback;
import org.panda_lang.panda.language.structure.prototype.structure.method.variant.PandaMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;

public class ClassPrototypeModelLoader {

    private final Panda panda;

    public ClassPrototypeModelLoader(Panda panda) {
        this.panda = panda;
    }

    public void load(Collection<Class<? extends ClassPrototypeModel>> models)  {
        try {
            loadModels(models);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadModels(Collection<Class<? extends ClassPrototypeModel>> models) throws Exception {
        ClassPool pool = ClassPool.getDefault();

        CtClass objectCtClass = pool.getCtClass(Object.class.getName());
        CtClass methodCallbackCtClass = pool.get(MethodCallback.class.getName());
        CtClass executableBranchCtClass = pool.get(ExecutableBranch.class.getName());
        CtClass valueArrayCtClass = pool.get(Value[].class.getName());

        for (Class<? extends ClassPrototypeModel> modelClass : models) {
            ModuleDeclaration moduleDeclaration = modelClass.getAnnotation(ModuleDeclaration.class);

            ModuleRegistry registry = ModuleRegistry.getDefault();
            Module defaultModule = registry.getOrCreate(moduleDeclaration.value());

            ClassDeclaration classDeclaration = modelClass.getAnnotation(ClassDeclaration.class);
            ClassPrototype prototype = new ClassPrototype(defaultModule, classDeclaration.value());
            defaultModule.add(prototype);


            for (Method method : modelClass.getMethods()) {
                MethodDeclaration methodInfo = method.getAnnotation(MethodDeclaration.class);

                if (methodInfo == null) {
                    continue;
                }

                String methodCallbackClassName = modelClass.getSimpleName() + StringUtils.capitalize(method.getName()) + "MethodCallback";
                CtClass generatedMethodCallbackClass = pool.makeClass(methodCallbackClassName);
                generatedMethodCallbackClass.addInterface(generatedMethodCallbackClass);

                Parameter parameter = method.getParameters()[1];
                CtClass parameterCtClass = pool.getCtClass(parameter.getType().getName());

                CtMethod callbackImplementation = new CtMethod(objectCtClass, "invoke", new CtClass[] { executableBranchCtClass, objectCtClass, valueArrayCtClass}, generatedMethodCallbackClass);
                if (methodInfo.isStatic()) {
                    callbackImplementation.setBody("return " + modelClass.getName() + "." + method.getName() + "($1, $2, $3);");
                } else {
                    callbackImplementation.setBody("{ " +
                            modelClass.getName() + " typedInstance = (" + modelClass.getName() + ") $2;" +
                            "return typedInstance." + method.getName() + "($1, typedInstance, $3);"
                    + " }");
                }
                generatedMethodCallbackClass.addMethod(callbackImplementation);

                Class<MethodCallback<?>> methodCallbackClass = generatedMethodCallbackClass.toClass();
                MethodCallback<?> methodCallback = methodCallbackClass.newInstance();

                PandaMethod pandaMethod = PandaMethod.builder()
                        .methodName(method.getName())
                        .prototype(prototype)
                        .isStatic(methodInfo.isStatic())
                        .visibility(methodInfo.visibility())
                        .methodBody(methodCallback)
                        .build();
                prototype.getMethods().registerMethod(pandaMethod);
            }
        }
    }

}
