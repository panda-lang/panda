/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.framework.language.architecture.prototype.dynamic;

import javassist.ClassPool;
import javassist.CtClass;
import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.architecture.prototype.DynamicClass;
import org.panda_lang.framework.design.architecture.prototype.PrototypeModels;
import org.panda_lang.framework.language.architecture.prototype.PrototypeClass;
import org.panda_lang.utilities.commons.ClassPoolUtils;
import org.panda_lang.utilities.commons.ClassUtils;
import org.panda_lang.utilities.commons.ObjectUtils;
import org.panda_lang.utilities.commons.PackageUtils;
import org.panda_lang.utilities.commons.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public final class PandaDynamicClass implements DynamicClass {

    private static final AtomicInteger ID = new AtomicInteger();

    protected final String name;
    protected final String module;
    protected final String model;
    protected final Collection<DynamicClass> interfaces = new ArrayList<>();
    protected DynamicClass superclass = null;
    protected Class<?> structure = Object.class;
    protected Class<?> implementation = Object.class;
    protected boolean frozen;

    public PandaDynamicClass(String name, String module, String model) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }

        if (module == null) {
            throw new IllegalArgumentException("Module cannot be null");
        }

        if (model == null) {
            throw new IllegalArgumentException("Model cannot be null");
        }

        this.name = name;
        this.module = StringUtils.replace(module, ":", ".");
        this.model = model;
    }

    public PandaDynamicClass(Class<?> clazz, String customName, String module) {
        this(customName, module, PrototypeModels.of(clazz));
        this.structure = clazz;
        this.implementation = clazz;
        this.frozen = true;
    }

    public PandaDynamicClass(Class<?> clazz) {
        this(clazz, clazz.getSimpleName(), PackageUtils.toString(clazz.getPackage(), StringUtils.EMPTY));
    }

    @Override
    public void regenerate() throws Exception {
        if (frozen) {
            return;
        }

        CtClass generatedClass = ClassPool.getDefault().makeInterface(module + "." + name, ClassPoolUtils.get(PrototypeClass.class));

        if (superclass != null) {
            CtClass ctSuperclass = ClassPoolUtils.get(superclass.getStructure());

            this.structure = ClassPoolUtils.toClass(generatedClass);
        }

    }

    @Override
    public void append(Class<?> clazz) {
        DynamicClass dynamicClass = new PandaDynamicClass(clazz);

        if (dynamicClass.isClass()) {
            extendClass(dynamicClass);
        }
        else if (dynamicClass.isInterface()) {
            implementInterface(dynamicClass);
        }
        else {
            throw new PandaFrameworkException("Unsupported model " + model);
        }
    }

    @Override
    public void extendClass(DynamicClass superclass) {
        if (this.superclass != null) {
            throw new IllegalStateException("Class cannot extend more than one class");
        }

        this.superclass = superclass;
    }

    @Override
    public void implementInterface(DynamicClass interfaceClass) {
        interfaces.add(interfaceClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, module, model);
    }

    @Override
    public boolean equals(Object o) { // lgtm [java/unchecked-cast-in-equals]
        DynamicClass that = ObjectUtils.cast(o);
        return that != null && model.equals(that.getModule()) && name.equals(that.getSimpleName()) && model.equals(that.getModel());
    }

    @Override
    public boolean isAssignableFrom(Class<?> cls) {
        return ClassUtils.isAssignableFrom(implementation, cls);
    }

    @Override
    public boolean isClass() {
        return PrototypeModels.CLASS.equals(model);
    }

    @Override
    public boolean isInterface() {
        return PrototypeModels.INTERFACE.equals(model);
    }

    @Override
    public Class<?> getImplementation() {
        return implementation;
    }

    @Override
    public Class<?> getStructure() {
        return structure;
    }

    @Override
    public String getModel() {
        return model;
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
        return getModule() + "." + getSimpleName();
    }

}
