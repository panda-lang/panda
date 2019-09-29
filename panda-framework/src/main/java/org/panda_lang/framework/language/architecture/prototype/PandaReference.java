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

package org.panda_lang.framework.language.architecture.prototype;

import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.Reference;

import java.util.ArrayList;
import java.util.List;

public class PandaReference extends AbstractPrototypeMetadata implements Reference {

    private final Prototype prototype;
    private final List<Runnable> initializers = new ArrayList<>(1);
    private boolean initialized;

    protected PandaReference(Prototype prototype) {
        super(prototype.getName(), prototype.getModule(), prototype.getAssociatedClass(), prototype.getVisibility());
        this.prototype = prototype;
    }

    @Override
    public Reference addInitializer(Runnable runnable) {
        initializers.add(runnable);
        return this;
    }

    @Override
    public synchronized Prototype fetch() {
        if (!initialized) {
            initialized = true;

            for (Runnable initializer : initializers) {
                initializer.run();
            }
        }

        return prototype;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public Prototype getPrototype() {
        return prototype;
    }

    @Override
    public Class<?> getAssociatedClass() {
        return prototype.getAssociatedClass();
    }

    @Override
    public String getName() {
        return prototype.getName();
    }

    @Override
    public String toString() {
        return prototype.toString();
    }

}
