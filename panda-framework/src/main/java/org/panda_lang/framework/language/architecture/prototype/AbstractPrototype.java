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
import org.panda_lang.framework.design.architecture.prototype.State;
import org.panda_lang.framework.design.architecture.prototype.Reference;
import org.panda_lang.framework.design.architecture.prototype.Constructors;
import org.panda_lang.framework.design.architecture.prototype.Fields;
import org.panda_lang.framework.design.architecture.prototype.Methods;
import org.panda_lang.framework.design.architecture.prototype.Visibility;
import org.panda_lang.framework.design.interpreter.source.Source;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

abstract class AbstractPrototype extends AbstractPrototypeDeclaration implements Prototype {

    protected final Reference reference = new PandaReference(this);
    protected final Collection<Reference> extended = new ArrayList<>(1);
    protected final Constructors constructors = new PandaConstructors();
    protected final Fields fields = new PandaFields();
    protected final Methods methods = new PandaMethods();

    public AbstractPrototype(Module module, String name, Source source, Class<?> associated, State state, Visibility visibility) {
        super(name, module, source, associated, state, visibility);
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public Methods getMethods() {
        return methods;
    }

    @Override
    public Fields getFields() {
        return fields;
    }

    @Override
    public Constructors getConstructors() {
        return constructors;
    }

    @Override
    public Reference getReference() {
        return reference;
    }

    @Override
    public Prototype getPrototype() {
        return this;
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
        return "prototype " + name;
    }

}
