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

import org.panda_lang.language.architecture.module.Module;
import org.panda_lang.language.architecture.module.TypeLoader;
import org.panda_lang.language.architecture.type.member.Member;
import org.panda_lang.language.architecture.type.member.Members;
import org.panda_lang.language.architecture.type.member.Metadata;
import org.panda_lang.language.architecture.type.member.constructor.Constructors;
import org.panda_lang.language.architecture.type.member.field.Fields;
import org.panda_lang.language.architecture.type.member.method.Methods;
import org.panda_lang.language.architecture.type.signature.Signed;
import org.panda_lang.language.architecture.type.signature.TypedSignature;
import org.panda_lang.utilities.commons.function.Completable;
import org.panda_lang.utilities.commons.function.Option;

import java.util.Collection;
import java.util.Map;

/**
 * Extensible owner of properties
 */
public interface Type extends Metadata, Typed, Signed {

    /**
     * Execute all registered initializers
     */
    void initialize(TypeLoader typeLoader);

    /**
     * Inherit the given type
     *
     * @param baseType the type to inherit from
     */
    void addBase(TypedSignature baseType);

    /**
     * Support automatic casting to other type
     *
     * @param to the type to cast to
     * @param autocast the autocast
     */
    void addAutocast(Reference to, Autocast<?, ?> autocast);

    /**
     * Add static initializer
     *
     * @param staticInitializer the static initializer to add
     */
    void addInitializer(Initializer<Type> staticInitializer);

    /**
     * Check if the type is the given type
     *
     * @param name the name to check
     * @return true if the given name is equals to the type name
     */
    default boolean is(String name) {
        return getName().equals(name);
    }

    /**
     * Check if current declaration is assignable from the given declaration
     *
     * @param type to compare with
     * @return true if this type is assignable from the given declaration, otherwise false
     */
    boolean isAssignableFrom(Type type);

    /**
     * Check if the type has been initialized
     *
     * @return true if initializers was called
     */
    boolean isInitialized();

    /**
     * Get methods that belongs to the type
     *
     * @return the methods
     */
    Methods getMethods();

    /**
     * Get fields that belongs to the type
     *
     * @return the fields
     */
    Fields getFields();

    /**
     * Get constructors that belongs to the type
     *
     * @return the constructors
     */
    Constructors getConstructors();

    /**
     * Get properties of the given type
     *
     * @param propertyType the property class
     * @param <T> generic type that represents the property type
     * @return the properties
     */
    <T extends Member> Option<? extends Members<T>> getProperties(Class<T> propertyType);

    /**
     * Get autocast for the given type
     *
     * @param to the type to search for
     * @return the autocast
     */
    Option<Autocast<?, ?>> getAutocast(Reference to);

    /**
     * Get all of the supported autocasts
     *
     * @return collection of autocasts
     */
    Map<? extends Reference, ? extends Autocast<?, ?>> getAutocasts();

    /**
     * Get supertypes of type
     *
     * @return collection of supertypes
     */
    Collection<? extends TypedSignature> getBases();

    /**
     * Get super class
     *
     * @return the superclass
     */
    Option<? extends TypedSignature> getSuperclass();

    /**
     * Get Java class associated with the type
     *
     * @return the associated class
     */
    Completable<? extends Class<?>> getAssociated();

    Option<TypeScope> getTypeScope();

    /**
     * Get state of type
     *
     * @return the state
     */
    State getState();

    /**
     * Get represented model of type
     *
     * @return the model that represents type
     */
    String getKind();

    @Override
    TypedSignature getSignature();

    Reference getReference();

    /**
     * Get loader that loaded this type
     *
     * @return a type loader
     */
    Option<? extends TypeLoader> getTypeLoader();

    /**
     * Get associated module
     *
     * @return the associated module
     */
    Module getModule();

    @Override
    default Type toType() {
        return this;
    }

}
