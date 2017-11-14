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

import org.panda_lang.panda.language.structure.prototype.structure.method.MethodVisibility;
import org.panda_lang.panda.util.ReflectionsUtils;
import org.reflections.Reflections;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public interface ClassPrototypeModel {

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface PandaModule {
        String value();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface PandaClass {
        String value();
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface PandaMethod {
        MethodVisibility visibility();
        boolean isStatic();
        String returnType();
    }

    @SuppressWarnings("unchecked")
    static Collection<Class<? extends ClassPrototypeModel>> of(String moduleName) {
        Reflections reflections = new Reflections(ReflectionsUtils.REFLECTIONS_CONFIG);
        Set<Class<? extends ClassPrototypeModel>> annotated = reflections.getSubTypesOf(ClassPrototypeModel.class);
        Collection<Class<? extends ClassPrototypeModel>> classes = new ArrayList<>();

        for (Class<? extends ClassPrototypeModel> clazz : annotated) {
            PandaModule module = clazz.getAnnotation(PandaModule.class);

            if (!module.value().equals(moduleName)) {
                continue;
            }

            classes.add(clazz);
        }

        return classes;
    }

}
