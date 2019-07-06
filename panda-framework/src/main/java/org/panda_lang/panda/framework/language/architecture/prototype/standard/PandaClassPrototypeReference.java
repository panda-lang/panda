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

package org.panda_lang.panda.framework.language.architecture.prototype.standard;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.language.architecture.prototype.array.ArrayClassPrototypeUtils;

import java.util.ArrayList;
import java.util.List;

public class PandaClassPrototypeReference extends AbstractClassPrototypeMetadata implements ClassPrototypeReference {

    private final ClassPrototype prototype;
    private final List<Runnable> initializers = new ArrayList<>(0);
    private boolean initialized;

    protected PandaClassPrototypeReference(ClassPrototype prototype) {
        super(prototype.getClassName(), prototype.getModule(), prototype.getAssociatedClass());
        this.prototype = prototype;
    }

    @Override
    public ClassPrototypeReference addInitializer(Runnable runnable) {
        initializers.add(runnable);
        return this;
    }

    @Override
    public synchronized ClassPrototype fetch() {
        if (!initialized) {
            initialized = true;
            initializers.forEach(Runnable::run);
        }

        return prototype;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public Class<?> getAssociatedClass() {
        return prototype.getAssociatedClass();
    }

    @Override
    public String getClassName() {
        return prototype.getClassName();
    }

}
