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
import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeMethod;
import org.panda_lang.utilities.commons.ClassUtils;

final class AssociatedClassGenerator {

    private static final ClassPool CLASS_POOL = ClassPool.getDefault();

    private final Prototype prototype;
    private final boolean locked;
    private Class<?> currentClass;

    AssociatedClassGenerator(Prototype prototype, @Nullable Class<?> predefinedClass) {
        this.prototype = prototype;
        this.currentClass = predefinedClass;
        this.locked = predefinedClass != null;
    }

    void regenerate() throws Exception {
        if (locked) {
            return;
        }

        if (ClassUtils.exists(prototype.getSimpleName())) {
            this.currentClass = Class.forName(prototype.getSimpleName());
            return;
        }

        CtClass generatedClass = CLASS_POOL.makeClass(prototype.getSimpleName());

        for (PrototypeMethod method : prototype.getMethods().getProperties()) {
            System.out.println(method.isNative());
        }

        this.currentClass = generatedClass.toClass();
    }

    Class<?> getCurrentClass() {
        return currentClass;
    }
    
}
