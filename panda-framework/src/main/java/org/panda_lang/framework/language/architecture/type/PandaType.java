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

package org.panda_lang.framework.language.architecture.type;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.architecture.type.Autocast;
import org.panda_lang.framework.design.architecture.type.Constructors;
import org.panda_lang.framework.design.architecture.type.DynamicClass;
import org.panda_lang.framework.design.architecture.type.ExecutableProperty;
import org.panda_lang.framework.design.architecture.type.Fields;
import org.panda_lang.framework.design.architecture.type.Methods;
import org.panda_lang.framework.design.architecture.type.Properties;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.architecture.type.TypeConstructor;
import org.panda_lang.framework.design.architecture.type.TypeField;
import org.panda_lang.framework.design.architecture.type.TypeMethod;
import org.panda_lang.framework.design.architecture.type.Reference;
import org.panda_lang.framework.design.architecture.type.State;
import org.panda_lang.framework.language.architecture.type.array.ArrayClassTypeFetcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

public class PandaType extends AbstractProperty implements Type {

    protected final Reference reference;
    protected final Module module;
    protected final String model;
    protected final State state;
    protected final DynamicClass associated;
    protected final Collection<Type> bases = new ArrayList<>(1);
    protected final Map<Type, Autocast<?, ?>> autocasts = new HashMap<>();
    protected final Fields fields = new PandaFields(this);
    protected final Constructors constructors = new PandaConstructors(this);
    protected final Methods methods = new PandaMethods(this);

    public PandaType(PandaTypeBuilder<?, ?> builder) {
        super(builder.name, builder.location, builder.visibility, builder.isNative);

        if (builder.reference == null) {
            throw new IllegalArgumentException("Type requires reference");
        }

        if (builder.module == null) {
            throw new IllegalArgumentException("Type needs module");
        }

        if (builder.model == null) {
            throw new IllegalArgumentException("Type requires defined model");
        }

        if (builder.state == null) {
            throw new IllegalArgumentException("State of type is missing");
        }

        this.reference = builder.reference;
        this.module = builder.module;
        this.model = builder.model;
        this.state = builder.state;
        this.associated = builder.associated;
    }

    @Override
    public void addBase(Type baseType) {
        bases.add(baseType);
    }

    @Override
    public void addAutocast(Type to, Autocast<?, ?> autocast) {
        autocasts.put(to, autocast);
    }

    @Override
    public Type toArray(ModuleLoader loader) {
        return ArrayClassTypeFetcher.getArrayOf(getModule(), this, 1);
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
    public boolean isAssignableFrom(@Nullable Type type) {
        if (type == null) {
            return true;
        }

        return type.equals(this)
                || getAssociatedClass().isAssignableFrom(type.getAssociatedClass())
                || hasCommonTypes(bases, type.getBases())
                || type.getAutocast(this).isPresent();
    }

    private boolean hasCommonTypes(Collection<? extends Type> fromTypes, Collection<? extends Type> toTypes) {
        for (Type from : fromTypes) {
            for (Type to : toTypes) {
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

        if (TypeMethod.class.isAssignableFrom(propertyType)) {
            properties = (Properties<T>) methods;
        }
        else if (TypeConstructor.class.isAssignableFrom(propertyType)) {
            properties = (Properties<T>) constructors;
        }
        else if (TypeField.class.isAssignableFrom(propertyType)) {
            properties = (Properties<T>) fields;
        }

        return Optional.ofNullable(properties);
    }

    @Override
    public Optional<Autocast<?, ?>> getAutocast(Type to) {
        for (Entry<Type, Autocast<?, ?>> autocastEntry : autocasts.entrySet()) {
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
    public Collection<Type> getBases() {
        return bases;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public Type getType() {
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
    public String toString() {
        return "type " + getName();
    }

    public static <T> PandaTypeBuilder<?, ?> builder() {
        return new PandaTypeBuilder<>();
    }

}
