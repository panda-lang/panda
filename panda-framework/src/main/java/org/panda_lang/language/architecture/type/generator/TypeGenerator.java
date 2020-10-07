/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.language.architecture.type.generator;

import org.panda_lang.language.FrameworkController;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.language.architecture.module.Module;
import org.panda_lang.language.architecture.module.TypeLoader;
import org.panda_lang.language.architecture.type.State;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.Kind;
import org.panda_lang.language.architecture.type.Visibility;
import org.panda_lang.language.architecture.type.PandaType;
import org.panda_lang.language.interpreter.source.PandaClassSource;
import org.panda_lang.utilities.commons.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

final class TypeGenerator {

    protected final Map<String, Type> initializedTypes = new HashMap<>();
    protected final FrameworkController frameworkController;

    TypeGenerator(FrameworkController frameworkController) {
        this.frameworkController = frameworkController;
    }

    protected Type generate(Module module, String name, Class<?> javaType) {
        String identifier = getId(module, name);

        return Option.of(initializedTypes.get(identifier))
                .orElse(() -> module.get(javaType))
                .orElseGet(() -> {
                    Type type = PandaType.builder()
                            .name(name)
                            .module(module)
                            .associatedType(javaType)
                            .isNative(true)
                            .location(new PandaClassSource(javaType).toLocation())
                            .kind(Kind.of(javaType))
                            .state(State.of(javaType))
                            .visibility(Visibility.OPEN)
                            .build();

                    type.addInitializer((typeLoader, initializedType) -> {
                        Class<?> baseClass = javaType.getSuperclass();

                        // Object.class does not have supertype
                        if (baseClass != null) {
                            type.addBase(typeLoader.load(findOrGenerate(typeLoader, module, baseClass)));
                        }

                        for (Class<?> javaInterface : javaType.getInterfaces()) {
                            type.addBase(typeLoader.load(findOrGenerate(typeLoader, module, javaInterface)));
                        }

                        if (!Modifier.isPublic(javaType.getModifiers())) {
                            return;
                        }

                        for (Field field : javaType.getDeclaredFields()) {
                            if (!Modifier.isPublic(field.getModifiers())) {
                                continue;
                            }

                            FieldGenerator generator = new FieldGenerator(this, initializedType, field);
                            initializedType.getFields().declare(field.getName(), () -> generator.generate(typeLoader));
                        }

                        for (Constructor<?> constructor : ReflectionUtils.getByModifier(javaType.getDeclaredConstructors(), Modifier.PUBLIC)) {
                            ConstructorGenerator generator = new ConstructorGenerator(initializedType, constructor);
                            initializedType.getConstructors().declare(name, () -> generator.generate(typeLoader));
                        }

                        for (Method method : ReflectionUtils.getByModifier(javaType.getDeclaredMethods(), Modifier.PUBLIC)) {
                            MethodGenerator generator = new MethodGenerator(frameworkController, this, initializedType, method);
                            initializedType.getMethods().declare(method.getName(), () -> generator.generate(typeLoader));
                        }
                    });

                    initializedTypes.put(identifier, type);
                    return type;
                });
    }

    protected Type findOrGenerate(TypeLoader typeLoader, Module module, Class<?> javaType) {
        /*if (javaType.isPrimitive()) {
            javaType = ClassUtils.getNonPrimitiveClass(javaType);
        }*/

        Option<Type> typeValue = typeLoader.forClass(javaType);

        if (typeValue.isDefined()) {
            return typeValue.get();
        }

        Type type = initializedTypes.get(getId(module, javaType.getSimpleName()));

        if (type != null) {
            return type;
        }

        return generate(module, javaType.getSimpleName(), javaType);
    }

    protected void disposeCache() {
        initializedTypes.clear();
    }

    private String getId(Module module, String name) {
        return module.getName() + "::" + name;
    }

}
