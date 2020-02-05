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

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.expression.ExpressionUtils;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.type.DynamicClass;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.architecture.type.TypeField;
import org.panda_lang.framework.design.architecture.type.Reference;
import org.panda_lang.framework.design.architecture.type.ReferenceFetchException;
import org.panda_lang.framework.language.architecture.type.dynamic.PandaDynamicClass;
import org.panda_lang.utilities.commons.function.ThrowingFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class PandaReference implements Reference {

    private final String name;
    private final Module module;
    private final DynamicClass associatedClass;
    private final ThrowingFunction<Reference, Type, ReferenceFetchException> typeSupplier;
    private final List<Consumer<Type>> initializers = new ArrayList<>(1);
    private Type type;

    public PandaReference(DynamicClass associatedClass, Module module, ThrowingFunction<Reference, Type, ReferenceFetchException> typeSupplier) {
        this.name = associatedClass.getSimpleName();
        this.module = module;
        this.associatedClass = associatedClass;
        this.typeSupplier = typeSupplier;
    }

    public PandaReference(String name, Module module, String model, ThrowingFunction<Reference, Type, ReferenceFetchException> typeSupplier) {
        this(new PandaDynamicClass(name, module.getName(), model), module, typeSupplier);
    }

    @Override
    public Reference addInitializer(Consumer<Type> initializer) {
        initializers.add(initializer);
        return this;
    }

    @Override
    public synchronized Type fetch() {
        if (isInitialized()) {
            return type;
        }

        this.type = typeSupplier.apply(this);

        for (Consumer<Type> initializer : initializers) {
            initializer.accept(type);
        }

        for (TypeField field : type.getFields().getDeclaredProperties()) {
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

        return type;
    }

    @Override
    public boolean isInitialized() {
        return type != null;
    }

    @Override
    public int getAmountOfInitializers() {
        return initializers.size();
    }

    @Override
    public DynamicClass getAssociatedClass() {
        return associatedClass;
    }

    @Override
    public Module getModule() {
        return module;
    }

    @Override
    public String getSimpleName() {
        return name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ref " + (isInitialized() ? type : " not-initialized " + name);
    }

}
