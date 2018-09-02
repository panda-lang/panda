/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.architecture.prototype.registry;

import org.panda_lang.panda.framework.PandaFramework;
import org.panda_lang.panda.framework.design.architecture.prototype.method.MethodVisibility;
import org.panda_lang.panda.util.PandaUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collection;

public interface ClassPrototypeModel {

    @SuppressWarnings("unchecked")
    static Collection<Class<? extends ClassPrototypeModel>> of(String moduleName) {
        PandaFramework.getLogger().debug("Looking for subtypes of ClassPrototypeModel");

        Collection<Class<? extends ClassPrototypeModel>> classes = new ArrayList<>();
        Collection<Class<? extends ClassPrototypeModel>> models = PandaUtils.DEFAULT_PANDA_SCANNER.createSelector().selectSubtypesOf(ClassPrototypeModel.class);

        for (Class<? extends ClassPrototypeModel> clazz : models) {
            ModuleDeclaration module = clazz.getAnnotation(ModuleDeclaration.class);

            if (!module.value().equals(moduleName)) {
                continue;
            }

            classes.add(clazz);
        }

        PandaFramework.getLogger().debug("Subtypes: " + classes.size());
        return classes;
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ModuleDeclaration {

        String value();

    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ClassDeclaration {

        String value();

    }

    @Target(ElementType.CONSTRUCTOR)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ConstructorDeclaration {

    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MethodDeclaration {

        MethodVisibility visibility() default MethodVisibility.PUBLIC;

        String returnType() default "void";

        boolean catchAllParameters() default false;

        boolean isStatic() default false;

    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @interface TypeDeclaration {

        String value();

    }

}
