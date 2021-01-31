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

package org.panda_lang.framework.architecture.type;

/**
 * Class with dynamic implementation
 */
public interface DynamicClass {

    DynamicClass append(Type type);

    DynamicClass extendClass(DynamicClass superclass);

    DynamicClass implementInterface(DynamicClass interfaceClass);

    Class<?> fetchImplementation();

    Class<?> fetchStructure();

    default boolean isAssignableFrom(DynamicClass cls) {
        return isAssignableFrom(cls.fetchStructure());
    }

    boolean isAssignableFrom(Class<?> cls);

    boolean isAssignableTo(Class<?> cls);

    boolean isInterface();

    boolean isClass();

    String getModel();

    String getModule();

    String getSimpleName();

    String getName();

}
