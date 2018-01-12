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
import org.panda_lang.panda.language.structure.overall.module.Module;
import org.panda_lang.panda.language.structure.overall.module.ModuleRegistry;
import org.panda_lang.panda.language.structure.prototype.structure.constructor.Constructor;
import org.panda_lang.panda.language.structure.prototype.structure.field.Field;
import org.panda_lang.panda.language.structure.prototype.structure.method.Methods;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClassPrototype {

    private final Module module;
    private final String className;
    private final Collection<Class<?>> associated;
    private final Collection<ClassPrototype> extended;
    private final Collection<Constructor> constructors;
    private final List<Field> fields;
    private final Methods methods;

    public ClassPrototype(Module module, String className) {
        this.module = module;
        this.className = className;
        this.associated = new ArrayList<>(1);
        this.extended = new ArrayList<>(1);
        this.constructors = new ArrayList<>(1);
        this.fields = new ArrayList<>();
        this.methods = new Methods(this);
    }

    public Field getField(String fieldName) {
        for (Field field : fields) {
            if (!field.getName().equals(fieldName)) {
                continue;
            }

            return field;
        }

        return null;
    }

    public Methods getMethods() {
        return methods;
    }

    public List<Field> getFields() {
        return fields;
    }

    public Collection<Constructor> getConstructors() {
        return constructors;
    }

    public Collection<ClassPrototype> getExtended() {
        return extended;
    }

    public Collection<Class<?>> getAssociated() {
        return associated;
    }

    public Module getModule() {
        return module;
    }

    public String getName() {
        return module.getName() + ":" + this.getClassName();
    }

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

}
