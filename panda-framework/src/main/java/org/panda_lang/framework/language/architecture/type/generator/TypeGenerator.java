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

package org.panda_lang.framework.language.architecture.type.generator;

import io.vavr.control.Option;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.architecture.type.TypeModels;
import org.panda_lang.framework.design.architecture.type.Reference;
import org.panda_lang.framework.design.architecture.type.State;
import org.panda_lang.framework.design.architecture.type.Visibility;
import org.panda_lang.framework.language.architecture.type.PandaType;
import org.panda_lang.framework.language.architecture.type.PandaReference;
import org.panda_lang.framework.language.architecture.type.dynamic.PandaDynamicClass;
import org.panda_lang.framework.language.interpreter.source.PandaClassSource;
import org.panda_lang.utilities.commons.ClassUtils;
import org.panda_lang.utilities.commons.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

final class TypeGenerator {

    protected final Map<String, Reference> cachedReferences = new HashMap<>();

    protected Reference generate(Module module, String name, Class<?> javaType) {
        Reference reference = cachedReferences.get(getId(module, name));

        if (reference == null) {
            reference = module.forClass(javaType).getOrElse((Reference) null);
        }

        if (reference != null && reference.getAmountOfInitializers() > 1 && reference.isInitialized()) {
            return reference;
        }

        if (reference == null) {
            reference = new PandaReference(new PandaDynamicClass(javaType, name, module.getName()), module, ref -> PandaType.builder()
                    .name(name)
                    .reference(ref)
                    .module(module)
                    .associated(ref.getAssociatedClass())
                    .location(new PandaClassSource(javaType).toLocation())
                    .associated(ref.getAssociatedClass())
                    .model(TypeModels.of(javaType))
                    .state(State.of(javaType))
                    .visibility(Visibility.PUBLIC)
                    .build()
            );
        }

        Consumer<Type> initializer = type -> {
            if (!Modifier.isPublic(javaType.getModifiers())) {
                return;
            }

            for (Field field : javaType.getFields()) {
                if (!Modifier.isPublic(field.getModifiers())) {
                    continue;
                }

                FieldGenerator generator = new FieldGenerator(this, type, field);
                type.getFields().declare(field.getName(), generator::generate);
            }

            for (Constructor<?> constructor : ReflectionUtils.getByModifier(javaType.getConstructors(), Modifier.PUBLIC)) {
                ConstructorGenerator generator = new ConstructorGenerator(type, constructor);
                type.getConstructors().declare(name, generator::generate);
            }

            for (Method method : ReflectionUtils.getByModifier(javaType.getMethods(), Modifier.PUBLIC)) {
                MethodGenerator generator = new MethodGenerator(this, type, method);
                type.getMethods().declare(method.getName(), generator::generate);
            }
        };

        reference.addInitializer(initializer);

        if (reference.isInitialized()) {
            initializer.accept(reference.fetch());
        }

        cachedReferences.put(getId(module, name), reference);
        return reference;
    }

    protected Reference findOrGenerate(Module module, Class<?> type) {
        if (type.isPrimitive()) {
            type = ClassUtils.getNonPrimitiveClass(type);
        }

        Option<Reference> referenceValue = module.getModuleLoader().forClass(type);

        if (referenceValue.isDefined()) {
            return referenceValue.get();
        }

        Reference reference = cachedReferences.get(getId(module, type.getSimpleName()));

        if (reference != null) {
            return reference;
        }

        return generate(module, type.getSimpleName(), type);
    }

    protected void disposeCache() {
        cachedReferences.clear();
    }

    private String getId(Module module, String name) {
        return module.getName() + "::" + name;
    }

}
