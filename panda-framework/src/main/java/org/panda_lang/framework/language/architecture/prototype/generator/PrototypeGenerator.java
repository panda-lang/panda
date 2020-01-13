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

package org.panda_lang.framework.language.architecture.prototype.generator;

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeModels;
import org.panda_lang.framework.design.architecture.prototype.Reference;
import org.panda_lang.framework.design.architecture.prototype.State;
import org.panda_lang.framework.design.architecture.prototype.Visibility;
import org.panda_lang.framework.language.architecture.prototype.PandaPrototype;
import org.panda_lang.framework.language.architecture.prototype.PandaReference;
import org.panda_lang.framework.language.architecture.prototype.dynamic.PandaDynamicClass;
import org.panda_lang.framework.language.interpreter.source.PandaClassSource;
import org.panda_lang.utilities.commons.ClassUtils;
import org.panda_lang.utilities.commons.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

final class PrototypeGenerator {

    protected final Map<String, Reference> cachedReferences = new HashMap<>();

    protected Reference generate(Module module, String name, Class<?> type) {
        // System.out.println("Request for " + name + " as " + type);
        Reference reference = cachedReferences.get(getId(module, name));

        if (reference == null) {
            reference = module.forClass(type).orElse(null);
        }

        if (reference != null && reference.getAmountOfInitializers() > 1 && reference.isInitialized()) {
            // System.out.println("Exists and initialized with" + reference.fetch().getFields().getProperties());
            return reference;
        }

        if (reference == null) {
            reference = new PandaReference(new PandaDynamicClass(type, name, module.getName()), module, ref -> PandaPrototype.builder()
                    .name(name)
                    .reference(ref)
                    .module(module)
                    .associated(ref.getAssociatedClass())
                    .location(new PandaClassSource(type).toLocation())
                    .associated(ref.getAssociatedClass())
                    .model(PrototypeModels.of(type))
                    .state(State.of(type))
                    .visibility(Visibility.PUBLIC)
                    .build()
            );
        }

        Consumer<Prototype> initializer = prototype -> {
            if (!Modifier.isPublic(type.getModifiers())) {
                return;
            }

            for (Field field : type.getFields()) {
                if (!Modifier.isPublic(field.getModifiers())) {
                    continue;
                }

                FieldGenerator generator = new FieldGenerator(this, prototype, field);
                prototype.getFields().declare(field.getName(), generator::generate);
            }

            for (Constructor<?> constructor : ReflectionUtils.getByModifier(type.getConstructors(), Modifier.PUBLIC)) {
                ConstructorGenerator generator = new ConstructorGenerator(prototype, constructor);
                prototype.getConstructors().declare(name, generator::generate);
            }

            for (Method method : ReflectionUtils.getByModifier(type.getMethods(), Modifier.PUBLIC)) {
                MethodGenerator generator = new MethodGenerator(this, prototype, method);
                prototype.getMethods().declare(method.getName(), generator::generate);
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

        Optional<Reference> referenceValue = module.getModuleLoader().forClass(type);

        if (referenceValue.isPresent()) {
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
