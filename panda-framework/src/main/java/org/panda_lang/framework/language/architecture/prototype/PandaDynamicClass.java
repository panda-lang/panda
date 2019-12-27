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

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.prototype.DynamicClass;
import org.panda_lang.utilities.commons.ClassUtils;
import org.panda_lang.utilities.commons.ObjectUtils;
import org.panda_lang.utilities.commons.StringUtils;

public final class PandaDynamicClass implements DynamicClass {

    private final String name;
    private final String module;
    private Class<?> implementation;

    public PandaDynamicClass(Module module, String name) {
        this.name = name;
        this.module = StringUtils.replace(module.toString(), ":", ".");
    }

    public PandaDynamicClass(Module module, String name, Class<?> implementation) {
        this(module, name);
        this.reimplement(implementation);
    }

    @Override
    public void reimplement(Class<?> implementation) {
        this.implementation = implementation;
    }

    @Override
    public int hashCode() {
        return (31 * name.hashCode()) + module.hashCode();
    }

    @Override
    public boolean equals(Object o) { // lgtm [java/unchecked-cast-in-equals]
        DynamicClass that = ObjectUtils.cast(o);
        return that != null && name.equals(that.getName()) && module.equals(that.getModule());
    }

    @Override
    public boolean isAssignableFrom(Class<?> cls) {
        return ClassUtils.isAssignableFrom(implementation, cls);
    }

    @Override
    public Class<?> getImplementation() {
        return implementation;
    }

    @Override
    public String getModule() {
        return module;
    }

    @Override
    public String getSimpleName() {
        return name;
    }

    @Override
    public String getName() {
        return module + "." + getSimpleName();
    }

}
