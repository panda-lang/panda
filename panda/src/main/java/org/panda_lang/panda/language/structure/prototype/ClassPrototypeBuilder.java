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

import org.panda_lang.panda.language.structure.prototype.field.Field;
import org.panda_lang.panda.language.structure.prototype.method.Method;

import java.util.HashMap;
import java.util.Map;

public class ClassPrototypeBuilder {

    private String className;
    private Map<String, Field> fields;
    private Map<String, Method> methods;

    public ClassPrototypeBuilder() {
        this.fields = new HashMap<>();
        this.methods = new HashMap<>();
    }

    public ClassPrototypeBuilder className(String className) {
        this.className = className;
        return this;
    }

    public ClassPrototypeBuilder method(Method method) {
        methods.put(method.getName(), method);
        return this;
    }

    public ClassPrototype build() {
        return new ClassPrototype(className, fields, methods);
    }

}
