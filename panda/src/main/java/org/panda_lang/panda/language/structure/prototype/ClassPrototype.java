/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.language.structure.prototype;

import com.google.common.base.Objects;
import org.panda_lang.panda.language.structure.group.Group;
import org.panda_lang.panda.language.structure.prototype.structure.constructor.Constructor;
import org.panda_lang.panda.language.structure.prototype.structure.field.Field;
import org.panda_lang.panda.language.structure.prototype.structure.method.Method;
import org.panda_lang.panda.language.structure.util.Container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClassPrototype {

    private final String className;
    private final Container<Group> group;
    private final Collection<ClassPrototype> extended;
    private final Collection<Constructor> constructors;
    private final Map<String, Field> fields;
    private final Map<String, Method> methods;

    public ClassPrototype(String className) {
        this.className = className;
        this.group = new Container<>();
        this.extended = new ArrayList<>();
        this.constructors = new ArrayList<>();
        this.fields = new HashMap<>();
        this.methods = new HashMap<>();
    }

    public Map<String, Method> getMethods() {
        return methods;
    }

    public Map<String, Field> getFields() {
        return fields;
    }

    public Collection<Constructor> getConstructors() {
        return constructors;
    }

    public Collection<ClassPrototype> getExtended() {
        return extended;
    }

    public Container<Group> getGroup() {
        return group;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public int hashCode() {
        return className.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o == null || super.equals(o);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("className", className)
                .add("group", group)
                .add("extended", extended)
                .add("constructors", constructors)
                .add("fields", fields)
                .add("methods", methods)
                .toString();
    }
}
