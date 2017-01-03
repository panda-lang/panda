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

package org.panda_lang.panda.language.structure.struct;

import org.panda_lang.panda.language.structure.field.Field;
import org.panda_lang.panda.language.structure.method.Method;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class ClassPrototype {

    private final String className;
    private final Collection<Constructor> constructors;
    private final Map<String, Field> fields;
    private final Map<String, Method> methods;

    protected ClassPrototype(String className, Map<String, Field> fields, Map<String, Method> methods) {
        this.className = className;
        this.constructors = new ArrayList<>();
        this.fields = fields;
        this.methods = methods;
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

    public String getClassName() {
        return className;
    }

    public static ClassPrototypeBuilder builder() {
        return new ClassPrototypeBuilder();
    }

}
