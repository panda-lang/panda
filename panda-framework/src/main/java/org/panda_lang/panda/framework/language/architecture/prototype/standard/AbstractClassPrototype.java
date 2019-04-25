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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructors;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeFields;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethods;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.constructor.PandaConstructors;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.field.PandaFields;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.method.PandaMethods;
import org.panda_lang.panda.framework.language.architecture.prototype.array.ArrayClassPrototypeUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public abstract class AbstractClassPrototype extends AbstractClassPrototypeMetadata implements ClassPrototype {

    protected final ClassPrototypeReference reference = new PandaClassPrototypeReference(this);
    protected final Collection<ClassPrototypeReference> extended = new ArrayList<>(1);
    protected final PrototypeConstructors constructors = new PandaConstructors();
    protected final PrototypeFields fields = new PandaFields();
    protected final PrototypeMethods methods = new PandaMethods();

    public AbstractClassPrototype(Module module, String name, Class<?> associated) {
        super(name, module, associated);
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public ClassPrototypeReference toArray() {
        return ArrayClassPrototypeUtils.getArrayOf(getReference(), 1);
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
    public ClassPrototypeReference getReference() {
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
