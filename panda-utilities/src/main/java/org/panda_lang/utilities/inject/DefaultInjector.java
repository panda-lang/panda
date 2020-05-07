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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.utilities.commons.ObjectUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.security.InvalidParameterException;

final class DefaultInjector implements Injector {

    private final InjectorResources resources;
    private final InjectorProcessor processor;

    public DefaultInjector(InjectorResources resources) {
        this.resources = resources;
        this.processor = new InjectorProcessor(this);
    }

    @Override
    public <T> T newInstance(Class<T> type, Object... injectorArgs) throws Throwable {
        return forConstructor(type).newInstance(injectorArgs);
    }

    @Override
    public <T> ConstructorInjector<T> forConstructor(Class<T> type) {
        if (type.getDeclaredConstructors().length != 1) {
            throw new InvalidParameterException("Class has to contain one and only constructor");
        }

        return new ConstructorInjector<T>(processor, type);
    }

    @Override
    public <T> T invokeMethod(Method method, Object instance, Object... injectorArgs) throws Throwable {
        return forMethod(method).invoke(instance, injectorArgs);
    }

    @Override
    public MethodInjector forMethod(Method method) {
        return new MethodInjector(processor, method);
    }

    @Override
    public GeneratedMethodInjector forGeneratedMethod(Method method) throws Exception {
        return new GeneratedMethodInjector(processor, method);
    }

    @Override
    public <T> @Nullable T invokeParameter(Parameter parameter, Object... injectorArgs) throws Exception {
        return ObjectUtils.cast(processor.tryFetchValue(processor, parameter, injectorArgs));
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
