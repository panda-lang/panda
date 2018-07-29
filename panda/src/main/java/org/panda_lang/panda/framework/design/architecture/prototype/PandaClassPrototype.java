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

package org.panda_lang.panda.framework.design.architecture.prototype;

import com.google.common.base.Objects;
import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PandaConstructors;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PandaFields;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PandaMethods;
import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructors;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeFields;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethods;
import org.panda_lang.panda.framework.design.architecture.value.StaticValue;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.value.PandaStaticValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class PandaClassPrototype implements ClassPrototype {

    private final Module module;
    private final String className;
    private final Class<?> associated;
    private final Collection<String> aliases;
    private final Collection<ClassPrototype> extended;
    private final PrototypeConstructors constructors;
    private final PrototypeFields fields;
    private final PrototypeMethods methods;
    private boolean initialized;

    public PandaClassPrototype(Module module, String className, Class<?> associated, String... aliases) {
        this.module = module;
        this.className = className;
        this.associated = associated;
        this.aliases = Arrays.asList(aliases);
        this.extended = new ArrayList<>(1);
        this.constructors = new PandaConstructors();
        this.fields = new PandaFields();
        this.methods = new PandaMethods();
        this.initialized = false;
    }

    public PandaClassPrototype(Module module, Class<?> clazz, String... aliases) {
        this(module, clazz.getSimpleName(), clazz, aliases);
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
    public Module getModule() {
        return module;
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
        // TODO: wtf
        //noinspection Contract
        return o == null || super.equals(o);
    }

    @Override
    public String toString() {
        return "ClassPrototype::" + className;
    }

}
