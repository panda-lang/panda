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

package org.panda_lang.framework.architecture.module;

import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.architecture.type.Type;
import org.panda_lang.utilities.commons.function.Completable;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.utilities.commons.function.PandaStream;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

abstract class PandaModuleContainer implements ModuleContainer {

    protected final Map<String, Module> modules = new HashMap<>();

    @Override
    public Module include(Module module) {
        modules.put(module.getSimpleName(), module);
        return module;
    }

    @Override
    public Completable<Option<Module>> forModule(String moduleQualifier) {
        if (moduleQualifier.contains("::")) {
            throw new PandaFrameworkException("Cannot get module using qualifier with type operator");
        }

        if (!moduleQualifier.contains(":")) {
            return Completable.completed(Option.of(modules.get(moduleQualifier)));
        }

        return fetch(moduleQualifier, false);
    }

    @Override
    public Completable<Module> acquire(String moduleQualifier) {
        return fetch(moduleQualifier, true).thenApply(module -> module.orThrow(() -> {
            throw new PandaFrameworkException("Cannot create module " + moduleQualifier);
        }));
    }

    protected Completable<Option<Module>> fetch(String moduleQualifier, boolean compute) {
        String[] names = moduleQualifier.split(":");

        // Kinda cancerous mutable based logic to mix recursive async computation of non-existing submodules
        AtomicInteger index = new AtomicInteger();
        AtomicReference<ModuleContainer> currentContainer = new AtomicReference<>(this);
        AtomicReference<Module> currentModule = new AtomicReference<>(null);

        return forModule(names[index.get()]).thenCompose(resultModule -> {
            Completable<Option<Module>> result = null;

            if (resultModule.isDefined()) {
                result = Completable.completed(resultModule);
            }

            if (resultModule.isEmpty() && compute) {
                // TODO: index is not updated, possible bug
                result = computeModule(currentContainer.get(), currentModule.get(), names[index.get()]);
            }

            if (result == null) {
                return Completable.completed(Option.none());
            }

            resultModule.peek(currentContainer::set);
            resultModule.peek(currentModule::set);
            return result;
        });
    }

    private static Completable<Option<Module>> computeModule(ModuleContainer source, Module parent, String name) {
        return source.forModule(name).thenApply(result -> result.orElse(() -> {
            Module computedModule = new PandaModule(parent, name);
            source.include(computedModule);
            return Option.of(computedModule);
        }));
    }

    public static Option<Type> forName(Collection<? extends TypeLoader> resources, String name) {
        return PandaStream.of(resources)
                .flatMap(parent -> parent.forType(name))
                .head();
    }

    @Override
    public Collection<? extends String> getNames() {
        return modules.keySet();
    }

    @Override
    public Collection<? extends Module> getModules() {
        return modules.values();
    }

}
