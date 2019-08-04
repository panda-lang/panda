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

package org.panda_lang.panda.utilities.inject;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface Injector {

    /**
     * Create a new instance of the specified type using Injector
     *
     * @param type the class to instantiate
     * @param <T> the type
     * @return a new instance
     */
    <T> T newInstance(Class<T> type) throws InstantiationException, IllegalAccessException, InvocationTargetException, InjectorException;

    /**
     * Invoke the method using Injector
     *
     * @param method the method to invoke
     * @param instance the instance to use (nullable for static context)
     * @param <T> the return type
     * @return the return value
     */
    @Nullable <T> T invokeMethod(Method method, @Nullable Object instance) throws Throwable;

    /**
     * Get resources of injector
     *
     * @return the resources
     */
    InjectorResources getResources();

}
