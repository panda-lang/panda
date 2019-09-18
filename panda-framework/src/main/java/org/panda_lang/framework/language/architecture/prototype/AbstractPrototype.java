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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeReference;
import org.panda_lang.framework.design.architecture.prototype.PrototypeConstructors;
import org.panda_lang.framework.design.architecture.prototype.PrototypeFields;
import org.panda_lang.framework.design.architecture.prototype.PrototypeMethods;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

abstract class AbstractPrototype extends AbstractPrototypeMetadata implements Prototype {

    protected final PrototypeReference reference = new PandaPrototypeReference(this);
    protected final Collection<PrototypeReference> extended = new ArrayList<>(1);
    protected final PrototypeConstructors constructors = new PandaConstructors();
    protected final PrototypeFields fields = new PandaFields();
    protected final PrototypeMethods methods = new PandaMethods();

    public AbstractPrototype(Module module, String name, Class<?> associated) {
        super(name, module, associated);
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public PrototypeMethods getMethods() {
        return methods;
    }

    @Override
    public PrototypeFields getFields() {
        return fields;
    }

    @Override
    public PrototypeConstructors getConstructors() {
        return constructors;
    }

    @Override
    public PrototypeReference getReference() {
        return reference;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        return this == o;
    }

    @Override
    public String toString() {
        return "ClassPrototype::" + name;
    }

}
