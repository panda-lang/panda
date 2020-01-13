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

package org.panda_lang.framework.language.architecture.prototype;

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.prototype.DynamicClass;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.Reference;
import org.panda_lang.framework.design.architecture.prototype.ReferenceFetchException;
import org.panda_lang.framework.language.runtime.PandaRuntimeException;

import java.util.function.Consumer;

public final class PandaStubReference implements Reference {

    private final Prototype prototype;

    public PandaStubReference(Prototype prototype) {
        this.prototype = prototype;
    }

    @Override
    public Prototype fetch() throws ReferenceFetchException {
        return prototype;
    }

    @Override
    public Reference addInitializer(Consumer<Prototype> initializer) {
        throw new PandaRuntimeException("Not supported");
    }

    @Override
    public boolean isInitialized() {
        return true;
    }

    @Override
    public int getAmountOfInitializers() {
        return 1;
    }

    @Override
    public DynamicClass getAssociatedClass() {
        return prototype.getAssociatedClass();
    }

    @Override
    public Module getModule() {
        return prototype.getModule();
    }

    @Override
    public String getSimpleName() {
        return prototype.getSimpleName();
    }

    @Override
    public String getName() {
        return prototype.getSimpleName();
    }

    @Override
    public String toString() {
        return "stub ref " + prototype;
    }

}
