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
import org.panda_lang.language.architecture.module.Module;
import org.panda_lang.language.architecture.module.TypeLoader;
import org.panda_lang.language.architecture.type.Kind;
import org.panda_lang.language.architecture.type.PandaType;
import org.panda_lang.language.architecture.type.Reference;
import org.panda_lang.language.architecture.type.State;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.Visibility;
import org.panda_lang.language.architecture.type.signature.Relation;
import org.panda_lang.language.architecture.type.signature.Signature;
import org.panda_lang.language.architecture.type.signature.TypedSignature;
import org.panda_lang.language.interpreter.source.PandaClassSource;
import org.panda_lang.language.interpreter.token.PandaSnippet;
import org.panda_lang.utilities.commons.ClassUtils;
import org.panda_lang.utilities.commons.ReflectionUtils;
import org.panda_lang.utilities.commons.function.Completable;
import org.panda_lang.utilities.commons.function.Option;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public final class TypeGenerator {

    protected final Map<Class<?>, Type> initializedTypes = new HashMap<>();
    protected final FrameworkController frameworkController;

    public TypeGenerator(FrameworkController frameworkController) {
        this.frameworkController = frameworkController;
    }

    public Type allocate(Class<?> javaType, Type type) {
        initializedTypes.put(javaType, type);
        return type;
    }

    public Reference generate(Module module, String name, Class<?> javaType) {
        return Option.of(initializedTypes.get(javaType))
                .map(Reference::new)
                .orElse(() -> module.get(name))
                .orElseGet(() -> {
                    Completable<Type> completableType = new Completable<>();
                    Reference reference = new Reference(completableType, module, name, Visibility.OPEN, Kind.of(javaType), new PandaClassSource(javaType).toLocation());

                    Type type = PandaType.builder()
                            .name(name)
                            .module(module)
                            .associatedType(Completable.completed(javaType))
                            .isNative(true)
                            .signature(new TypedSignature(null, reference, new Signature[0], Relation.DIRECT, PandaSnippet.empty()))
                            .location(reference.getLocation())
                            .kind(reference.getKind())
                            .state(State.of(javaType))
                            .visibility(Visibility.OPEN)
                            .build();

                    type.addInitializer((typeLoader, initializedType) -> {
                        Class<?> baseClass = javaType.getSuperclass();

                        // Object.class does not have supertype
                        if (baseClass != null) {
                            type.addBase(typeLoader.load(findOrGenerate(typeLoader, module, baseClass)).getSignature());
                        }

                        for (Class<?> javaInterface : javaType.getInterfaces()) {
                            type.addBase(typeLoader.load(findOrGenerate(typeLoader, module, javaInterface)).getSignature());
                        }

                        if (!javaType.equals(Object.class) && type.getBases().isEmpty()) {
                            // type.addAutocast(typeLoader.requireType("panda::Object"), (originalType, object, resultType) -> object);
                            type.addBase(typeLoader.requireType("panda::Object").getSignature());
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
                            ConstructorGenerator generator = new ConstructorGenerator(this, initializedType, constructor);
                            initializedType.getConstructors().declare(constructor.toString(), () -> generator.generate(typeLoader));
                        }

                        for (Method method : ReflectionUtils.getByModifier(javaType.getDeclaredMethods(), Modifier.PUBLIC)) {
                            MethodGenerator generator = new MethodGenerator(frameworkController, this, initializedType, method);
                            initializedType.getMethods().declare(method.getName(), () -> generator.generate(typeLoader));
                        }
                    });

                    completableType.complete(allocate(javaType, type));
                    return type.getReference();
                });
    }

    protected Type findOrGenerate(TypeLoader typeLoader, Module module, Class<?> javaType) {
        if (javaType.isPrimitive()) {
            javaType = ClassUtils.getNonPrimitiveClass(javaType);
        }

        Option<Type> typeValue = typeLoader.forJavaType(javaType);

        if (typeValue.isDefined()) {
            return typeValue.get();
        }

        Type type = initializedTypes.get(javaType);

        if (type != null) {
            return type;
        }

        return generate(module, javaType.getSimpleName(), javaType).fetchType();
    }

}
