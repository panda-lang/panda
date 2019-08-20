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

package org.panda_lang.panda.utilities.commons;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public final class ClassPoolUtils {

    private static final ClassPool CLASS_POOL = ClassPool.getDefault();

    private ClassPoolUtils() { }

    public static CtClass[] toCtClasses(Class<?>[] classes) throws NotFoundException {
        CtClass[] ctClasses = new CtClass[classes.length];

        for (int index = 0; index < classes.length; index++) {
            ctClasses[index] = get(classes[index]);
        }

        return ctClasses;
    }

    public static CtClass get(Class<?> clazz) throws NotFoundException {
        return CLASS_POOL.get(clazz.getName());
    }

}
