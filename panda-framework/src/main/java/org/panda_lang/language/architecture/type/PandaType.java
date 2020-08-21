/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.language.architecture.type;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.expression.ExpressionUtils;
import org.panda_lang.language.architecture.module.Module;
import org.panda_lang.language.architecture.module.TypeLoader;
import org.panda_lang.language.architecture.type.array.ArrayClassTypeFetcher;
import org.panda_lang.language.architecture.type.generator.dynamic.PandaDynamicClass;
import org.panda_lang.language.architecture.type.member.AbstractProperty;
import org.panda_lang.language.architecture.type.member.constructor.Constructors;
import org.panda_lang.language.architecture.type.member.Member;
import org.panda_lang.language.architecture.type.member.field.Fields;
import org.panda_lang.language.architecture.type.member.method.Methods;
import org.panda_lang.language.architecture.type.member.constructor.PandaConstructors;
import org.panda_lang.language.architecture.type.member.field.PandaFields;
import org.panda_lang.language.architecture.type.member.method.PandaMethods;
import org.panda_lang.language.architecture.type.member.Properties;
import org.panda_lang.language.architecture.type.member.constructor.TypeConstructor;
import org.panda_lang.language.architecture.type.member.field.TypeField;
import org.panda_lang.language.architecture.type.member.method.TypeMethod;
import org.panda_lang.utilities.commons.ValidationUtils;
import org.panda_lang.utilities.commons.function.Option;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class PandaType extends AbstractProperty implements Type {

    protected final Module module;
    protected final String model;
    protected final State state;
    protected final DynamicClass associated;
    protected final Collection<Type> bases = new ArrayList<>(1);
    protected final Map<Type, Autocast<?, ?>> autocasts = new HashMap<>();
    protected final List<Initializer<Type>> initializers = new ArrayList<>(2);
    protected final Fields fields = new PandaFields(this);
    protected final Constructors constructors = new PandaConstructors(this);
    protected final Methods methods = new PandaMethods(this);
    protected TypeLoader typeLoader;

    public PandaType(PandaTypeMetadata<?, ?> metadata) {
        super(metadata.name, metadata.location, metadata.visibility, metadata.isNative);

        this.module = ValidationUtils.notNull(metadata.module, "Type needs module");
        this.model = ValidationUtils.notNull(metadata.model, "Type requires defined model");
        this.state = ValidationUtils.notNull(metadata.state, "State of type is missing");

        if (metadata.javaType == null) {
            this.associated = new PandaDynamicClass(this, getName(), getModule().getName(), getModel());
        }
        else {
            this.associated = new PandaDynamicClass(this, metadata.javaType);
        }
    }

    @Override
    public void initialize(TypeLoader typeLoader) {
        if (isInitialized()) {
            return;
        }

        this.typeLoader = typeLoader;

        for (Initializer<Type> initializer : initializers) {
            initializer.accept(typeLoader, this);
        }

        initializers.clear();

        for (TypeField field : getFields().getDeclaredProperties()) {
            if (!field.hasDefaultValue() || !field.isStatic()) {
                continue;
            }

            Expression expression = field.getDefaultValue();

            try {
                Object value = ExpressionUtils.evaluateConstExpression(expression);
                field.setStaticValue(() -> value);
            } catch (Exception e) {
                throw new ReferenceFetchException("Cannot evaluate static value of field " + field, e);
            }
        }
    }

    @Override
    public void addBase(Type baseType) {
        bases.add(baseType);
        associated.append(baseType);
    }

    @Override
    public void addAutocast(Type to, Autocast<?, ?> autocast) {
        autocasts.put(to, autocast);
    }

    @Override
    public void addInitializer(Initializer<Type> staticInitializer) {
        if (isInitialized()) {
            staticInitializer.accept(typeLoader, this);
        }
        else {
            initializers.add(staticInitializer);
        }
    }

    @Override
    public Type toArray(TypeLoader loader) {
        return ArrayClassTypeFetcher.getArrayOf(typeLoader, this, 1);
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
    public boolean isAssignableFrom(@Nullable Type to) {
        if (to == null) {
            return true;
        }

        return to.equals(this)
                || getAssociatedClass().isAssignableFrom(to.getAssociatedClass())
                || hasCommonTypes(bases, to)
                || to.getAutocast(this).isPresent();
    }

    private boolean hasCommonTypes(Collection<? extends Type> fromTypes, Type to) {
        for (Type from : fromTypes) {
            if (to.equals(from) || to.getAssociatedClass().isAssignableFrom(from.getAssociatedClass())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isInitialized() {
        return typeLoader != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Member> Option<Properties<T>> getProperties(Class<T> propertyType) {
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

        return Option.of(properties);
    }

    @Override
    public Option<Autocast<?, ?>> getAutocast(Type to) {
        for (Entry<Type, Autocast<?, ?>> autocastEntry : autocasts.entrySet()) {
            if (to.isAssignableFrom(autocastEntry.getKey())) {
                return Option.of(autocastEntry.getValue());
            }
        }

        return Option.none();
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
    public Option<Type> getSuperclass() {
        for (Type base : getBases()) {
            if (TypeModels.isClass(base)) {
                return Option.of(base);
            }
        }

        return Option.none();
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
    public Option<TypeLoader> getTypeLoader() {
        return Option.of(typeLoader);
    }

    @Override
    public Module getModule() {
        return module;
    }

    @Override
    public String toString() {
        return getName();
    }

    public static <T> PandaTypeMetadata<?, ?> builder() {
        return new PandaTypeMetadata<>();
    }

}
