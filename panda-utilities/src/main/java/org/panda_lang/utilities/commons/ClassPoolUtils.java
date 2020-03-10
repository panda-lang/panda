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

package org.panda_lang.utilities.commons;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.util.Arrays;

public final class ClassPoolUtils {

    private static final ClassPool CLASS_POOL = ClassPool.getDefault();

    private ClassPoolUtils() { }

    public static CtClass[] toCtClasses(Class<?>... classes) throws NotFoundException {
        CtClass[] ctClasses = new CtClass[classes.length];

        for (int index = 0; index < classes.length; index++) {
            ctClasses[index] = get(classes[index]);
        }

        return ctClasses;
    }

    /**
     * Compile CtClass into the proper class
     *
     * @param clazz the CtClass to compile
     * @return a result class
     */
    public static Class<?> toClass(CtClass clazz) {
        try {
            return clazz.toClass();
        } catch (CannotCompileException e) {
            throw new ClassPoolException(e.getCause());
        }
    }

    /**
     * Get CtClass using the default class pool.
     * The method throws {@link org.panda_lang.utilities.commons.ClassPoolUtils.ClassPoolException} with a default message if class cannot be located in the pool
     *
     * @param clazz the class to get
     * @return class representation
     */
    public static CtClass require(Class<?> clazz) {
        try {
            return get(clazz);
        } catch (NotFoundException e) {
            throw new ClassPoolException("Cannot find class " + clazz);
        }
    }

    /**
     * Map array of classes using the {@link org.panda_lang.utilities.commons.ClassPoolUtils#require(Class)}
     *
     * @param classes the array of classes to map
     * @return the array of ct classes
     */
    public static CtClass[] require(Class<?>[] classes) {
        return Arrays.stream(classes)
                .map(ClassPoolUtils::require)
                .toArray(CtClass[]::new);
    }

    /**
     * Get CtClass using the default class pool
     *
     * @param clazz the class to get
     * @return class representation
     * @throws NotFoundException if class cannot be located in the pool
     */
    public static CtClass get(Class<?> clazz) throws NotFoundException {
        return CLASS_POOL.get(clazz.getName());
    }

    /**
     * Default runtime exception thrown by {@link org.panda_lang.utilities.commons.ClassPoolUtils} utilities
     */
    public static class ClassPoolException extends RuntimeException {

        public ClassPoolException(String message) {
            super(message);
        }

        public ClassPoolException(Throwable cause) {
            super(cause);
        }

    }

}
