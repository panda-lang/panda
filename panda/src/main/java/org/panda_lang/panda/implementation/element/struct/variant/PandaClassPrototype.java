/*
 * Copyright (c) 2015-2016 Dzikoysk
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

package org.panda_lang.panda.implementation.element.struct.variant;

import org.panda_lang.panda.implementation.element.field.Field;
import org.panda_lang.panda.implementation.element.method.Method;
import org.panda_lang.panda.implementation.element.struct.ClassPrototype;
import org.panda_lang.panda.implementation.element.struct.constructor.Constructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PandaClassPrototype implements ClassPrototype {

    private final String className;
    private final Collection<Constructor> constructors;
    private final Map<String, Field> fields;
    private final Map<String, Method> methods;

    public PandaClassPrototype(String className) {
        this(className, new HashMap<>(), new HashMap<>());
    }

    protected PandaClassPrototype(String className, Map<String, Field> fields, Map<String, Method> methods) {
        this.className = className;
        this.constructors = new ArrayList<>();
        this.fields = fields;
        this.methods = methods;
    }

    @Override
    public Map<String, Method> getMethods() {
        return methods;
    }

    @Override
    public Map<String, Field> getFields() {
        return fields;
    }

    @Override
    public Collection<Constructor> getConstructors() {
        return constructors;
    }

    @Override
    public String getClassName() {
        return className;
    }

    public static PandaClassPrototypeBuilder builder() {
        return new PandaClassPrototypeBuilder();
    }

}
