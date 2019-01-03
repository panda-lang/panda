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

package org.panda_lang.panda.framework.language.architecture.prototype.clazz;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructors;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeFields;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethods;
import org.panda_lang.panda.framework.language.architecture.prototype.clazz.constructor.PandaConstructors;
import org.panda_lang.panda.framework.language.architecture.prototype.clazz.field.PandaFields;
import org.panda_lang.panda.framework.language.architecture.prototype.clazz.method.PandaMethods;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class AbstractClassPrototype implements ClassPrototype {

    protected final String name;
    protected final Class<?> associated;
    protected final Collection<String> aliases;
    protected final Collection<ClassPrototype> extended = new ArrayList<>(1);
    protected final PrototypeConstructors constructors = new PandaConstructors();
    protected final PrototypeFields fields = new PandaFields();
    protected final PrototypeMethods methods = new PandaMethods();

    public AbstractClassPrototype(String name, Class<?> associated, Collection<String> aliases) {
        this.name = name;
        this.associated = associated;
        this.aliases = new ArrayList<>(aliases);
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
    public boolean isAssignableFrom(ClassPrototype prototype) { // this (Panda Class | Java Class) isAssociatedWith
        if (prototype == null) {
            return true;
        }

        return prototype.equals(this)
                || PandaClassPrototypeUtils.isAssignableFrom(associated, prototype.getAssociated())
                || PandaClassPrototypeUtils.hasCommonPrototypes(extended, prototype.getExtended());
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
    public String getClassName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        return this == o;
    }

    @Override
    public String toString() {
        return "ClassPrototype::" + name;
    }

}
