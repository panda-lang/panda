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
import org.panda_lang.framework.design.architecture.prototype.Autocast;
import org.panda_lang.framework.design.architecture.prototype.Constructors;
import org.panda_lang.framework.design.architecture.prototype.DynamicClass;
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
import org.panda_lang.framework.language.architecture.prototype.array.ArrayClassPrototypeFetcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

public class PandaPrototype extends AbstractProperty implements Prototype {

    protected final Reference reference;
    protected final Module module;
    protected final DynamicClass associated;
    protected final String type;
    protected final State state;
    protected final Collection<Prototype> bases = new ArrayList<>(1);
    protected final Map<Prototype, Autocast<?, ?>> autocasts = new HashMap<>();
    protected final Fields fields = new PandaFields(this);
    protected final Constructors constructors = new PandaConstructors(this);
    protected final Methods methods = new PandaMethods(this);

    public PandaPrototype(PandaPrototypeBuilder<?, ?> builder) {
        super(builder.name, builder.location, builder.visibility, builder.isNative);

        if (builder.reference == null) {
            throw new IllegalArgumentException("Prototype has to be referenced");
        }

        if (builder.module == null) {
            throw new IllegalArgumentException("Prototype needs module");
        }

        if (builder.type == null) {
            throw new IllegalArgumentException("Prototype requires defined type");
        }

        if (builder.state == null) {
            throw new IllegalArgumentException("State of prototype is missing");
        }

        this.reference = builder.reference;
        this.module = builder.module;
        this.type = builder.type;
        this.state = builder.state;
        this.associated = builder.associated;
    }

    @Override
    public void addBase(Prototype basePrototype) {
        bases.add(basePrototype);
    }

    @Override
    public void addAutocast(Prototype to, Autocast<?, ?> autocast) {
        autocasts.put(to, autocast);
    }

    @Override
    public Prototype toArray(ModuleLoader loader) {
        return ArrayClassPrototypeFetcher.getArrayOf(getModule(), this, 1);
    }

    @Override
    public Reference toReference() {
        return reference;
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
    public boolean isAssignableFrom(@Nullable Prototype prototype) {
        if (prototype == null) {
            return true;
        }

        return prototype.equals(this)
                || getAssociatedClass().isAssignableFrom(prototype.getAssociatedClass())
                || hasCommonPrototypes(bases, prototype.getBases())
                || prototype.getAutocast(this).isPresent();
    }

    private boolean hasCommonPrototypes(Collection<? extends Prototype> fromPrototypes, Collection<? extends Prototype> toPrototypes) {
        for (Prototype from : fromPrototypes) {
            for (Prototype to : toPrototypes) {
                if (from.equals(to)) {
                    return true;
                }

                if (from.getAssociatedClass().isAssignableFrom(to.getAssociatedClass())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean isArray() {
        return false;
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
    public Optional<Autocast<?, ?>> getAutocast(Prototype to) {
        for (Entry<Prototype, Autocast<?, ?>> autocastEntry : autocasts.entrySet()) {
            if (to.isAssignableFrom(autocastEntry.getKey())) {
                return Optional.of(autocastEntry.getValue());
            }
        }

        return Optional.empty();
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
    public Collection<Prototype> getBases() {
        return bases;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public String getModel() {
        return type;
    }

    @Override
    public Prototype getType() {
        return this;
    }

    @Override
    public DynamicClass getAssociatedClass() {
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

}
