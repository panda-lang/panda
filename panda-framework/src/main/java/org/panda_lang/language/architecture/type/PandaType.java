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
import org.panda_lang.language.architecture.type.member.AbstractMetadata;
import org.panda_lang.language.architecture.type.member.Member;
import org.panda_lang.language.architecture.type.member.Members;
import org.panda_lang.language.architecture.type.member.constructor.Constructors;
import org.panda_lang.language.architecture.type.member.constructor.PandaConstructors;
import org.panda_lang.language.architecture.type.member.constructor.TypeConstructor;
import org.panda_lang.language.architecture.type.member.field.Fields;
import org.panda_lang.language.architecture.type.member.field.PandaFields;
import org.panda_lang.language.architecture.type.member.field.TypeField;
import org.panda_lang.language.architecture.type.member.method.Methods;
import org.panda_lang.language.architecture.type.member.method.PandaMethods;
import org.panda_lang.language.architecture.type.member.method.TypeMethod;
import org.panda_lang.language.architecture.type.signature.TypedSignature;
import org.panda_lang.utilities.commons.ValidationUtils;
import org.panda_lang.utilities.commons.function.Completable;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.utilities.commons.function.PandaStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PandaType extends AbstractMetadata implements Type {

    protected final Reference reference;
    protected final TypedSignature signature;
    protected final Module module;
    protected final String kind;
    protected final State state;
    protected final Option<TypeScope> typeScope;
    protected final Completable<? extends Class<?>> associated;
    protected final List<TypedSignature> bases = new ArrayList<>();
    protected final Map<Reference, Autocast<?, ?>> autocasts = new HashMap<>();
    protected final Fields fields = new PandaFields(this);
    protected final Constructors constructors = new PandaConstructors(this);
    protected final Methods methods = new PandaMethods(this);
    protected final List<Initializer<Type>> initializers = new ArrayList<>(2);
    protected TypeLoader typeLoader;

    public PandaType(PandaTypeMetadata<?, ?> metadata) {
        super(metadata.name, metadata.location, metadata.visibility, metadata.isNative);

        this.signature = ValidationUtils.notNull(metadata.signature.toTyped(), "Signature cannot be null");
        this.module = ValidationUtils.notNull(metadata.module, "Type needs module");
        this.kind = ValidationUtils.notNull(metadata.kind, "The kind of type is not defined");
        this.state = ValidationUtils.notNull(metadata.state, "State of type is missing");
        this.associated = ValidationUtils.notNull(metadata.associatedType, "Associated type is missing");
        this.typeScope = Option.of(metadata.typeScope);
        ValidationUtils.notNull(metadata.bases, "Bases are not defined").forEach(this::addBase);

        this.reference = new Reference(this);
    }

    @Override
    public void initialize(TypeLoader typeLoader) {
        if (isInitialized()) {
            return;
        }

        for (Initializer<Type> initializer : initializers) {
            initializer.accept(typeLoader, this);
        }

        initializers.clear();
        this.typeLoader = typeLoader;

        for (TypeField field : getFields().getDeclaredProperties()) {
            if (!field.hasDefaultValue() || !field.isStatic()) {
                continue;
            }

            Expression expression = field.getDefaultValue();

            try {
                Object value = ExpressionUtils.evaluateConstExpression(expression);
                field.setStaticValue(() -> value);
            } catch (Exception exception) {
                throw new ReferenceFetchException("Cannot evaluate static value of field " + field, exception);
            }
        }
    }

    @Override
    public void addAutocast(Reference to, Autocast<?, ?> autocast) {
        if (isInitialized()) {
            throw new IllegalStateException("Cannot add autocast to initialized type");
        }

        autocasts.put(to, autocast);
    }

    @Override
    public void addBase(TypedSignature baseSignature) {
        if (isInitialized()) {
            throw new IllegalStateException("Cannot add base type to initialized type");
        }

        bases.add(baseSignature);
        autocasts.put(baseSignature.getReference(), (originalType, object, resultType) -> object);
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
    public int hashCode() {
        return Objects.hash(getModule(), getSimpleName());
    }

    @Override
    public boolean equals(@Nullable Object to) {
        return this == to;
    }

    @Override
    public boolean isAssignableFrom(Type from) {
        return this.equals(from) || from.getAutocast(this.getReference()).isDefined();
    }

    @Override
    public boolean isInitialized() {
        return typeLoader != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Member> Option<Members<T>> getProperties(Class<T> propertyType) {
        Members<T> members = null;

        if (TypeMethod.class.isAssignableFrom(propertyType)) {
            members = (Members<T>) methods;
        }
        else if (TypeConstructor.class.isAssignableFrom(propertyType)) {
            members = (Members<T>) constructors;
        }
        else if (TypeField.class.isAssignableFrom(propertyType)) {
            members = (Members<T>) fields;
        }

        return Option.of(members);
    }

    @Override
    public Option<Autocast<?, ?>> getAutocast(Reference to) {
        Autocast<?, ?> autocast = autocasts.get(to);

        if (autocast != null) {
            return Option.of(autocast);
        }

        for (TypedSignature base : bases) {
            Option<Autocast<?, ?>> baseAutocast = base.fetchType().getAutocast(to);

            if (baseAutocast != null) {
                return baseAutocast;
            }
        }

        return Option.none();
    }

    @Override
    public Map<? extends Reference, ? extends Autocast<?, ?>> getAutocasts() {
        return autocasts;
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
    public Collection<? extends TypedSignature> getBases() {
        return bases;
    }

    @Override
    public Option<? extends TypedSignature> getSuperclass() {
        return PandaStream.of(getBases()).find(base -> Kind.TYPE.equals(base.getReference().getKind()));
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public String getKind() {
        return kind;
    }

    @Override
    public TypedSignature getSignature() {
        return signature;
    }

    @Override
    public Type getType() {
        return this;
    }

    @Override
    public Completable<? extends Class<?>> getAssociated() {
        return associated;
    }

    @Override
    public Option<TypeScope> getTypeScope() {
        return typeScope;
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
    public Reference getReference() {
        return reference;
    }

    @Override
    public String getName() {
        return getModule().getName() + "::" + getSimpleName();
    }

    @Override
    public String toString() {
        return getName();
    }

    public static <T> PandaTypeMetadata<?, ?> builder() {
        return new PandaTypeMetadata<>();
    }

}
