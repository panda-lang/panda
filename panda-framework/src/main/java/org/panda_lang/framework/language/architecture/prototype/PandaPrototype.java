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
import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.architecture.prototype.Constructors;
import org.panda_lang.framework.design.architecture.prototype.ExecutableProperty;
import org.panda_lang.framework.design.architecture.prototype.Fields;
import org.panda_lang.framework.design.architecture.prototype.Methods;
import org.panda_lang.framework.design.architecture.prototype.Properties;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeConstructor;
import org.panda_lang.framework.design.architecture.prototype.PrototypeField;
import org.panda_lang.framework.design.architecture.prototype.PrototypeMethod;
import org.panda_lang.framework.design.architecture.prototype.Reference;
import org.panda_lang.framework.design.architecture.prototype.State;
import org.panda_lang.framework.design.architecture.prototype.Visibility;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.language.architecture.prototype.array.ArrayClassPrototypeFetcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class PandaPrototype extends AbstractProperty implements Prototype {

    protected final Module module;
    protected final Class<?> associated;
    protected final String type;
    protected final State state;
    protected final Collection<Prototype> bases = new ArrayList<>(1);
    protected final Fields fields = new PandaFields(this);
    protected final Constructors constructors = new PandaConstructors(this);
    protected final Methods methods = new PandaMethods(this);

    public PandaPrototype(Module module, String name, SourceLocation location, Class<?> associated, String type, State state, Visibility visibility) {
        super(name, location, visibility);

        if (module == null) {
            throw new IllegalArgumentException("Prototype needs module");
        }

        if (associated == null) {
            throw new IllegalArgumentException("Prototype has to be associated with a java class");
        }

        if (type == null) {
            throw new IllegalArgumentException("Prototype requires defined type");
        }

        if (state == null) {
            throw new IllegalArgumentException("State of prototype is missing");
        }

        this.module = module;
        this.associated = associated;
        this.type = type;
        this.state = state;
    }

    protected PandaPrototype(PandaPrototypeBuilder<?, ?> builder) {
        this(builder.module, builder.name, builder.location, builder.associated, builder.type, builder.state, builder.visibility);
    }

    @Override
    public void addBase(Prototype basePrototype) {
        bases.add(basePrototype);
    }

    @Override
    public Prototype toArray(ModuleLoader loader) {
        return ArrayClassPrototypeFetcher.getArrayOf(getModule(), this, 1);
    }

    @Override
    public Reference toReference() {
        return new PandaStubReference(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getModule(), getSimpleName());
    }

    @Override
    public boolean equals(@Nullable Object o) {
        return this == o;
    }

    @Override
    public boolean isAssignableFrom(@Nullable Prototype prototype) { // this (Panda Class | Java Class) isAssociatedWith
        if (prototype == null) {
            return true;
        }

        return prototype.equals(this)
                || PandaPrototypeUtils.isAssignableFrom(associated, prototype.getAssociatedClass())
                || PandaPrototypeUtils.hasCommonPrototypes(bases, prototype.getBases());
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
    @SuppressWarnings("unchecked")
    public <T extends ExecutableProperty> Optional<Properties<T>> getProperties(Class<T> propertyType) {
        Properties<T> properties = null;

        if (PrototypeMethod.class.isAssignableFrom(propertyType)) {
            properties = (Properties<T>) methods;
        }
        else if (PrototypeConstructor.class.isAssignableFrom(propertyType)) {
            properties = (Properties<T>) constructors;
        }
        else if (PrototypeField.class.isAssignableFrom(propertyType)) {
            properties = (Properties<T>) fields;
        }

        return Optional.ofNullable(properties);
    }

    @Override
    public Collection<Prototype> getBases() {
        return bases;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Class<?> getAssociatedClass() {
        return associated;
    }

    @Override
    public Module getModule() {
        return module;
    }

    @Override
    public Prototype getPrototype() {
        return this;
    }

    @Override
    public String getPropertyName() {
        return module.getName() + "::" + getSimpleName();
    }

    @Override
    public String toString() {
        return "prototype " + getPropertyName();
    }

    public static <T> PandaPrototypeBuilder<?, ?> builder() {
        return new PandaPrototypeBuilder<>();
    }

    public static final class PandaPrototypeBuilder<BUILDER extends PandaPrototypeBuilder<BUILDER, ?>, TYPE extends PandaPrototype> {

        protected String name;
        protected Module module;
        protected SourceLocation location;
        protected Class<?> associated;
        protected String type;
        protected State state;
        protected Visibility visibility;

        protected PandaPrototypeBuilder() {
            this.associated = Object.class;
        }

        public BUILDER name(String name) {
            this.name = name;
            return getThis();
        }

        public BUILDER module(Module module) {
            this.module = module;
            return getThis();
        }

        public BUILDER location(SourceLocation location) {
            this.location = location;
            return getThis();
        }

        public BUILDER associated(Class associated) {
            this.associated = associated;

            if (name == null) {
                this.name = associated.getCanonicalName();
            }

            return getThis();
        }

        public BUILDER type(String type) {
            this.type = type;
            return getThis();
        }

        public BUILDER state(State state) {
            this.state = state;
            return getThis();
        }

        public BUILDER visibility(Visibility visibility) {
            this.visibility = visibility;
            return getThis();
        }

        @SuppressWarnings("unchecked")
        public TYPE build() {
            return (TYPE) new PandaPrototype(this);
        }

        @SuppressWarnings("unchecked")
        protected BUILDER getThis() {
            return (BUILDER) this;
        }

    }

}
