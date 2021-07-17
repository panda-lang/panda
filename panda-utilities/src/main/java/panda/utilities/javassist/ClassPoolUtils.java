/*
 * Copyright (c) 2021 dzikoysk
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

package panda.utilities.javassist;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.util.Arrays;

public final class ClassPoolUtils {

    private static final ClassPool CLASS_POOL = ClassPool.getDefault();
    private static final double VERSION = Double.parseDouble(System.getProperty("java.specification.version"));

    static {
        CLASS_POOL.insertClassPath(new ClassClassPath(ClassPoolUtils.class));
    }

    private ClassPoolUtils() { }

    public static CtClass[] toCt(Class<?>... classes) {
        CtClass[] ctClasses = new CtClass[classes.length];

        for (int index = 0; index < classes.length; index++) {
            ctClasses[index] = require(classes[index]);
        }

        return ctClasses;
    }

    public static Class<?> toClass(CtClass clazz, Class<?> domainClass) throws CannotCompileException {
        if (VERSION >= 11) {
            return clazz.toClass(domainClass);
        }
        else {
            return clazz.toClass();
        }
    }

    /**
     * Get CtClass using the default class pool.
     * The method throws {@link ClassPoolUtils.ClassPoolException} with a default message if class cannot be located in the pool
     *
     * @param clazz the class to get
     * @return class representation
     */
    public static CtClass require(Class<?> clazz) {
        try {
            return get(clazz);
        } catch (NotFoundException notFoundException) {
            throw new ClassPoolException("Cannot find class " + clazz);
        }
    }

    /**
     * Map array of classes using the {@link ClassPoolUtils#require(Class)}
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
     * Get initialized class pool
     *
     * @return the initialized class pool
     */
    public static ClassPool getClassPool() {
        return CLASS_POOL;
    }

    /**
     * Default runtime exception thrown by {@link ClassPoolUtils} utilities
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
