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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;

final class ConstructorInjection {

    private final InjectorProcessor processor;

    ConstructorInjection(Injector injector) {
        this.processor = new InjectorProcessor(injector);
    }

    @SuppressWarnings("unchecked")
    public <T> T invoke(Class<T> type) throws InstantiationException, IllegalAccessException, InvocationTargetException, InjectorException {
        if (type.getDeclaredConstructors().length != 1) {
            throw new InvalidParameterException("Class has to contain one and only constructor");
        }

        Constructor<?> constructor = type.getDeclaredConstructors()[0];
        constructor.setAccessible(true);

        return (T) constructor.newInstance(processor.fetchValues(constructor));
    }

}
