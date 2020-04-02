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

package org.panda_lang.utilities.inject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class DefaultInjector implements Injector {

    private final InjectorResources resources;

    public DefaultInjector(InjectorResources resources) {
        this.resources = resources;
    }

    @Override
    public <T> T newInstance(Class<T> type) throws InstantiationException, IllegalAccessException, InvocationTargetException, InjectorException {
        return new ConstructorInjection(this, type).invoke();
    }

    @Override
    public <T> T invokeMethod(Method method, Object instance) throws Throwable {
        return new MethodInjector(this, method).invoke(instance);
    }

    @Override
    public Injector fork(InjectorController controller) {
        return DependencyInjection.INJECTOR_FACTORY.createInjector(controller, resources.fork());
    }

    @Override
    public Injector duplicate(InjectorController controller) {
        return DependencyInjection.INJECTOR_FACTORY.createInjector(controller, resources.duplicate());
    }

    public InjectorResources getResources() {
        return resources;
    }

}
