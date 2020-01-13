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

package org.panda_lang.framework.design.architecture.prototype;

/**
 * Wrapper for Java class
 */
public interface DynamicClass {

    void regenerate() throws Exception;

    void append(Class<?> dynamicClass);

    void extendClass(DynamicClass superclass);

    void implementInterface(DynamicClass interfaceClass);

    default boolean isAssignableFrom(DynamicClass cls) {
        return isAssignableFrom(cls.getImplementation());
    }

    boolean isAssignableFrom(Class<?> cls);

    boolean isInterface();

    boolean isClass();

    Class<?> getImplementation();

    Class<?> getStructure();

    String getModel();

    String getModule();

    String getSimpleName();

    String getName();

}
