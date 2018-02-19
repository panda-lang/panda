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

package org.panda_lang.panda.language.structure.prototype.structure;

import com.google.common.base.Objects;
import org.panda_lang.panda.language.structure.general.expression.Expression;
import org.panda_lang.panda.language.structure.overall.module.Module;
import org.panda_lang.panda.language.structure.overall.module.ModuleRegistry;
import org.panda_lang.panda.language.structure.prototype.structure.constructor.PrototypeConstructor;
import org.panda_lang.panda.language.structure.prototype.structure.field.PrototypeField;
import org.panda_lang.panda.language.structure.prototype.structure.method.Methods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PandaClassPrototype implements ClassPrototype {

    private final Module module;
    private final String className;
    private final Collection<String> aliases;
    private final Collection<Class<?>> associated;
    private final Collection<ClassPrototype> extended;
    private final Collection<PrototypeConstructor> constructors;
    private final List<PrototypeField> fields;
    private final Methods methods;
    private boolean initialized;

    public PandaClassPrototype(Module module, String className, String... aliases) {
        this.module = module;
        this.className = className;
        this.aliases = Arrays.asList(aliases);
        this.associated = new ArrayList<>(1);
        this.extended = new ArrayList<>(1);
        this.constructors = new ArrayList<>(1);
        this.fields = new ArrayList<>();
        this.methods = new Methods(this);
        this.initialized = false;
    }

    public PandaClassPrototype(Module module, Class<?> clazz, String... aliases) {
        this(module, clazz.getSimpleName(), aliases);
        this.associated.add(clazz);
    }

    public synchronized void initialize() {
        if (initialized) {
            return;
        }

        initialized = true;

        for (PrototypeField field : this.getFields()) {
            if (!field.hasDefaultValue() || !field.isStatic()) {
                continue;
            }

            Expression expression = field.getDefaultValue();
            field.setStaticValue(expression.getExpressionValue(null));
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
    public PrototypeField getField(String fieldName) {
        for (PrototypeField field : fields) {
            if (!field.getName().equals(fieldName)) {
                continue;
            }

            return field;
        }

        return null;
    }

    @Override
    public Methods getMethods() {
        return methods;
    }

    @Override
    public List<PrototypeField> getFields() {
        return fields;
    }

    @Override
    public Collection<PrototypeConstructor> getConstructors() {
        return constructors;
    }

    @Override
    public Collection<ClassPrototype> getExtended() {
        return extended;
    }

    @Override
    public Collection<Class<?>> getAssociated() {
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
    public String getName() {
        return module.getName() + ":" + this.getClassName();
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
    public boolean equals(Object o) {
        return o == null || super.equals(o);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("className", className)
                .add("module", module)
                .add("extended", extended)
                .add("constructors", constructors)
                .add("fields", fields)
                .add("methods", methods)
                .toString();
    }

    public static ClassPrototype forName(String prototype) {
        return ModuleRegistry.forName(prototype);
    }

    public static ClassPrototype forClass(Class<?> clazz) {
        return ModuleRegistry.forClass(clazz);
    }

}
