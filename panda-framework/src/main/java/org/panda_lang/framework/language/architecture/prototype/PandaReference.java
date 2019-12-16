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

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.expression.ExpressionUtils;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeField;
import org.panda_lang.framework.design.architecture.prototype.Reference;
import org.panda_lang.framework.design.architecture.prototype.ReferenceFetchException;
import org.panda_lang.utilities.commons.function.ThrowingFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class PandaReference implements Reference {

    private final String name;
    private final Class<?> associatedClass;
    private final ThrowingFunction<Reference, Prototype, ReferenceFetchException> prototypeSupplier;
    private final List<Consumer<Prototype>> initializers = new ArrayList<>(1);
    private Prototype prototype;

    public PandaReference(String name, Class<?> associatedClass, ThrowingFunction<Reference, Prototype, ReferenceFetchException> prototypeSupplier) {
        this.name = name;
        this.associatedClass = associatedClass;
        this.prototypeSupplier = prototypeSupplier;
    }

    public PandaReference(Prototype prototype) {
        this(prototype.getSimpleName(), prototype.getAssociatedClass(), reference -> prototype);
    }

    @Override
    public Reference addInitializer(Consumer<Prototype> initializer) {
        initializers.add(initializer);
        return this;
    }

    @Override
    public synchronized Prototype fetch() {
        if (isInitialized()) {
            return prototype;
        }

        this.prototype = prototypeSupplier.apply(this);

        for (Consumer<Prototype> initializer : initializers) {
            initializer.accept(prototype);
        }

        // System.out.println("Generate " + prototype);

        for (PrototypeField field : prototype.getFields().getDeclaredProperties()) {
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

        return prototype;
    }

    @Override
    public boolean isInitialized() {
        return prototype != null;
    }

    @Override
    public int getAmountOfInitializers() {
        return initializers.size();
    }

    @Override
    public Class<?> getAssociatedClass() {
        return associatedClass;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ref " + (isInitialized() ? prototype : " not-initialized " + name);
    }

}
