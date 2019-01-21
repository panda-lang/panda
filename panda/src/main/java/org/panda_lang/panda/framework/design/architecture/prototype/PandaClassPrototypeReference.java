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

package org.panda_lang.panda.framework.design.architecture.prototype;

import org.jetbrains.annotations.Nullable;

public class PandaClassPrototypeReference extends AbstractClassPrototypeMetadata implements ClassPrototypeReference  {

    private final ClassPrototype prototype;
    private final Runnable initializer;
    private boolean initialized;

    public PandaClassPrototypeReference(ClassPrototype prototype, @Nullable Runnable initializer) {
        super(prototype.getClassName(), prototype.getModule(), prototype.getAssociatedClass());

        this.prototype = prototype;
        this.initializer = initializer;
    }

    public PandaClassPrototypeReference(ClassPrototype prototype) {
        this(prototype, null);
    }

    @Override
    public synchronized ClassPrototype get() {
        if (!initialized && initializer != null) {
            initialized = true;
            initializer.run();
        }

        return prototype;
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
