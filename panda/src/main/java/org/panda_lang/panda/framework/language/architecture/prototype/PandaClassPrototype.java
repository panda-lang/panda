/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.architecture.prototype;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructors;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeFields;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethods;
import org.panda_lang.panda.framework.design.architecture.value.StaticValue;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.prototype.constructor.PandaConstructors;
import org.panda_lang.panda.framework.language.architecture.prototype.field.PandaFields;
import org.panda_lang.panda.framework.language.architecture.prototype.method.PandaMethods;
import org.panda_lang.panda.framework.language.architecture.value.PandaStaticValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class PandaClassPrototype implements ClassPrototype {

    private final String className;
    private final Class<?> associated;
    private final Collection<String> aliases;
    private boolean initialized;

    private final Collection<ClassPrototype> extended = new ArrayList<>(1);
    private final PrototypeConstructors constructors = new PandaConstructors();
    private final PrototypeFields fields = new PandaFields();
    private final PrototypeMethods methods = new PandaMethods();

    protected PandaClassPrototype(String className, Class<?> associated, Collection<String> aliases) {
        this.className = className;
        this.associated = associated;
        this.aliases = aliases;
    }

    protected PandaClassPrototype(PandaClassPrototypeBuilder builder) {
        this(builder.name, builder.associated, builder.aliases);
    }

    public synchronized void initialize() {
        if (initialized) {
            return;
        }

        this.initialized = true;

        for (PrototypeField field : fields.getListOfFields()) {
            if (!field.hasDefaultValue() || !field.isStatic()) {
                continue;
            }

            Expression expression = field.getDefaultValue();
            StaticValue staticValue = PandaStaticValue.of(expression.getExpressionValue(null));
            field.setStaticValue(staticValue);
        }
    }

    @Override
    public boolean isClassOf(String className) {
        if (this.getClassName().equals(className)) {
            return true;
        }

        if (this.associated != null && this.associated.getSimpleName().equals(className)) {
            return true;
        }

        for (String alias : this.getAliases()) {
            if (alias.equals(className)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isAssociatedWith(ClassPrototype prototype) { // this (Panda Class | Java Class) isAssociatedWith
        return prototype != null && (prototype.equals(this)
                || PandaClassPrototypeUtils.isAssociatedWith(associated, prototype.getAssociated())
                || PandaClassPrototypeUtils.hasCommonPrototypes(extended, prototype.getExtended()));
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
    public Collection<ClassPrototype> getExtended() {
        return extended;
    }

    @Override
    public Class<?> getAssociated() {
        return associated;
    }

    @Override
    public Collection<String> getAliases() {
        return aliases;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(className);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        return this == o;
    }

    @Override
    public String toString() {
        return "ClassPrototype::" + className;
    }

    public static PandaClassPrototype of(Class<?> type, String... aliases) {
        return builder()
                .associated(type)
                .aliases(aliases)
                .build();
    }

    public static <T> PandaClassPrototypeBuilder<?, ?> builder() {
        return new PandaClassPrototypeBuilder<>();
    }

}
